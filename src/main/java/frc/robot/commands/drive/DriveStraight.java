package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.DriveTrain;
import frc.robot.util.QuadTimer;
import frc.robot.util.SynchronousPIDF;

public class DriveStraight extends CommandBase {
    private QuadTimer timer;
    private SynchronousPIDF pidController;
    private double power;
    private double initialYaw;
    private boolean steadyPower;

    //drive robot straight with power determined by xbox right trigger
    public DriveStraight() {
        //super(new PIDController());
        //super(1.0,1.0,1.0);
        // If any subsystems are needed, you will need to pass them into the requires() method
        addRequirements(DriveTrain.getInstance());
        this.timer = new QuadTimer();
        this.initializePID();
        this.power = 0;
        this.steadyPower = false;
    }

    //drive robot straight with constant speed
    public DriveStraight(double power) {
        //super(new PIDController());
        //super(1.0,1.0,1.0);
        // If any subsystems are needed, you will need to pass them into the requires() method
        addRequirements(DriveTrain.getInstance());
        this.timer = new QuadTimer();
        this.initializePID();
        this.power = power;
        this.steadyPower = true;
    }

    private void initializePID(){
        //takes same PID constants as DriveAntitipPD does. if necessary will add new set of constants
        double kp;
        double ki;
        double kd;
        //if (SmartDashboard.containsKey("Display Drivetrain data?")) {
        if(true){
            kp = SmartDashboard.getNumber("Tuning/PID P", 0.9);
            ki = SmartDashboard.getNumber("Tuning/PID I", 0.0);
            kd = SmartDashboard.getNumber("Tuning/PID D", 0.0);
        }else{
            kp = RobotConfig.DRIVE_STRAIGHT.kP;
            ki = RobotConfig.DRIVE_STRAIGHT.kI;
            kd = RobotConfig.DRIVE_STRAIGHT.kD;
        }

        this.pidController = new SynchronousPIDF(kp, ki, kd);
        pidController.setPID(kp, ki, kd);
        pidController.setInputRange(0, 360);
        pidController.setContinuous(true);
        pidController.setOutputRange(-1, 1);
    }

    @Override
    public void initialize() {
        this.timer.start();
        //this.initialYaw = IMU.getInstance().getYaw();
        pidController.setSetpoint(this.initialYaw);
    }

    @Override
    public void execute() {
        //double input = IMU.getInstance().getYaw();
        double input = 0;
        if(!this.steadyPower){
            powerCurve(OI.getINSTANCE().getPrimaryXboxRightTrigger() - OI.getINSTANCE().getPrimaryXboxLeftTrigger());
        }
        //double output = this.pidController.calculate(input, this.timer.delta());
        double output = (input - this.initialYaw) * RobotConfig.DRIVE_STRAIGHT.kP_DUMB;
        //double output = this.pidController.calculate(this.initialYaw, this.timer.delta());
        //SmartDashboard.putNumber("DriveStraight Offset", output);
        DriveTrain.getInstance().tankDrive(power - output, power + output);
    }

    private static double powerCurve(double x) {
        double curve = Math.pow(Math.sin(Math.PI / 2 * Math.abs(x)), RobotConfig.DRIVE.POWER_CURVE_EXPONENT) * Math.signum(x);
        return Math.copySign(curve, x);
    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        DriveTrain.getInstance().stop();
    }
}
