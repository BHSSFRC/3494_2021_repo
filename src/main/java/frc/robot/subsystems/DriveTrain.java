package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class DriveTrain extends SubsystemBase {

// Any variables/fields used in the constructor must appear before the "INSTANCE" variable
// so that they are initialized before the constructor is called.

    private TalonFX leftMaster;
    private TalonFX leftSlave;
    private TalonFX rightMaster;
    private TalonFX rightSlave;

    private Relay led;

    private final static DriveTrain INSTANCE = new DriveTrain();

    private DriveTrain() {
        StatorCurrentLimitConfiguration currentLimitConfiguration = new StatorCurrentLimitConfiguration(true, 40, 45, 1.0);
        double rampRate = 0.001;
        this.leftMaster = new TalonFX(RobotMap.DRIVETRAIN.LEFT_MASTER);
        this.leftMaster.setNeutralMode(NeutralMode.Brake);
        this.leftMaster.configOpenloopRamp(rampRate);
        this.leftMaster.configStatorCurrentLimit(currentLimitConfiguration);
        this.leftSlave = new TalonFX(RobotMap.DRIVETRAIN.LEFT_SLAVE);
        this.leftSlave.setNeutralMode(NeutralMode.Brake);
        //this.leftSlave.configStatorCurrentLimit(currentLimitConfiguration);
        this.leftSlave.configOpenloopRamp(rampRate);

        this.rightMaster = new TalonFX(RobotMap.DRIVETRAIN.RIGHT_MASTER);
        this.rightMaster.configStatorCurrentLimit(currentLimitConfiguration);
        this.rightMaster.configOpenloopRamp(rampRate);
        this.rightMaster.setNeutralMode(NeutralMode.Brake);
        this.rightSlave = new TalonFX(RobotMap.DRIVETRAIN.RIGHT_SLAVE);
        this.leftSlave.configStatorCurrentLimit(currentLimitConfiguration);
        //this.rightSlave.setNeutralMode(NeutralMode.Brake);
        this.rightSlave.configOpenloopRamp(rampRate);
        this.rightMaster.setInverted(true);
        this.rightSlave.setInverted(true);

        this.leftSlave.follow(this.leftMaster);
        this.rightSlave.follow(this.rightMaster);

        this.led = new Relay(RobotMap.SENSORS.SPIKE);
    }

    public void getRPM(){

    }

    public void toggleLED(){
        if(this.led.get() == Relay.Value.kOn){
            this.led.set(Relay.Value.kOff);
        }else{
            this.led.set(Relay.Value.kOn);
        }
    }

    public void tankDrive(double leftPower, double rightPower){
        this.leftMaster.set(ControlMode.PercentOutput, leftPower);
        this.rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, 0.02);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, 0.02);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squaredInputs) {
            xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
            zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }

        double leftMotorOutput;
        double rightMotorOutput;

        leftMotorOutput = xSpeed - zRotation;
        rightMotorOutput = xSpeed + zRotation;

        double[] stickSpeeds = normalize(new double[]{leftMotorOutput, rightMotorOutput});
        this.tankDrive(stickSpeeds[0], stickSpeeds[1]);
    }
    
    private double[] normalize(double[] motorSpeeds) {
        double max = Math.abs(motorSpeeds[0]);
        boolean normFlag = max > 1;

        for (int i = 1; i < motorSpeeds.length; i++) {
            if (Math.abs(motorSpeeds[i]) > max) {
                max = Math.abs(motorSpeeds[i]);
                normFlag = max > 1;
            }
        }

        if (normFlag) {
            for (int i = 0; i < motorSpeeds.length; i++) {
                motorSpeeds[i] /= max;
            }
        }
        return motorSpeeds;
    }

    private static double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    private static double limit(double value) {
        if (value > 1.0) {
            return 1.0;
        }
        if (value < -1.0) {
            return -1.0;
        }
        return value;
    }

    /**public boolean aboveMaxTemp(){
        if (this.rightMaster.getTemperature() > RobotMap.DRIVETRAIN.MAX_TEMP) {
            return true;
        } else if (this.rightSlave.getTemperature() > RobotMap.DRIVETRAIN.MAX_TEMP || this.leftMaster.getTemperature() > RobotMap.DRIVETRAIN.MAX_TEMP || this.leftSlave.getTemperature() > RobotMap.DRIVETRAIN.MAX_TEMP) {
            return true;
        } else {
            return false;
        }
    }*/

    public double getLeftEncoderPosition(){
        return (this.leftMaster.getSelectedSensorPosition() + this.leftSlave.getSelectedSensorPosition()) / 2;
    }

    public double getRightEncoderPosition(){
        return (this.rightMaster.getSelectedSensorPosition() + this.rightSlave.getSelectedSensorPosition()) / 2;
    }
    public double getEncoderPosition(){
        return (this.getLeftEncoderPosition() + this.getRightEncoderPosition()) / 2;
    }

    public void stop(){
        this.tankDrive(0,0);
    }

    public static DriveTrain getInstance() {
        return INSTANCE;
    }

}

