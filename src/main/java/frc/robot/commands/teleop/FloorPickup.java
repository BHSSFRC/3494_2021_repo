package frc.robot.commands.teleop;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.auto.RunIntakeAuto;

//Intake balls from the floor and stage them to enter the shooter without actually shooting them
public class FloorPickup extends ParallelCommandGroup {
    public FloorPickup() {
        super(
                new IntakingRoutine(),
                new RunIntakeAuto()
        );
    }
}