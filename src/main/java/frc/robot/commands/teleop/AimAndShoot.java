package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Shoot;
import frc.robot.subsystems.Hopper;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

/**
 * Set the hood to position two, Aim the shooter, Start the shooter motor
 * once the shooter is aimed correctly and spinning at target RPM,
 * run shooter until the given number of balls have all launched
 */
public class AimAndShoot extends SequentialCommandGroup {
    public AimAndShoot(double targetRPM, int ballsToShoot) {
        //Preshooter should only start once target RPM is reached
        super(
                new InstantCommand(() -> System.out.println("Aim and Shoot--RPM: " + targetRPM)),
                new InstantCommand(() -> Shooter.getInstance().setPosition(Shooter.Position.TWO)),
                new StopHopperMagazine(),
                new ParallelCommandGroup(
                        //new AimBot(),
                        new Shoot(targetRPM, true)).withInterrupt(() -> Shooter.getInstance().atTargetSpeed(targetRPM) &&
                                                            Turret.getInstance().atCameraSetpoint()),
                new InstantCommand(() -> System.out.println("Turn on Hopper Magazine " + targetRPM)),
                new ParallelDeadlineGroup(
                        new CountBallsShot(ballsToShoot),
                        new Shoot(targetRPM, true),
                        new RunHMWhileShooting(targetRPM)
                ).withTimeout(10)
        );
    }

    //runs command with the target RPM specified by the SmartDashboard
    public AimAndShoot(int ballsToShoot){
        this(SmartDashboard.getNumber("Shooter RPM Target", 0), ballsToShoot);
    }

    @Override
    public void end(boolean interrupted) {
        Shooter.getInstance().setPosition(Shooter.Position.ONE);
        Hopper.getInstance().stop();
        Magazine.getInstance().stop();
    }
}