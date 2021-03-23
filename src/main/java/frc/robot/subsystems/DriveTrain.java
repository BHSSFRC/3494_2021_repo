package frc.robot.subsystems;

//TODO: transfer all RobotContainer trajectory functions to separate class

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.Constants;
import frc.robot.RobotConfig;
//Trajectory-added imports
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import com.revrobotics.CANSparkMax;
import frc.robot.sensors.NavX;
import frc.robot.RobotContainer;

public class DriveTrain extends SubsystemBase {

// Any variables/fields used in the constructor must appear before the "INSTANCE" variable
// so that they are initialized before the constructor is called.

    private WPI_TalonFX leftMaster;
    private WPI_TalonFX leftSlave;
    private WPI_TalonFX rightMaster;
    private WPI_TalonFX rightSlave;

    private Relay led;

    // Set up the NavX
    private final NavX m_gyro = NavX.getInstance();

    // Odometry class for tracking robot pose
    private final DifferentialDriveOdometry m_odometry;
    // Also show a field diagram
    private final Field2d m_field2d = new Field2d();
    NetworkTableEntry m_xEntry = NetworkTableInstance.getDefault().getTable("troubleshooting").getEntry("X");
    NetworkTableEntry m_yEntry = NetworkTableInstance.getDefault().getTable("troubleshooting").getEntry("Y");
    // Set up the differential drive controller
    private final DifferentialDrive m_diffDrive;

    //private final static DriveTrain INSTANCE = new DriveTrain();

    public DriveTrain() {
        StatorCurrentLimitConfiguration currentLimitConfiguration = new StatorCurrentLimitConfiguration(true, 40, 45, 1.0);
        double rampRate = 0.001;
        this.leftMaster = new WPI_TalonFX(RobotMap.DRIVETRAIN.LEFT_MASTER);
        this.leftMaster.setNeutralMode(NeutralMode.Brake);
        this.leftMaster.configOpenloopRamp(rampRate);
        this.leftMaster.configStatorCurrentLimit(currentLimitConfiguration);
        this.leftSlave = new WPI_TalonFX(RobotMap.DRIVETRAIN.LEFT_SLAVE);
        this.leftSlave.setNeutralMode(NeutralMode.Brake);
        //this.leftSlave.configStatorCurrentLimit(currentLimitConfiguration);
        this.leftSlave.configOpenloopRamp(rampRate);

        this.rightMaster = new WPI_TalonFX(RobotMap.DRIVETRAIN.RIGHT_MASTER);
        this.rightMaster.configStatorCurrentLimit(currentLimitConfiguration);
        this.rightMaster.configOpenloopRamp(rampRate);
        this.rightMaster.setNeutralMode(NeutralMode.Brake);
        this.rightSlave = new WPI_TalonFX(RobotMap.DRIVETRAIN.RIGHT_SLAVE);
        this.leftSlave.configStatorCurrentLimit(currentLimitConfiguration);
        //this.rightSlave.setNeutralMode(NeutralMode.Brake);
        this.rightSlave.configOpenloopRamp(rampRate);
        this.rightMaster.setInverted(true);
        this.rightSlave.setInverted(true);

        this.leftSlave.follow(this.leftMaster);
        this.rightSlave.follow(this.rightMaster);

        this.led = new Relay(RobotMap.SENSORS.SPIKE);

        m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d());
        SmartDashboard.putData("field", m_field2d);
        m_diffDrive = new DifferentialDrive(this.leftMaster, this.rightMaster);
        this.resetEncoders();
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

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            } else {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }
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

    public double getLeftEncoderPositionMeters(){
        return (this.leftMaster.getSelectedSensorPosition() + this.leftSlave.getSelectedSensorPosition()) / 2;
    }

    public double getRightEncoderPositionMeters(){
        return (this.rightMaster.getSelectedSensorPosition() + this.rightSlave.getSelectedSensorPosition()) / 2 * RobotConfig.DRIVE_STRAIGHT.ENCODER_TICKS_PER_INCH * Constants.DriveConstants.INCHES_TO_METERS;
    }

    public double getEncoderPosition(){
        return (this.getLeftEncoderPosition() + this.getRightEncoderPosition()) / 2 * RobotConfig.DRIVE_STRAIGHT.ENCODER_TICKS_PER_INCH * Constants.DriveConstants.INCHES_TO_METERS;
    }

    public void stop(){
        this.tankDrive(0,0);
    }

    @Override
    public void periodic() {
        m_odometry.update(m_gyro.getRotation2d(), this.getLeftEncoderPositionMeters(), this.getLeftEncoderPositionMeters());
        Translation2d translation = m_odometry.getPoseMeters().getTranslation();
        m_xEntry.setNumber(translation.getX());
        m_yEntry.setNumber(translation.getY());

        // Also update the Field2D object (so that we can visualize this in sim)
        m_field2d.setRobotPose(getPose());
        SmartDashboard.putNumber("LeftDrivePosMeters", this.getLeftEncoderPositionMeters());
        SmartDashboard.putNumber("RightDrivePosMeters", this.getRightEncoderPositionMeters());
    }

    //This method was removed because the DriveTrain instance is housed in the RobotContainer class
    /**
    public static DriveTrain getInstance() {
        return INSTANCE;
    }*/

    //Helper Functions for Trajectory Following are below
    /**
   * Returns the current wheel speeds of the robot.
   * @return The current wheel speeds
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    double leftVel = this.leftMaster.getSelectedSensorVelocity() * Constants.DriveConstants.RPM_TO_METERS_PER_SECOND;
    double rightVel = this.rightMaster.getSelectedSensorVelocity() * Constants.DriveConstants.RPM_TO_METERS_PER_SECOND;
    return new DifferentialDriveWheelSpeeds(leftVel, rightVel);
}

/**
* Returns the currently estimated pose of the robot.
* @return The pose
*/
public Pose2d getPose() {
    return m_odometry.getPoseMeters();
}

public Pose2d getPoseRadians() {
    return getPose();
}

/**
* Resets the odometry to the specified pose
* @param pose The pose to which to set the odometry
*/
public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, m_gyro.getRotation2d());
}

/**
 * Controls the left and right sides of the drive directly with voltages.
 * @param leftVolts the commanded left output
 * @param rightVolts the commanded right output
 */
public void tankDriveVolts(double leftVolts, double rightVolts) {
    //Any inversions should happen here
    leftVolts = -Math.min(10,Math.max(-10,leftVolts));
    rightVolts = -Math.min(10,Math.max(-10,rightVolts));
    SmartDashboard.putNumber("DriveLeftVolts",leftVolts);
    SmartDashboard.putNumber("DriveRightVolts", rightVolts);
    
    this.leftMaster.set(ControlMode.Current,leftVolts);
    this.rightMaster.set(ControlMode.Current,rightVolts);
    
    m_diffDrive.feed();
}

public void resetEncoders() {
    leftMaster.setSelectedSensorPosition(0);
    rightMaster.setSelectedSensorPosition(0);
}


    /**
   * Current angle of the Romi around the Z-axis.
   *
   * @return The current angle of the Romi in degrees
   */
    public double getGyroAngleZ() {
        return m_gyro.getFusedHeading();
    }

    public void setGyroAngleZ(double degree) {
        m_gyro.setFusedHeading(degree);
        return;
    }

    /** Reset the gyro. */
    public void resetGyro() {
        m_gyro.reset();
    }

}

