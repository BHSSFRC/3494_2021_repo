/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.auto.FireTurnDrive;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer
{
    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer()
    {
    }

    public Command getTeleopCommand()
    {
        return null;
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        if(SmartDashboard.getBoolean("Auto Forward?", true)){
            return new FireTurnDrive(40).withTimeout(15).andThen(new InstantCommand(() -> System.out.println("Done")));
        }else{
            return new FireTurnDrive(-40).withTimeout(15).andThen(new InstantCommand(() -> System.out.println("Done")));
        }
    }
}
