package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;

/**Goal: feed balls through hopper and magazine into the shooter while
 * only actually launching the balls when the shooter is at full RPM
 *
 * run hopper and magazine motors to continue moving balls toward launch position if:
 * there is not already a ball staged to enter the shooter or
 * the shooter is at target RPM and thus ready to receive a ball to be shot
 * if there is a ball staged in the magazine and ready to be shot but
 * the shooter isn't ready to launch balls(not at RPM), then stop the hopper and
 * magazine motors to avoid launching a ball at too low an RPM
 */

public class RunHMWhileShooting extends CommandBase {
    private double targetRPM;
    public RunHMWhileShooting(double targetRPM) {
        addRequirements(Hopper.getInstance(), Magazine.getInstance());
        this.targetRPM = targetRPM;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        if(!Robot.getLinebreakTop().lineBroken() || Shooter.getInstance().atTargetSpeed(this.targetRPM)){
            Hopper.getInstance().spin(RobotConfig.MAGAZINE.HOPPER_DEFAULT_POWER);
            Magazine.getInstance().run(true, true);
        }else{
            Hopper.getInstance().stop();
            Magazine.getInstance().stop();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Hopper.getInstance().stop();
        Magazine.getInstance().stop();
    }
}
