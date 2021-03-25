package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;

public class Magazine extends SubsystemBase {
    private TalonSRX front;
    private TalonSRX bottom;

    private final static Magazine INSTANCE = new Magazine();

    private Magazine() {
        this.front = new TalonSRX(RobotMap.MAGAZINE.FRONT);
        this.front.setNeutralMode(NeutralMode.Brake);
        this.bottom = new TalonSRX(RobotMap.MAGAZINE.BOTTOM);
        this.bottom.setNeutralMode(NeutralMode.Brake);
        this.bottom.setInverted(true);
    }

    public void runFront(double power){
        this.front.set(ControlMode.PercentOutput, power);
    }

    public void runBottom(double power){
        this.bottom.set(ControlMode.PercentOutput, power);
    }

    public void run(double front, double bottom){
        this.runFront(front);
        this.runBottom(bottom);
    }

    public void stop(){
        this.run(0, 0);
    }

    public static Magazine getInstance() {
        return INSTANCE;
    }

}

