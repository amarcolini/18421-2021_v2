package org.firstinspires.ftc.teamcode.old.robot.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.amarcolini.joos.command.Command;
import com.amarcolini.joos.command.FunctionalCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.old.DuckDetector;
import org.firstinspires.ftc.teamcode.old.robot.FedEx;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "AutoBlueStorageUnit", group = "actual")
public class AutoBlueStorageUnit extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final FedEx bot = new FedEx(this);
        bot.drive.setPoseEstimate(new Pose2d(-38.50, 61.50, Math.toRadians(-90.00)));
        bot.camera.start();

        final TrajectorySequence carousel = bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                .forward(5.0)
                .addDisplacementMarker(bot.bucket::sit)
                .turn(Math.toRadians(-95.0))
                .forward(11.0)
                .setAccelConstraint(new ProfileAccelerationConstraint(10.0))
                .forward(7.0)
                .build();

        waitForStart();

        bot.camera.close();
        final DuckDetector.Position spot = bot.camera.getLastPosition();
        telemetry.addData("spot", spot);
        telemetry.update();

        bot.drive.followTrajectorySequence(carousel);

        bot.spinner.start();
        sleep(3000);
        bot.spinner.stop();

        int position = 1;
        double distance = 2;
        switch (spot) {
            case TWO: {
                position = 2;
                distance = 4.0;
                break;
            }
            case THREE: {
                position = 3;
                distance = 6.0;
                break;
            }
        }
        telemetry.addData("position", position);
        telemetry.update();

        final TrajectorySequence shippingHub = bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                .back(0.1)
                .splineTo(new Vector2d(-37, 56), Math.toRadians(0))
                .splineTo(new Vector2d(-18, 49), Math.toRadians(-90))
                .splineTo(new Vector2d(-18, 47 - distance), Math.toRadians(-90.0))
                .build();

        bot.drive.followTrajectorySequence(shippingHub);
        bot.drive.turnTo(90);

        bot.lift.setLevel(position).run();
        bot.bucket.open();
        sleep(2000);
        bot.bucket.close();
        sleep(500);
        bot.lift.setLevel(1).run();


//        switch(position) {
//            case 1: {
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .forward(6.0)
//                                .splineTo(new Vector2d(-27, 39), Math.toRadians(-80))
//                                .forward(3.0)
//                                .addDisplacementMarker(5.0, () -> {
//                                    bot.intake.start();
//                                    bot.conveyor.start();
//                                })
//                                .build()
//                );
//                bot.intake.stop();
//                bot.conveyor.stop();
//                sleep(500);
//                bot.bucket.sit();
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .back(4.0)
//                                .turn(Math.toRadians(-160))
//                                .build()
//                );
//                break;
//            }
//            case 2: {
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .splineTo(new Vector2d(-37, 50), Math.toRadians(-90))
//                                .forward(12)
//                                .addDisplacementMarker(5.0, () -> {
//                                    bot.intake.start();
//                                    bot.conveyor.start();
//                                })
//                                .build()
//                );
//                bot.intake.stop();
//                bot.conveyor.stop();
//                sleep(500);
//                bot.bucket.sit();
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .back(3)
//                                .splineTo(new Vector2d(-33, 33), Math.toRadians(-30))
//                                .build()
//                );
//                break;
//            }
//            case 3: {
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .splineTo(new Vector2d(-40, 38), Math.toRadians(-180))
//                                .addDisplacementMarker(5.0, () -> {
//                                    bot.intake.start();
//                                    bot.conveyor.start();
//                                })
//                                .build()
//                );
//                bot.intake.stop();
//                bot.conveyor.stop();
//                sleep(500);
//                bot.bucket.sit();
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .back(0.1)
//                                .splineTo(new Vector2d(-36, 28), Math.toRadians(-30))
//                                .turn(Math.toRadians(30))
//                                .build()
//                );
//                break;
//            }
//        }
//
//        bot.bucket.sit();
//        bot.lift.setLevel(3).run();
//        bot.bucket.open();
//        sleep(2000);
//        bot.bucket.close();
//        sleep(500);
//        bot.lift.setLevel(1).run();

        switch (position) {
            case 1:
            case 2: {
                bot.drive.followTrajectorySequence(
                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                                .splineTo(new Vector2d(-60, 38), Math.toRadians(180.0))
                                .build()
                );
                bot.drive.turnTo(Math.toRadians(180.0));
            }
                break;
            case 3: {
                bot.drive.followTrajectorySequence(
                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                                .splineTo(new Vector2d(-40, 53), Math.toRadians(180.0))
                                .splineTo(new Vector2d(-58, 36), Math.toRadians(-90.0))
                                .build()
                );
                bot.drive.turnTo(Math.toRadians(-90.0));
            }
                break;
        }
    }
}
