package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.OI;
import frc.robot.subsystems.Hopper;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//spin hopper at constant speed
public class RunHopper extends CommandBase {
    boolean run;
    boolean reverse;

    public RunHopper() {
        this(false, false);
    }

    public RunHopper(boolean run, boolean reverse) {
        addRequirements(Hopper.getInstance());

        this.run = run;
        this.reverse = reverse;
    }

    @Override
    public void initialize() {
        Hopper.getInstance().spin(0);
    }

    @Override
    public void execute() {
        double power = this.run ? RobotConfig.MAGAZINE.HOPPER_DEFAULT_POWER : 0;
        
        if (this.reverse) power = -power;

        Hopper.getInstance().spin(power);
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
