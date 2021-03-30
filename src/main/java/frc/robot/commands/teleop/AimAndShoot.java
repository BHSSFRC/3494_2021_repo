package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import frc.robot.Robot;
import frc.robot.RobotConfig;
import frc.robot.commands.DoNothing;
import frc.robot.commands.Shoot;
import frc.robot.commands.turret.AimBot;
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
    public static Shooter.Settings settings = new Shooter.Settings(SmartDashboard.getNumber("Shooter/Shooter RPM Target", 100), Shooter.Position.fromNumber((int) SmartDashboard.getNumber("Shooter/Shooter Hood", 1)));

    public AimAndShoot() {
        //Preshooter should only start once target RPM is reached
        super(
            new InstantCommand(() -> System.out.println("Aim and Shoot--RPM: " + settings.getRPM())),
            new InstantCommand(() -> Shooter.getInstance().setPosition(settings.getPosition())),
            new StopHopperMagazine(),
            new ParallelCommandGroup(
                new ConditionalCommand(new DoNothing(), new IntakingRoutine(), () -> Robot.getLinebreakBottom().lineBroken() && Robot.getLinebreakTop().lineBroken()),
                //new AimBot(),
                new Shoot(settings.getRPM())).withInterrupt(() -> Shooter.getInstance().atTargetSpeed(settings.getRPM()) && Turret.getInstance().atCameraSetpoint()
            ),
            new InstantCommand(() -> System.out.println("Turn on Hopper Magazine " + settings.getRPM())),
            new ParallelDeadlineGroup(
                new CountBallsShot(RobotConfig.SHOOTER.BALLS_NEED_SHOT),
                new Shoot(settings.getRPM()),
                new RunHMWhileShooting(settings.getRPM())
            ).withTimeout(10)
        );
    }

    @Override
    public void end(boolean interrupted) {
        Shooter.getInstance().setPosition(Shooter.Position.ONE);
        Hopper.getInstance().stop();
        Magazine.getInstance().stop();
        Shooter.getInstance().stop();
    }
}