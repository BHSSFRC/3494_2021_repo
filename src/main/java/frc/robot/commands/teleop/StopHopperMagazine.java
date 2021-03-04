package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Magazine;


public class StopHopperMagazine extends CommandBase {

    //instant command stops all hopper and magazine motors
    public StopHopperMagazine() {
        addRequirements(Hopper.getInstance(), Magazine.getInstance());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        Hopper.getInstance().stop();
        Magazine.getInstance().stop();
    }
}
