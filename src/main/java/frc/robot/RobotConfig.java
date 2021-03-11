/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 * <p>
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */

//this class store software variables such as PID constants that change from time to time
public final class RobotConfig
{
    public static class FIRE_TURN_DRIVE {
        public static final double
                SHOOTER_POWER = 0.47,
                TURN_AMOUNT_DEGREES = 80,
                DRIVE_DISTANCE_INCHES = 40;
        public static final double
            SHOOT_TIMEOUT = 2,
            HOPPER_MAGAZINE_TIMEOUT = 5,
            TURN_DEGREES_TIMEOUT = 4,
            DISTANCE_DRIVE_TIMEOUT = 4;
    }
    public static class DRIVE {
        public static final double POWER_CURVE_EXPONENT = 2.8;
        public static final String[] DRIVE_SCHEMES = { "Bork", "Emerson", "Hazel", "Katrina" };
    }

    public static class DRIVE_STRAIGHT {
        public static final int kP = 1;
        public static final int kI = 0;
        public static final int kD = 0;

        public static final double kP_DUMB = 5 / 360;

        public static final double TURN_SPEED = 1.0;
        public static final double ENCODER_TICKS_PER_INCH = 793.7;

        public static final double AUTO_LINE_INCHES = 40;
    }

    public static class TURRET {
        public static double DEFAULT_SPIN_POWER = 0.3;
    }

    public static class MAGAZINE {
        public static double FRONT_MOTOR_DEFAULT_POWER = 0.4;//SmartDashboard.getNumber("Front Magazine Power", 0.7);
        public static double BOTTOM_MOTOR_DEFAULT_POWER = 0.5;//SmartDashboard.getNumber("Bottom Magazine Power", 0.7);
        public static double HOPPER_DEFAULT_POWER = SmartDashboard.getNumber("Hopper Power", 0.8);
        public static double INTAKE_DEFAULT_POWER = 0.6;
        public static double TIME_FOR_REVERSE = 0.1;
    }

    public static class SHOOTER {
        public static double PRESHOOTER_POWER = 0.3;
        public static double SHOOTER_MAX_POWER = .8;
        public static double PRESHOOTER_DELAY = 2;
        public final double 
            kP = 0.0,
            kI = 0.0,
            kD = 0.0,
            kF = 0.0;
    }
}