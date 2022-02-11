package org.firstinspires.ftc.teamcode.old.robot;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.amarcolini.joos.command.Command;
import com.amarcolini.joos.command.Component;
import com.amarcolini.joos.command.Robot;
import com.amarcolini.joos.hardware.Imu;
import com.amarcolini.joos.hardware.Motor;
import com.amarcolini.joos.hardware.MotorGroup;
import com.amarcolini.joos.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.drive.SampleTankDrive;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Config
public class FedEx extends Robot {
    public final Lift lift;
    public final Spinner spinner;
    public final Intake intake;
    public final Conveyor conveyor;
    private DcMotorEx conveyorMotor;
    public final Bucket bucket;
    public final SampleTankDrive drive;
    public final Camera camera;
    public final MotorGroup left;
    public final MotorGroup right;
    public final Imu imu;
    private boolean reversed = false;
    public static double freightAmps = 1.3;

    public FedEx(@NonNull OpMode opMode) {
        super(opMode);

        //Initializing components
        lift = new Lift(new Motor(hMap, "lift", 435, 384.5));
        bucket = new Bucket(new Servo(hMap, "bucket"));
        left = new MotorGroup(
                new Motor(hMap, "front_left", 312.0, 537.7, 2.4, 1.0),
                new Motor(hMap, "back_left", 312.0, 537.7, 2.4, 1.0).reversed()
        );
        right = new MotorGroup(
                new Motor(hMap, "front_right", 312.0, 537.7, 2.4, 1.0).reversed(),
                new Motor(hMap, "back_right", 312.0, 537.7, 2.4, 1.0)
        );
        left.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        right.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        imu = new Imu(hMap, "imu");
        imu.setAxis(Imu.Axis.Z);
        drive = new SampleTankDrive(hMap);

        spinner = new Spinner(hMap.get(DcMotor.class, "spinner"));
        intake = new Intake(new Motor(hMap, "intake", 1620));
        conveyorMotor = hMap.get(DcMotorEx.class, "conveyor");
        conveyor = new Conveyor(new Motor(conveyorMotor, 1620));
        camera = new Camera(hMap, "webcam");

        register(lift, bucket, spinner, intake, conveyor, gamepad, Component.of(drive::update));
    }

    public void initTeleOp() {
        bucket.close();

        schedule(Command.of(() -> {
            com.amarcolini.joos.geometry.Vector2d stick = gamepad.p1.getLeftStick();
            drive.setDrivePower(new Pose2d(
                    -stick.y * (reversed || lift.getLevel() > 1 ? -1 : 1) * (lift.getLevel() > 1 ? 0.5 : 1),
                    0,
                    -stick.x * (lift.getLevel() > 1 ? 0.8 : 1)
            ));
        }).runUntil(false));

        AtomicBoolean hasFreight = new AtomicBoolean(false);
        AtomicInteger state = new AtomicInteger();
        schedule(Command.of(() -> {
            final double amps = conveyorMotor.getCurrent(CurrentUnit.AMPS);
            if (amps > freightAmps) {
                switch (state.get()) {
                    case 0: state.set(1);
                    break;
                    case 2: {
                        hasFreight.set(true);
                        state.set(3);
                        schedule(spinner.spinDuck());
                    }
                    break;
                }
            } else if (state.get() == 1) state.set(2);
            if (conveyorMotor.getPower() == 0) state.set(0);
            getPacket().put("current", amps);
            getPacket().put("state", state.get());
            telemetry.addData("reversed", reversed);
            telemetry.addLine(hasFreight.get() ? "YOU'VE GOT FREIGHT!" : "You do not have freight.");
        }).runUntil(false));

        map(() -> gamepad.p1.a.justActivated() && !(intake.isActive() || conveyor.isActive()), Command.select(() -> {
            int newLevel = lift.getLevel() + 1;
            if (newLevel > 3) newLevel = 1;

            if (newLevel == 1) bucket.close();
            else bucket.sit();

            return lift.setLevel(newLevel);
        }).requires(lift));
        map(() -> gamepad.p1.b.justActivated() && requiring(lift) == null, () -> {
            bucket.toggle();
            if (bucket.isOpen()) hasFreight.set(false);
        });
        map(() -> gamepad.p1.x.justActivated() && lift.getLevel() == 1, Command.of(() -> {
            conveyor.toggle();
            intake.toggle();
        }).requires(intake, conveyor));
        map(gamepad.p1.y::justActivated, spinner.spinDuck());
        map(gamepad.p1.left_bumper::justActivated, Command.of(() -> {
            intake.reverse();
            conveyor.reverse();
        })
                .requires(intake, conveyor)
                .runUntil(() -> !gamepad.p1.left_bumper.isActive())
                .onEnd((cmd, interrupted) -> {
            if (intake.isActive()) intake.start();
            else intake.stop();
            if (conveyor.isActive()) conveyor.start();
            else conveyor.stop();
        }));
        map(gamepad.p1.right_bumper::justActivated, () -> reversed = !reversed);
    }

    @Override
    public void init() {
        if (isInTeleOp) initTeleOp();
    }

    @Override
    public void start() {

    }
}
