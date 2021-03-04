package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Hopper;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//spin hopper at constant speed
public class RunHopper extends CommandBase {

    public RunHopper() {
        addRequirements(Hopper.getInstance());
    }

    @Override
    public void execute() {
        Hopper.getInstance().spin(RobotConfig.MAGAZINE.HOPPER_DEFAULT_POWER);
    }

    @Override
    public void end(boolean interrupted) {
        Hopper.getInstance().stop();
    }
}
