package org.firstinspires.ftc.teamcode.old.robot.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.old.robot.FedEx;

@TeleOp(name = "TeleOpBlue", group = "actual")
public class TeleOpBlue extends OpMode {
    private FedEx bot;

    @Override
    public void init() {
        bot = new FedEx(this);
        bot.initTeleOp();
    }

    @Override
    public void start() {
        bot.bucket.close();
    }

    @Override
    public void loop() {
        bot.update();
    }
}
