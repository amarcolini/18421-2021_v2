package org.firstinspires.ftc.teamcode.old.robot;

import com.acmerobotics.dashboard.config.Config;
import com.amarcolini.joos.command.Command;
import com.amarcolini.joos.command.Component;
import com.amarcolini.joos.command.InstantCommand;
import com.qualcomm.robotcore.hardware.DcMotor;

@Config
public class Spinner implements Component {
    private final DcMotor motor;
    private boolean isActive = false;
    public boolean reversed = false;
    public static double normalTime = 1.0;
    public static double fastTime = 0.1;
    public static double normalSpeed = 0.3;
    public static double fastSpeed = 1.0;

    public Spinner(DcMotor motor) {
        this.motor = motor;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void start() {
        if (reversed) motor.setPower(-0.3);
        else motor.setPower(0.3);
        isActive = true;
    }

    public Command spinDuck() {
        final double newNormalTime = normalTime;
        final double newFastTime = fastTime;
        final double newNormalSpeed = normalSpeed;
        final double newFastSpeed = fastSpeed;
        return new InstantCommand(() -> motor.setPower(newNormalSpeed * (reversed ? -1 : 1)))
                .wait(newNormalTime)
                .then(new InstantCommand(() -> motor.setPower(newFastSpeed * (reversed ? -1 : 1))))
                .wait(newFastTime)
                .onEnd((cmd, interrupted) -> motor.setPower(0.0))
                .isInterruptable(true)
                .requires(this);
    }

    public void stop() {
        motor.setPower(0.0);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void toggle() {
        if (isActive) stop();
        else start();
    }

    @Override
    public void update() {
    }
}
