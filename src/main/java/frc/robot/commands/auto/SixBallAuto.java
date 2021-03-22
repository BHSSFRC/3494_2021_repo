package frc.robot.commands.auto;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Shoot;
import frc.robot.commands.drive.DistanceDrive;
import frc.robot.commands.teleop.AimAndShoot;
import frc.robot.subsystems.Intake;

/**
 * shoot 3 balls, drive forward to pickup 3 more balls,
 * return to starting position, shoot 3 more balls
 */
public class SixBallAuto extends SequentialCommandGroup {
    public SixBallAuto() {
        super(
                new InstantCommand(() -> Intake.getInstance().setDeployed(true)),
                new AimAndShoot(),
                new ParallelDeadlineGroup(
                        new DistanceDrive(-80),
                        new RunIntakeAuto()
                ),
                new RunIntakeAuto().withTimeout(0.5),
                new ParallelDeadlineGroup(
                        new DistanceDrive(80),
                        new Shoot(SmartDashboard.getNumber("Shooter/Shooter RPM Target", 0))),
                new AimAndShoot()
        );
    }
}