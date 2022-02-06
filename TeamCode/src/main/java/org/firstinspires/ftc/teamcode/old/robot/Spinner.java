package org.firstinspires.ftc.teamcode.old.robot;

import com.amarcolini.joos.command.Component;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Spinner implements Component {
    private final DcMotor motor;
    private boolean isActive = false;
    public boolean reversed = false;

    public Spinner(DcMotor motor) {
        this.motor = motor;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void start() {
        if (reversed) motor.setPower(-0.3);
        else motor.setPower(0.3);
        isActive = true;
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
