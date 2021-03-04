package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.subsystems.DriveTrain;


public class DistanceDrive extends CommandBase {
    private double dist;
    private double distEncoderInitial;
    private double distEncoderGoal;
    private double distEncoderTraveled;
    private double speed;

    /**
     * @param distance- distance in inches for the robot to drive
     *                can be positive or negative to represent driving backwards or forward
     */
    public DistanceDrive(double distance) {
        addRequirements(DriveTrain.getInstance());
        this.dist = distance;
    }

    @Override
    public void initialize() {
        this.distEncoderInitial = DriveTrain.getInstance().getEncoderPosition();
        this.distEncoderGoal = Math.abs(this.dist) * RobotConfig.DRIVE_STRAIGHT.ENCODER_TICKS_PER_INCH;
        if(this.dist > 0){
            this.speed = 0.3;
        }else{
            this.speed = -.3;
        }
        this.distEncoderTraveled = 0;
    }

    @Override
    public void execute() {
        this.distEncoderTraveled = Math.abs(DriveTrain.getInstance().getEncoderPosition() - this.distEncoderInitial);

        //drive slower once within 12 inches of the target
        if((distEncoderTraveled > (Math.abs(this.distEncoderGoal) - RobotConfig.DRIVE_STRAIGHT.ENCODER_TICKS_PER_INCH * 12)) &&
        Math.abs(this.speed) == 0.3){
            this.speed /= 3;
        }
        DriveTrain.getInstance().tankDrive(this.speed, this.speed);
    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return (this.distEncoderTraveled > this.distEncoderGoal);
    }

    @Override
    public void end(boolean interrupted) {
        if(interrupted){
            System.out.println("Distance Drive interrupted");
        }
        DriveTrain.getInstance().stop();
    }
}
