package frc.robot.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class Linebreaker {
    private DigitalInput di;

    public Linebreaker(int port) {
        this.di = new DigitalInput(port);
    }

    public boolean lineBroken() {
        return !this.di.get();
    }
}
