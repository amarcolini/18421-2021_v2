package com.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TranslationalVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(30, 30, 2.732, Math.toRadians(180), 30)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-38.50, 61.50, Math.toRadians(-90.00)))
                                .forward(5.0)
//                                .addDisplacementMarker(bot.bucket::sit)
                                .turn(Math.toRadians(-95.0))
                                .forward(11.0)
                                .setAccelConstraint(new ProfileAccelerationConstraint(10.0))
                                .forward(7.0)
                                .waitSeconds(0.5)
                                .back(0.1)
                                .splineTo(new Vector2d(-37, 56), Math.toRadians(0))
                                .splineTo(new Vector2d(-18, 49), Math.toRadians(-90))
                                .splineTo(new Vector2d(-18, 47 - 4), Math.toRadians(-90.0))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}