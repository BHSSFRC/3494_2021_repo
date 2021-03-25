package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.auto.FireTurnDrive;
import frc.robot.subsystems.DriveTrain;
import frc.robot.commands.drive.Drive;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import java.nio.file.Path;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.BiConsumer;
import edu.wpi.first.wpilibj.Filesystem;
import java.io.IOException;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;

public class TrajectoryBuilder {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("troubleshooting");
    NetworkTableEntry leftReference = table.getEntry("left_reference");
    NetworkTableEntry leftMeasurement = table.getEntry("left_measurement");
    NetworkTableEntry rightReference = table.getEntry("right_reference");
    NetworkTableEntry rightMeasurement = table.getEntry("right_measurement");
    private DriveTrain m_drivetrain;

    public TrajectoryBuilder(DriveTrain drivetrain) {
      m_drivetrain = drivetrain;
    }

    public Command getAutoRamseteCommand(Trajectory trajectory){
        double trajectoryTime = trajectory.getTotalTimeSeconds();
        System.out.print(trajectoryTime);
        System.out.println(" seconds to complete trajectory");
        System.out.println("Starting pose:");
        System.out.println(trajectory.getInitialPose());
        if (trajectoryTime != 0){
          System.out.println("Ending pose:");
          System.out.println(trajectory.sample(trajectoryTime-.01));
        }
      
        PIDController leftController = new PIDController(DriveConstants.kPDriveVel, 0, 0);
        PIDController rightController = new PIDController(DriveConstants.kPDriveVel, 0, 0);
        
        Supplier<DifferentialDriveWheelSpeeds> getWheelSpeeds = m_drivetrain::getWheelSpeeds;
        BiConsumer<Double, Double> outputVolts = (Double leftVolts, Double rightVolts) -> {
          m_drivetrain.tankDriveVolts(leftVolts, rightVolts);
      
          leftMeasurement.setNumber(m_drivetrain.getWheelSpeeds().leftMetersPerSecond);
          leftReference.setNumber(leftController.getSetpoint());
      
          rightMeasurement.setNumber(m_drivetrain.getWheelSpeeds().rightMetersPerSecond);
          rightReference.setNumber(rightController.getSetpoint());
          System.out.print(leftVolts);
          System.out.print("   ");
          System.out.print(rightVolts);
          System.out.println(" Run AutoRamseteCommand()");
        };
      
        RamseteCommand ramseteCommand = new RamseteCommand(
          trajectory,
          m_drivetrain::getPoseRadians,
          new RamseteController(AutoConstants.kRamseteB, AutoConstants.kRamseteZeta),
          new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                    DriveConstants.kvVoltSecondsPerMeter,
                                    DriveConstants.kaVoltSecondsSquaredPerMeter),
          DriveConstants.kDriveKinematics,
          getWheelSpeeds,
          leftController,
          rightController,
          // RamseteCommand passes volts to the callback
          outputVolts,
          m_drivetrain//
        );
      
        // Reset odometry to the starting pose of the trajectory.
        m_drivetrain.resetOdometry(trajectory.getInitialPose());
      
      
        SequentialCommandGroup resetOdometry = new InstantCommand(() -> m_drivetrain.setGyroAngleZ(trajectory.getInitialPose().getRotation().getDegrees())).andThen(() -> m_drivetrain.resetOdometry(trajectory.getInitialPose())).andThen((() -> System.out.println("Reset odometry")));
        
        // Run path following command, then stop at the end.
        return resetOdometry.andThen(ramseteCommand).andThen(() -> m_drivetrain.tankDriveVolts(0, 0)).andThen(() -> System.out.println(trajectory.sample(trajectoryTime-.01)));
      }
      
      public Trajectory getTrajectoryFromPathweaver(String trajectoryName){
        String trajectoryJSON = "paths/" + trajectoryName + ".wpilib.json";
        Trajectory trajectory = new Trajectory();
        try {
          Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
          trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
          System.out.println("Starting Path");
          DriverStation.reportWarning("Start Trajectory Path "+ trajectoryName,false);
        } catch (IOException ex) {
          DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
        }
      
        
      
        return trajectory;
      }

      /**
       * 
       * @param chooser contains a list of commands for auto
       * Generate Trajectory Following commands for all possible PathWeaver trajectories
       * Add each trajectory to the chooser
       * @return chooser with the added trajectories
       */
      public SendableChooser<Command> addAllTrajectoryCommandsToChooser(SendableChooser<Command> chooser) {
        for (int i = 0; i < Constants.TrajectoryConstants.autonav_path_names.length; i++) {
          String trajectoryName = Constants.TrajectoryConstants.autonav_path_names[i];
          Trajectory trajectory = getTrajectoryFromPathweaver(trajectoryName);
          Command trajectoryCommand = getAutoRamseteCommand(trajectory);
          chooser.addOption(trajectoryName, trajectoryCommand);
        }
        return chooser;
      }
    
}
