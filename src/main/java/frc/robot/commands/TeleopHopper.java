package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.OI;
import frc.robot.subsystems.Hopper;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//spin hopper on xbox input
public class TeleopHopper extends CommandBase {
    public TeleopHopper() {
        addRequirements(Hopper.getInstance());
    }

    @Override
    public void initialize() {
        Hopper.getInstance().spin(0);
    }

    @Override
    public void execute() {
        if (OI.getINSTANCE().getXboxRightBumper()) Hopper.getInstance().spin(RobotConfig.MAGAZINE.HOPPER_DEFAULT_POWER);
        else Hopper.getInstance().spin(0);
    }

    @Override
    public void end(boolean interrupted) {
        Hopper.getInstance().stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
