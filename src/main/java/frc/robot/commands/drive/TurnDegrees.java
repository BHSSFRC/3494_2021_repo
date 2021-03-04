package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.subsystems.DriveTrain;

public class TurnDegrees extends CommandBase {
    private double setpoint;
    private double initialYaw;
    private double turnDegrees;
    private double currentDegrees;
    private double delta;

    //default constructor turns 0 degrees
    public TurnDegrees() {
        // If any subsystems are needed, you will need to pass them into the requires() method
        addRequirements(DriveTrain.getInstance());
        this.turnDegrees = 90.0;
    }

    /**
     * @param degrees should be between -180 and 180, the number of degrees the robot should rotate
     */
    public TurnDegrees(double degrees) {
        // If any subsystems are needed, you will need to pass them into the requires() method
        addRequirements(DriveTrain.getInstance());
        this.turnDegrees = degrees;
    }

    @Override
    public void initialize() {
        //IMU.getInstance().reset();
        //IMU should reset yaw to 0, now set initial yaw to 180
        //System.out.println("Yaw initial: " + IMU.getInstance().getYaw());
        //this.initialYaw = IMU.getInstance().getYaw() + 180;
        this.setpoint = this.initialYaw + this.turnDegrees;
        this.delta = this.setpoint;
    }

    @Override
    public void execute() {
        //this.currentDegrees = IMU.getInstance().getYaw() + 180;
        this.delta = (this.currentDegrees - this.setpoint) % 360;
        //SmartDashboard.putNumber("DriveTurn Offset", delta);

        double output = this.delta / 360;
        //should be between -1 and 1
        if(output < 0){
            output = Math.min(output, -.2);
        }else{
            output = Math.max(output, .2);
        }
        output *= RobotConfig.DRIVE_STRAIGHT.TURN_SPEED;

        DriveTrain.getInstance().tankDrive(output, -output);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(this.delta) < 5;
    }

    @Override
    public void end(boolean interrupted){
        DriveTrain.getInstance().stop();
    }
}
