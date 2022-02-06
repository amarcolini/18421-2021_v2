package org.firstinspires.ftc.teamcode.old.robot.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.old.robot.FedEx;

@TeleOp(name = "TeleOpRed", group = "actual")
public class TeleOpRed extends OpMode {
    private FedEx bot;

    @Override
    public void init() {
        bot = new FedEx(this);
        bot.initTeleOp();
        bot.spinner.reversed = true;
    }

    @Override
    public void loop() {
        bot.update();
    }
}
