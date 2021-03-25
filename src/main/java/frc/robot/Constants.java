// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DriveConstants {
      public static final double ksVolts = .682;//0.207;
        public static final double kvVoltSecondsPerMeter = 2.14;//3.24;//8.0;//5.04;//3.24 is the actual value found by the characterization tool
        public static final double kaVoltSecondsSquaredPerMeter = 0.542;//0.318;
        
        //TODO: test diff values of kP
        public static final double kPDriveVel = 11.7;//0.0//11.7
        
        public static final double kTrackwidthMeters = 0.4;
        public static final DifferentialDriveKinematics kDriveKinematics =
            new DifferentialDriveKinematics(kTrackwidthMeters);

        public static final double INCHES_TO_METERS = 1.0 / 39.37;
        public static final double METERS_TO_INCHES = 39.37;
        public static final double ENCODER_TICKS_PER_INCH = 0.006012462923579616;

        public static final double RPM_TO_METERS_PER_SECOND = 1 / (26.75*2*60);
        public static final double RPM_TO_METERS = 1 / (26.75);// 1 / (25.5*4);
      }
    
      public static final class AutoConstants {
        public static final double kMaxSpeedMetersPerSecond = 2.0;//0.8;//3.0;
        public static final double kMaxAccelerationMetersPerSecondSquared = 2.0;//0.8;//3.0;
    
        // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
      }

      public static final class TrajectoryConstants {
        public static final String[] autonav_path_names = {"LongCurvy","GSB_Red","GSB_Blue","Slalom","BarrelRacing","Bounce","GSA_Red","GSA_Blue","SmoothCurve","SmoothCurveLong","FigureEight"};
      }
}
