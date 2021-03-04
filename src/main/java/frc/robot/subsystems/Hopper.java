package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Hopper extends SubsystemBase {

    private TalonSRX motor;

    private final static Hopper INSTANCE = new Hopper();

    private Hopper() {
        this.motor = new TalonSRX(RobotMap.HOPPER.MOTOR);
        this.motor.configOpenloopRamp(1);
    }

    public void spin(double power) {
        this.motor.set(ControlMode.PercentOutput, -power);
    }

    public void stop() {
        this.motor.set(ControlMode.PercentOutput, 0);
    }

    public static Hopper getInstance() {
        return INSTANCE;
    }

}

