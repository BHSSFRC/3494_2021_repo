package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
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
        this.bottom = new TalonSRX(RobotMap.MAGAZINE.BOTTOM);
        this.bottom.setInverted(true);
    }

    public void runFront(boolean on){
        if(on){
            this.front.set(ControlMode.PercentOutput, RobotConfig.MAGAZINE.FRONT_MOTOR_DEFAULT_POWER);
        }else{
            this.front.set(ControlMode.PercentOutput, 0);
        }
    }

    public void runReverse(){
        this.front.set(ControlMode.PercentOutput, -RobotConfig.MAGAZINE.FRONT_MOTOR_DEFAULT_POWER);
        this.bottom.set(ControlMode.PercentOutput, -RobotConfig.MAGAZINE.BOTTOM_MOTOR_DEFAULT_POWER);
    }

    public void runBottom(boolean on){
        if(on){
            this.bottom.set(ControlMode.PercentOutput, RobotConfig.MAGAZINE.BOTTOM_MOTOR_DEFAULT_POWER);
        }else{
            this.bottom.set(ControlMode.PercentOutput, 0);
        }
    }

    public void run(boolean front, boolean bottom){
        this.runFront(front);
        this.runBottom(bottom);
    }

    public void stop(){
        this.runFront(false);
        this.runBottom(false);
    }

    public static Magazine getInstance() {
        return INSTANCE;
    }

}

