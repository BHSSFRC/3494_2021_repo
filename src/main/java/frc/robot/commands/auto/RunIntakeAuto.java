package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Intake;

//Extend and run intake
public class RunIntakeAuto extends CommandBase {

    public RunIntakeAuto() {
        addRequirements(Intake.getInstance());
    }

    @Override
    public void initialize() {
        Intake.getInstance().setDeployed(true);
    }

    @Override
    public void execute() {
        Intake.getInstance().runIntake((RobotConfig.MAGAZINE.INTAKE_DEFAULT_POWER));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Intake.getInstance().stop();
        Intake.getInstance().setDeployed(false);
    }
}
