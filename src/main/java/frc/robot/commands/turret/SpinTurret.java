package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.subsystems.Turret;

//Spin turret with power according to xbox rightX input
public class SpinTurret extends CommandBase {

    public SpinTurret() {
        addRequirements(Turret.getInstance());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double power = OI.getINSTANCE().getXboxRightX() * 0.4;
        Turret.getInstance().spin(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Turret.getInstance().spin(0);
    }
}