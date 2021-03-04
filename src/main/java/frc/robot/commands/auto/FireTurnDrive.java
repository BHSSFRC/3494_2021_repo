package frc.robot.commands.auto;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotConfig;
import frc.robot.commands.Shoot;
import frc.robot.commands.drive.DistanceDrive;
import frc.robot.commands.teleop.RunHopperMagazine;

//Shoot balls and drive forward
//currently robot does not turn at any point during this command
public class FireTurnDrive extends SequentialCommandGroup {
    public FireTurnDrive(double distance) {
        super(
                new Shoot(RobotConfig.FIRE_TURN_DRIVE.SHOOTER_POWER).withTimeout(RobotConfig.FIRE_TURN_DRIVE.SHOOT_TIMEOUT),
                new ParallelCommandGroup(
                        new Shoot(RobotConfig.FIRE_TURN_DRIVE.SHOOTER_POWER),
                        new RunHopperMagazine()).withTimeout(RobotConfig.FIRE_TURN_DRIVE.HOPPER_MAGAZINE_TIMEOUT),
                //new TurnDegrees(RobotConfig.FIRE_TURN_DRIVE.TURN_AMOUNT_DEGREES)
                //        .withTimeout(RobotConfig.FIRE_TURN_DRIVE.TURN_DEGREES_TIMEOUT),
                new DistanceDrive(distance).withTimeout(RobotConfig.FIRE_TURN_DRIVE.DISTANCE_DRIVE_TIMEOUT)
        );
    }
}