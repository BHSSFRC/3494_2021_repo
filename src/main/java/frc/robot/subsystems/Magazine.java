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

    public void runFront(boolean on, boolean reverse){
        if(on){
            if (reverse) this.front.set(ControlMode.PercentOutput, -RobotConfig.MAGAZINE.FRONT_MOTOR_DEFAULT_POWER);
            else this.front.set(ControlMode.PercentOutput, RobotConfig.MAGAZINE.FRONT_MOTOR_DEFAULT_POWER);
        }else{
            this.front.set(ControlMode.PercentOutput, 0);
        }
    }

    public void runBottom(boolean on, boolean reverse){
        if(on){
            if (reverse) this.bottom.set(ControlMode.PercentOutput, -RobotConfig.MAGAZINE.BOTTOM_MOTOR_DEFAULT_POWER);
            else this.bottom.set(ControlMode.PercentOutput, RobotConfig.MAGAZINE.BOTTOM_MOTOR_DEFAULT_POWER);
        }else{
            this.bottom.set(ControlMode.PercentOutput, 0);
        }
    }

    public void runReverse(){
        this.front.set(ControlMode.PercentOutput, -RobotConfig.MAGAZINE.FRONT_MOTOR_DEFAULT_POWER);
        this.bottom.set(ControlMode.PercentOutput, -RobotConfig.MAGAZINE.BOTTOM_MOTOR_DEFAULT_POWER);
    }

    public void run(boolean front, boolean frontReverse, boolean bottom, boolean bottomReverse){
        this.runFront(front, frontReverse);
        this.runBottom(bottom, bottomReverse);
    }

    public void stop(){
        this.runFront(false, false);
        this.runBottom(false, false);
    }

    public static Magazine getInstance() {
        return INSTANCE;
    }

}

