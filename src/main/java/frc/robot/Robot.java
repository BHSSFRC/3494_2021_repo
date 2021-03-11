/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.*;
import frc.robot.commands.drive.Drive;
import frc.robot.commands.turret.SpinTurret;
import frc.robot.sensors.Linebreaker;
import frc.robot.subsystems.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * methods corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;

    private RobotContainer robotContainer;

    private static Linebreaker bottom;
    private static Linebreaker top;
    private NetworkTable table;

    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();

        bottom = new Linebreaker(RobotMap.SENSORS.LINEBREAK_BOT);
        top = new Linebreaker(RobotMap.SENSORS.LINEBREAK_TOP);

        this.table = NetworkTableInstance.getDefault().getTable("OpenSight");

        //remove all SmartDashboard elements from the Driver Station
        /**String[] smartDashKeys = SmartDashboard.getKeys().toArray(new String[0]);
        for (String key : smartDashKeys){
            SmartDashboard.delete(key);
        }*/

        String[] SDDoubles = {"Shooter Max Power", "Angle", "Calibrate1", "Calibrate2",
                "Encoder Distance", "Inches to Drive", "Rotation(degrees)", "target-x", "target-y", "Turret Pos", "Pos Degrees",
                "Shooter RPM", "Shooter Power Current", "Controls/Drive Max Power", "Controls/Turn Max Power", "Gain/FSP", "Hopper Power", "Magazine Power", "Shooter RPM Target",
                "Preshooter Power", "Shooter Left Power", "Shooter Right Power", "Shooter Left RPM", "Shooter Right RPM"};

        for (String doubleName : SDDoubles) {
            if (!SmartDashboard.containsKey(doubleName)) {
                SmartDashboard.putNumber(doubleName, 1);
                SmartDashboard.setPersistent(doubleName);
            }
        }
        CommandScheduler.getInstance().setDefaultCommand(Shooter.getInstance(), new Shoot());
        CommandScheduler.getInstance().setDefaultCommand(Intake.getInstance(), new RunIntake());
        CommandScheduler.getInstance().setDefaultCommand(Turret.getInstance(), new SpinTurret());
        CommandScheduler.getInstance().setDefaultCommand(DriveTrain.getInstance(), new Drive());
        
        CommandScheduler.getInstance().setDefaultCommand(Hopper.getInstance(), new TeleopHopper());
        CommandScheduler.getInstance().setDefaultCommand(Magazine.getInstance(), new TeleopMagazine());

        //CommandScheduler.getInstance().setDefaultCommand(Magazine.getInstance(), new RunMagazine());

        CommandScheduler.getInstance().schedule(new InstantCommand(Pneumatics.getInstance()::startCompressor));

        String[] SDBooleans = {"Front Limit", "Back Limit", "Auto Forward?", "LBottom", "LTop", "Enable AimBot"};

        for (String booleanName : SDBooleans) {
            if (!SmartDashboard.containsKey(booleanName)) {
                SmartDashboard.putBoolean(booleanName, false);
                SmartDashboard.setPersistent(booleanName);
            }
        }

        SmartDashboard.setDefaultString("Controls/Drive Scheme", RobotConfig.DRIVE.DRIVE_SCHEMES[0]);
        SmartDashboard.setDefaultStringArray("Controls/Drive Schemes", RobotConfig.DRIVE.DRIVE_SCHEMES);

        CommandScheduler.getInstance().run();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    /**
     * This method is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        CommandScheduler.getInstance().schedule(new InstantCommand(() -> Shooter.getInstance().setPosition(Shooter.Position.ONE)));
    }

    @Override
    public void teleopPeriodic() {
        //Do not show continuously update all Smart Dash variables to avoid
        //crashing the Driver Station
        boolean showSmartDashInfo = true;
        boolean showShooterPowerInfo = true;
        if (showSmartDashInfo){
            SmartDashboard.putNumber("Encoder Distance", DriveTrain.getInstance().getEncoderPosition());
            SmartDashboard.putNumber("Shooter RPM", Shooter.getInstance().getRPM());
            SmartDashboard.putNumber("Turret Pos", Turret.getInstance().getPosition());
            SmartDashboard.putNumber("Pos Degrees", Turret.getInstance().getDegreesPosition());
            SmartDashboard.putBoolean("Front Limit", Turret.getInstance().atFrontLimit());
            SmartDashboard.putBoolean("Back Limit", Turret.getInstance().atBackLimit());

            SmartDashboard.putBoolean("LBottom", getLinebreakBottom().lineBroken());
            SmartDashboard.putBoolean("LTop", getLinebreakTop().lineBroken());

            SmartDashboard.putNumber("target-x", this.table.getEntry("target-x").getDouble(666));
            SmartDashboard.putNumber("target-y", this.table.getEntry("target-y").getDouble(666));
            SmartDashboard.putNumber("Target Area", this.table.getEntry("area").getDouble(0));
            Shooter.getInstance().updateMotorPID();

            if(showShooterPowerInfo){
                SmartDashboard.putNumber("Shooter Left Power", Shooter.getInstance().getLeftPower());
                SmartDashboard.putNumber("Shooter Right Power", Shooter.getInstance().getRightPower());
                SmartDashboard.putNumber("Shooter Left RPM", Shooter.getInstance().getLeftRPM());
                SmartDashboard.putNumber("Shooter Right RPM", Shooter.getInstance().getRightRPM());
            }
        }
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }

    public static Linebreaker getLinebreakBottom() {
        return bottom;
    }

    public static Linebreaker getLinebreakTop() {
        return top;
    }
}
