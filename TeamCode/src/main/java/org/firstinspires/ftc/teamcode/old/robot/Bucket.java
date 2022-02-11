package org.firstinspires.ftc.teamcode.old.robot;

import com.amarcolini.joos.command.Component;
import com.amarcolini.joos.hardware.Servo;

public class Bucket implements Component {
    private final Servo servo;
    private int state = 1;
    private int lastState = 1;

    public Bucket(Servo servo) {
        this.servo = servo;
        servo.setPosition(0.12);
    }

    public void open() {
        servo.setPosition(0.55);
        lastState = state;
        state = 3;
    }

    public boolean isOpen() {
        return state == 3;
    }

    public void sit() {
        servo.setPosition(0.2);
        lastState = state;
        state = 2;
    }

    public void close() {
        servo.setPosition(0.0);
        lastState = state;
        state = 1;
    }

    public void toggle() {
        switch (state) {
            case 2:
            case 1:
                open();
            break;
            default:
                if (lastState == 2) sit();
                else close();
        }
    }

    @Override
    public void update() {
        servo.update();
    }
}
