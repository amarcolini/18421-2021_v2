package org.firstinspires.ftc.teamcode.old.robot.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.old.DuckDetector;
import org.firstinspires.ftc.teamcode.old.robot.FedEx;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "AutoRedWarehouse", group = "actual")
public class AutoRedWarehouse extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final FedEx bot = new FedEx(this);
        bot.drive.setPoseEstimate(new Pose2d(12, -61.50, Math.toRadians(90.00)));
        bot.camera.start();

        waitForStart();

        bot.camera.close();
        final DuckDetector.Position spot = bot.camera.getLastPosition();
        telemetry.addData("spot", spot);
        telemetry.update();

        double distance = 8;
        switch (spot) {
            case ONE:
                break;
            case TWO: distance = 10;
                break;
            case THREE: distance = 11;
                break;
        }

        final TrajectorySequence shippingHub = bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                .splineTo(new Vector2d(-10, -55), Math.toRadians(-90))
                .back(0.01)
                .splineTo(new Vector2d(-10, -55 + distance), Math.toRadians(90))
                .addDisplacementMarker(5, bot.bucket::sit)
                .build();

        bot.drive.followTrajectorySequence(shippingHub);

        switch (spot) {
            case ONE: bot.lift.setLevel(1).run();
                break;
            case TWO: bot.lift.setLevel(2).run();
                break;
            case THREE: bot.lift.setLevel(3).run();
                break;
        }

        bot.bucket.open();
        sleep(2000);
        bot.bucket.close();
        sleep(500);
        bot.lift.setLevel(1).run();

        final TrajectorySequence warehouse = bot.drive.trajectorySequenceBuilder()
                .splineTo(new Vector2d(-5, -47), Math.toRadians(0))
//                                .turn(Math.toRadians(-90))
                .setVelConstraint(new TranslationalVelocityConstraint(60))
                .setAccelConstraint(new ProfileAccelerationConstraint(50))
                .forward(60)
                .build();

        bot.drive.followTrajectorySequence(warehouse);
    }
}
