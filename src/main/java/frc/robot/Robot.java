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
        /*String[] smartDashKeys = SmartDashboard.getKeys().toArray(new String[0]);
        for (String key : smartDashKeys){
            SmartDashboard.delete(key);
        }*/

        String[] SDDoubles = {
            "Calibrate1", "Calibrate2", "Gain/FSP",
            "Shooter/Shooter Max Power", "Shooter/Shooter RPM", "Shooter/Shooter Power Current", "Shooter/Shooter RPM Target", "Shooter/Shooter Left Power", "Shooter/Shooter Right Power", "Shooter/Shooter Left RPM", "Shooter/Shooter Right RPM", "Shooter/P Gain", "Shooter/I Gain", "Shooter/D Gain", "Shooter/I Zone", "Shooter/Feed Forward", "Shooter/Max Output", "Shooter/Min Output",
            "DriveTrain/Angle", "DriveTrain/Encoder Distance", "DriveTrain/Inches to Drive", "DriveTrain/DriveStraight Offset", "DriveTrain/DriveStraight Offset", "DriveTrain/PID P", "DriveTrain/PID I", "DriveTrain/PID D",
            "Turret/Rotation(degrees)", "Turret/target-x", "Turret/target-y", "Turret/Target Area", "Turret/Turret Pos", "Turret/Pos Degrees",
            "Controls/Drive Max Power", "Controls/Turn Max Power",
            "Magazine/Hopper Power", "Magazine/Front Magazine Power", "Magazine/Bottom Magazine Power", "Magazine/Preshooter Power"
        };

        for (String doubleName : SDDoubles) {
            if (!SmartDashboard.containsKey(doubleName)) {
                SmartDashboard.putNumber(doubleName, 1);
                SmartDashboard.setPersistent(doubleName);
            }
        }

        String[] SDBooleans = {
            "Turret/Front Limit", "Turret/Back Limit", "Turret/Go To Setpoint?",
            "DriveTrain/Auto Forward?", 
            "Shooter/Enable AimBot", 
            "Magazine/Linebreak Bottom", "Magazine/Linebreak Top", "Magazine/Intaking Routine"
        };

        for (String booleanName : SDBooleans) {
            if (!SmartDashboard.containsKey(booleanName)) {
                SmartDashboard.putBoolean(booleanName, false);
                SmartDashboard.setPersistent(booleanName);
            }
        }

        String[] SDStrins = {
            "Turret/Spin",
            "Controls/Drive Scheme"
        };

        for (String stringName : SDStrins) {
            if (!SmartDashboard.containsKey(stringName)) {
                SmartDashboard.putString(stringName, "");
                SmartDashboard.setPersistent(stringName);
            }
        }
        
        if (!SmartDashboard.containsKey("Controls/Drive Schemes")) {
            SmartDashboard.setDefaultStringArray("Controls/Drive Schemes", RobotConfig.DRIVE.DRIVE_SCHEMES);
            SmartDashboard.setPersistent("Controls/Drive Schemes");
        }

        if (!SmartDashboard.containsKey("Shooter/Shooter Presets")) {
            SmartDashboard.setDefaultStringArray("Shooter/Shooter Presets", RobotConfig.SHOOTER.PRESETS);
            SmartDashboard.setPersistent("Shooter/Shooter Presets");
        }

        CommandScheduler.getInstance().setDefaultCommand(Shooter.getInstance(), new TeleopShooter());
        CommandScheduler.getInstance().setDefaultCommand(Intake.getInstance(), new RunIntake());
        CommandScheduler.getInstance().setDefaultCommand(Turret.getInstance(), new SpinTurret());
        CommandScheduler.getInstance().setDefaultCommand(DriveTrain.getInstance(), new Drive());
        
        CommandScheduler.getInstance().setDefaultCommand(Hopper.getInstance(), new TeleopHopper());
        CommandScheduler.getInstance().setDefaultCommand(Magazine.getInstance(), new TeleopMagazine());

        //CommandScheduler.getInstance().setDefaultCommand(Magazine.getInstance(), new RunMagazine());

        CommandScheduler.getInstance().schedule(new InstantCommand(Pneumatics.getInstance()::startCompressor));

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
            SmartDashboard.putNumber("DriveTrain/Encoder Distance", DriveTrain.getInstance().getEncoderPosition());
            SmartDashboard.putNumber("Shooter/Shooter RPM", Shooter.getInstance().getRPM());
            SmartDashboard.putNumber("Turret/Turret Pos", Turret.getInstance().getPosition());
            SmartDashboard.putNumber("Turret/Pos Degrees", Turret.getInstance().getDegreesPosition());
            SmartDashboard.putBoolean("Turret/Front Limit", Turret.getInstance().atFrontLimit());
            SmartDashboard.putBoolean("Turret/Back Limit", Turret.getInstance().atBackLimit());

            SmartDashboard.putBoolean("Magazine/Linebreak Bottom", getLinebreakBottom().lineBroken());
            SmartDashboard.putBoolean("Magazine/Linebreak Top", getLinebreakTop().lineBroken());

            SmartDashboard.putNumber("Turret/target-x", this.table.getEntry("target-x").getDouble(666));
            SmartDashboard.putNumber("Turret/target-y", this.table.getEntry("target-y").getDouble(666));
            SmartDashboard.putNumber("Turret/Target Area", this.table.getEntry("area").getDouble(0));
            Shooter.getInstance().updateMotorPID();

            if(showShooterPowerInfo){
                SmartDashboard.putNumber("Shooter/Shooter Left Power", Shooter.getInstance().getLeftPower());
                SmartDashboard.putNumber("Shooter/Shooter Right Power", Shooter.getInstance().getRightPower());
                SmartDashboard.putNumber("Shooter/Shooter Left RPM", Shooter.getInstance().getLeftRPM());
                SmartDashboard.putNumber("Shooter/Shooter Right RPM", Shooter.getInstance().getRightRPM());
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
