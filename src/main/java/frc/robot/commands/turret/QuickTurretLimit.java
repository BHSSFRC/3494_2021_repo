package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Turret;

/**
 * Spin turret to farthest front position, set hard and soft limits,
 * then rotate turret to face forward
 */
public class QuickTurretLimit extends CommandBase {

    public QuickTurretLimit() {
        addRequirements(Turret.getInstance());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        Turret.getInstance().spin(RobotConfig.TURRET.DEFAULT_SPIN_POWER);
    }

    @Override
    public boolean isFinished() {
        return Turret.getInstance().atFrontLimit();
    }

    @Override
    public void end(boolean interrupted) {
        Turret.getInstance().spin(0);
        Turret.getInstance().resetEncoder();
        Turret.getInstance().setAllLimits();
        CommandScheduler.getInstance().schedule(new SpinToPosition(130));
    }
}