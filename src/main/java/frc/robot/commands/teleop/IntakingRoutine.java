package frc.robot.commands.teleop;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.RobotConfig;
import frc.robot.commands.RunHopper;
import frc.robot.commands.RunMagazine;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Magazine;

/**
 * assuming balls are already inside robot, stage the balls so that they
 * are ready to enter the shooter but not in the shooter
 */
public class IntakingRoutine extends SequentialCommandGroup {
    public IntakingRoutine() {
        SmartDashboard.putBoolean("Magazine/Intaking Routine", true);
        System.out.println("Start Intaking Routine");
        addCommands(
                new ParallelDeadlineGroup(
                        new SequentialCommandGroup(
                                new RunMagazine(true, false, true, false).withInterrupt(() -> Robot.getLinebreakBottom().lineBroken()),
                                new InstantCommand(() -> System.out.println("First Linebreak Sensor Tripped")),
                                new RunMagazine(true, false, true, false).withInterrupt(() -> Robot.getLinebreakTop().lineBroken()),
                                new InstantCommand(() -> System.out.println("Second Linebreak Sensor Tripped")),
                                new RunMagazine(true, false, false, false).withInterrupt(() -> Robot.getLinebreakBottom().lineBroken()),
                                new InstantCommand(() -> System.out.println("First Linebreak Sensor Tripped"))
                                //new RunMagazine(false, false, true, true).withTimeout(RobotConfig.MAGAZINE.TIME_FOR_REVERSE),
                                //new InstantCommand(() -> System.out.println(RobotConfig.MAGAZINE.TIME_FOR_REVERSE + " passed, Finish."))
                        ),
                        new RunHopper(true, false)
                ).withTimeout(30),
                new InstantCommand(() -> Hopper.getInstance().stop()),
                new InstantCommand(() -> Magazine.getInstance().stop()),
                new InstantCommand(() -> System.out.println("Intaking Routine Finished"))
        );
        SmartDashboard.putBoolean("Magazine/Intaking Routine", false);
    }
}