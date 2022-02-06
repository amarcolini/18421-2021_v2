package org.firstinspires.ftc.teamcode.old.robot.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.old.DuckDetector;
import org.firstinspires.ftc.teamcode.old.robot.FedEx;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "AutoRedStorageUnit", group = "actual")
public class AutoRedStorageUnit extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        final FedEx bot = new FedEx(this);
        bot.drive.setPoseEstimate(new Pose2d(-32.5, -61.5, Math.toRadians(90.00)));
        bot.spinner.reversed = true;
        bot.camera.start();

        final TrajectorySequence carousel = bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                .setAccelConstraint(new ProfileAccelerationConstraint(20))
                .splineTo(new Vector2d(-53, -56), Math.toRadians(-110))
                .addDisplacementMarker(5, bot.bucket::sit)
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
        double distance = 1.0;
        switch (spot) {
            case TWO: {
                position = 2;
                distance = 1.0;
                break;
            }
            case THREE: {
                position = 3;
                distance = 3.0;
                break;
            }
        }
        telemetry.addData("position", position);
        telemetry.update();

        final TrajectorySequence shippingHub = bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                .back(0.1)
                .splineTo(new Vector2d(-20, -44), Math.toRadians(80))
                .back(distance)
                .build();

        bot.drive.followTrajectorySequence(shippingHub);
        bot.drive.turnTo(Math.toRadians(-100));

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
//                                .splineTo(new Vector2d(-40, -33), Math.toRadians(-180))
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
//                                .splineTo(new Vector2d(-30, -28), Math.toRadians(30))
//                                .turn(Math.toRadians(10))
//                                .build()
//                );
//                break;
//            }
//            case 2: {
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .splineTo(new Vector2d(-30, -50), Math.toRadians(90))
//                                .forward(15)
//                                .addDisplacementMarker(5.0, () -> {
//                                    bot.intake.start();
//                                    bot.conveyor.start();
//                                })
//                                .build()
//                );
//                bot.intake.stop();
//                bot.conveyor.stop();
//                sleep(1000);
//                bot.bucket.sit();
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .turn(Math.toRadians(120))
//                                .back(6.0)
//                                .build()
//                );
//                break;
//            }
//            case 3: {
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .forward(6.0)
//                                .splineTo(new Vector2d(-25, -40), Math.toRadians(66))
//                                .addDisplacementMarker(5.0, () -> {
//                                    bot.intake.start();
//                                    bot.conveyor.start();
//                                })
//                                .build()
//                );
//                sleep(500);
//                bot.intake.stop();
//                bot.conveyor.stop();
//                bot.drive.followTrajectorySequence(
//                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                                .turn(Math.toRadians(160))
//                                .back(4.0)
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
            case 3:
            case 2: {
                    bot.drive.followTrajectorySequence(
                            bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                                    .splineTo(new Vector2d(-60, -38), Math.toRadians(-180.0))
                                    .build()
                    );
                    bot.drive.turnTo(Math.toRadians(-180.0));
            }
                break;
            case 1: {
                bot.drive.followTrajectorySequence(
                        bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
                                .splineTo(new Vector2d(-40, -53), Math.toRadians(-180.0))
                                .splineTo(new Vector2d(-58, -36), Math.toRadians(90.0))
                                .build()
                );
                bot.drive.turnTo(Math.toRadians(90.0));
            }
                break;
        }

//        bot.drive.followTrajectorySequence(
//                bot.drive.trajectorySequenceBuilder(bot.drive.getPoseEstimate())
//                        .splineTo(new Vector2d(-60, -38), Math.toRadians(-180.0))
//                        .build()
//        );
    }
}
