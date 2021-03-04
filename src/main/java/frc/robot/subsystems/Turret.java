package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;
import frc.robot.RobotMap;

public class Turret extends PIDSubsystem {

// Any variables/fields used in the constructor must appear before the "INSTANCE" variable
// so that they are initialized before the constructor is called.

    /**
     * The Singleton instance of this Turret. External classes should
     * use the {@link #getInstance()} method to get the instance.
     */
    private TalonSRX turret;
    private final static Turret INSTANCE = new Turret();

    private DoubleSolenoid diskBrake;

    private DigitalInput dio1;
    private DigitalInput dio2;

    private Encoder encoder;
    private DigitalInput back;
    private DigitalInput front;

    private double frontSoftLimit;
    private double backSoftLimit;
    private double frontHardLimit;
    private double backHardLimit;

    private double setpoint;

    /**
     * Creates a new instance of this Turret.
     * This constructor is private since this class is a Singleton. External classes
     * should use the {@link #getInstance()} method to get the instance.
     */
    private Turret() {
        // TODO: Set the default command, if any, for this subsystem by calling setDefaultCommand(command)
        //       in the constructor or in the robot coordination class, such as RobotContainer.
        //       Also, you can call addChild(name, sendableChild) to associate sendables with the subsystem
        //       such as SpeedControllers, Encoders, DigitalInputs, etc.
        super(new PIDController(0.5,0,0));
        this.turret = new TalonSRX(RobotMap.TURRET.MOTOR);
        //this.turret.configFactoryDefault();
        this.turret.setNeutralMode(NeutralMode.Brake);
        //this.turret.configOpenloopRamp(2);
        //this.turret.configContinuousCurrentLimit(2);

        getController().enableContinuousInput(-1,1);
        getController().setTolerance(.03);
        getController().setSetpoint(180);
        this.setpoint = 180;

        //12:1 ratio
        //channel A = 0, B = 1

        this.dio1 = new DigitalInput(RobotMap.TURRET.ENCODER_CHANNEL_A);
        this.dio2 = new DigitalInput(RobotMap.TURRET.ENCODER_CHANNEL_B);
        this.encoder= new Encoder(dio1, dio2);
        this.encoder.setDistancePerPulse((double)1 / 256);
        this.encoder.reset();
        this.back = new DigitalInput(RobotMap.TURRET.LIMIT_SWITCH_BACK);
        this.front = new DigitalInput(RobotMap.TURRET.LIMIT_SWITCH_FRONT);

        this.frontSoftLimit = -4000;
        this.backSoftLimit = 4000;
    }

    public void spin(double power){
        //SmartDashboard.putString("Spin", "spinning...");
        //SmartDashboard.putString("Spin", "SpinTurret, power = " + power);
        if(this.getPosition() > this.backSoftLimit){
            power = Math.max(-.2, power);
            //SmartDashboard.putString("Spin", "BSL: " + this.getBackSoftLimit());
        }
        if(this.getPosition() < this.getFrontSoftLimit()){
            power = Math.min(0.2, power);
            //SmartDashboard.putString("Spin", ("FSL: " + this.getFrontSoftLimit() + " FHL: " + this.frontHardLimit));
        }
        if(this.atBackLimit()){
            power = Math.max(0, power);
            //SmartDashboard.putString("Spin", "back limit " + this.getBackSoftLimit());
        }
        if(this.atFrontLimit()){
            power = Math.min(0, power);
            //SmartDashboard.putString("Spin", "front limit " + this.getFrontSoftLimit());
        }
        if(Math.abs(power) < 0.02){
            power = 0;
        }
        this.turret.set(ControlMode.PercentOutput, power);
    }

    public void spinUnsafe(double power){
        this.turret.set(ControlMode.PercentOutput, power);
    }

    public void resetSoftLimits(){
        this.frontSoftLimit = -4000;
        this.backSoftLimit = 4000;
    }

    public void resetEncoder(){
        this.encoder.reset();
    }

    public boolean atFrontLimit(){
        return !this.front.get();
    }

    public boolean atBackLimit(){
        return !this.back.get();
    }

    public void setAllLimits(){
        this.setFrontLimits();
        this.backHardLimit = this.frontHardLimit + RobotMap.TURRET.RANGE_OF_MOTION * RobotMap.TURRET.ENCODER_COUNTS_PER_DEGREE;
        this.backSoftLimit = this.backHardLimit - 300;
    }

    public void setFrontLimits(){
        this.frontSoftLimit = this.getPosition() + 300;
        this.frontHardLimit = this.getPosition();
    }

    public void setBackLimits(){
        this.backSoftLimit = this.getPosition() - 300;
        this.backHardLimit = this.getPosition();
    }

    //returns a percentage in decimal form: between 0 and 100
    public double getPercentPosition(){
        return this.getPosition() / (this.frontHardLimit - this.backHardLimit);
    }

    public double getDegreesPosition(){
        return (this.getPosition() - this.frontHardLimit) / RobotMap.TURRET.ENCODER_COUNTS_PER_DEGREE;
        //return this.getPosition() * RobotMap.TURRET.RANGE_OF_MOTION / (this.backHardLimit - this.frontHardLimit);
    }

    public double getFrontSoftLimit(){
        return this.frontSoftLimit;
    }

    public double getBackSoftLimit(){
        return this.backSoftLimit;
    }

    public double getPosition(){
        return this.encoder.get();
    }

    public double getVelocity(){
        return this.encoder.getRate();
    }

    public void setsetpoint(double setpoint){
        this.setpoint = setpoint;
        this.getController().setSetpoint(setpoint);
    }

    public double getSetpoint(){
        return this.setpoint;
        //return this.getController().getSetpoint();
    }

    public boolean atSetpoint(){
        return Math.abs(this.setpoint - this.getDegreesPosition()) < 1;
        //return this.getController().atSetpoint();
    }

    public boolean atCameraSetpoint(){
        if(!SmartDashboard.getBoolean("Enable AimBot", false)){
            return true;
        }
        return Math.abs(SmartDashboard.getNumber("target-x", -1)) < .02;
    }

    public double getOffset(){
        return this.getPosition() - this.getController().getSetpoint();
    }

    /**
     * Returns the Singleton instance of this Turret. This static method
     * should be used -- {@code Turret.getInstance();} -- by external
     * classes, rather than the constructor to get the instance of this class.
     */
    public static Turret getInstance() {
        return INSTANCE;
    }

    @Override
    public void useOutput(double output, double setpoint) {
        this.spin(output);
        return;
    }

    @Override
    public double getMeasurement() {
        return this.getPosition();
    }
}