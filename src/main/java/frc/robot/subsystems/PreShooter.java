package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class PreShooter extends SubsystemBase {

// Any variables/fields used in the constructor must appear before the "INSTANCE" variable
// so that they are initialized before the constructor is called.
    private TalonSRX top;

    /**
     * The Singleton instance of this PreShooter. External classes should
     * use the {@link #getInstance()} method to get the instance.
     */
    private final static PreShooter INSTANCE = new PreShooter();

    /**
     * Creates a new instance of this PreShooter.
     * This constructor is private since this class is a Singleton. External classes
     * should use the {@link #getInstance()} method to get the instance.
     */
    private PreShooter() {
        top = new TalonSRX(RobotMap.MAGAZINE.TOP);
    }

    public void spin(double power){
        this.top.set(ControlMode.PercentOutput, power);
    }

    public void stop(){
        this.spin(0);
    }

    /**
     * Returns the Singleton instance of this PreShooter. This static method
     * should be used -- {@code PreShooter.getInstance();} -- by external
     * classes, rather than the constructor to get the instance of this class.
     */
    public static PreShooter getInstance() {
        return INSTANCE;
    }

}

