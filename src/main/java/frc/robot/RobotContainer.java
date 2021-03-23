/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.auto.FireTurnDrive;
import frc.robot.subsystems.DriveTrain;
import frc.robot.commands.drive.Drive;
import frc.robot.util.TrajectoryBuilder;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;


/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer
{
    // Create SmartDashboard chooser for autonomous routines
    private SendableChooser<Command> m_chooser = new SendableChooser<>();

    NetworkTable table = NetworkTableInstance.getDefault().getTable("troubleshooting");
    NetworkTableEntry leftReference = table.getEntry("left_reference");
    NetworkTableEntry leftMeasurement = table.getEntry("left_measurement");
    NetworkTableEntry rightReference = table.getEntry("right_reference");
    NetworkTableEntry rightMeasurement = table.getEntry("right_measurement");

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer()
    {
        // Configure the button bindings
        configureButtonBindings();
    }

    // The robot's subsystems and commands are defined here...
    private final DriveTrain m_drivetrain = new DriveTrain();
    private final TrajectoryBuilder m_trajectoryBuilder = new TrajectoryBuilder(m_drivetrain);

    public Command getTeleopCommand()
    {
        return null;
    }

    /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
    private void configureButtonBindings() {
        // Default command is arcade drive. This will run unless another command
        // is scheduled over it.
        m_drivetrain.setDefaultCommand(new Drive(m_drivetrain));
    
        //Add all trajectory options to SmartDashboard choosers
        System.out.println("Start to initialize all trajectories");
        m_chooser.setDefaultOption("drive", m_drivetrain.getDefaultCommand());
        m_chooser.addOption("FireTurnDrive Forward",new FireTurnDrive(m_drivetrain, 40).withTimeout(15));
        m_chooser.addOption("FireTurnDrive Backward",new FireTurnDrive(m_drivetrain, -40).withTimeout(15));
        m_chooser = m_trajectoryBuilder.addAllTrajectoryCommandsToChooser(m_chooser);
        
        SmartDashboard.putData(m_chooser);
      }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        return m_chooser.getSelected();
    }

public double getGyroAngleZ() {
	return m_drivetrain.getGyroAngleZ();
}

public Pose2d getPose() {
  return m_drivetrain.getPoseRadians();
}

public DriveTrain getDrivetrainInstance() {
  return this.m_drivetrain;
}

}
