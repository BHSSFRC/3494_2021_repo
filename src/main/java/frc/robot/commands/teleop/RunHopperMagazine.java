package frc.robot.commands.teleop;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.RunHopper;
import frc.robot.commands.RunMagazine;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Magazine;

//run all hopper and magazine motors
public class RunHopperMagazine extends ParallelCommandGroup {
    public RunHopperMagazine() {
        super(new RunHopper(),
                new RunMagazine(true, true));
    }

    @Override
    public void end(boolean interrupted) {
        Hopper.getInstance().stop();
        Magazine.getInstance().stop();
    }
}
