package org.firstinspires.ftc.teamcode.old.robot;

import com.amarcolini.joos.command.Component;
import com.amarcolini.joos.hardware.Motor;

public class Conveyor implements Component {
    private final Motor motor;
    private Boolean isActive = false;

    public Conveyor(Motor motor) {
        this.motor = motor;
    }

    public void start() {
        motor.setPower(1.0);
        isActive = true;
    }

    public void stop() {
        motor.setPower(0.0);
        isActive = false;
    }

    public void toggle() {
        if (isActive) stop();
        else start();
    }

    public Boolean isActive() {
        return isActive;
    }

    public void reverse() {
        motor.setPower(-1.0);
    }

    @Override
    public void update() {
        motor.update();
    }
}
