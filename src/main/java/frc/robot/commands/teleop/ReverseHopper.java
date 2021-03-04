package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Hopper;

//Run the Hopper in reverse to prevent jamming
public class ReverseHopper extends CommandBase {

    public ReverseHopper() {
        addRequirements(Hopper.getInstance());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Hopper.getInstance().spin(-RobotConfig.MAGAZINE.HOPPER_DEFAULT_POWER);
    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Hopper.getInstance().stop();
    }
}
