package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.ReleaseClimb;
import frc.robot.commands.DriveClimb;
import frc.robot.commands.Shoot;
import frc.robot.commands.drive.DistanceDrive;
import frc.robot.commands.teleop.*;
import frc.robot.commands.turret.AimBot;
import frc.robot.subsystems.Shooter;

public class OI {
    private static OI INSTANCE = new OI();
    private Joystick leftFlight;
    private Joystick rightFlight;
    private XboxController primaryXbox;
    private XboxController secondaryXbox;

    private JoystickButton runMagazine;

    private JoystickButton runShooter;
    private JoystickButton floorPickup;
    private Trigger releaseClimber;
    private Trigger retractClimber;
    private Trigger extendClimber;
    private JoystickButton safetyClimber;
    private JoystickButton intakingRoutine;
    private JoystickButton spinHopperMagazine;
    private JoystickButton ejectBalls;
    private JoystickButton quickTurretLimits;
    private JoystickButton aimBot;
    private JoystickButton turretToStartPos;
    private Trigger shooterLow;
    private Trigger shooterMed;
    private Trigger shooterHigh;
    private Trigger leftTriggerPressed;
    private JoystickButton turnDegrees;
    private JoystickButton aimAndShoot;
    private JoystickButton distanceDrive;
    private JoystickButton enableAimbot;

    private ButtonBoard bb;
    private JoystickButton[] boardButtons;

    private OI(){
        //leftFlight = new Joystick(RobotMap.OI.LEFT_FLIGHT);
        //rightFlight = new Joystick(RobotMap.OI.RIGHT_FLIGHT);

        primaryXbox = new XboxController(RobotMap.OI.PRIMARY_XBOX);
        secondaryXbox = new XboxController(RobotMap.OI.SECONDARY_XBOX);

        bb = new ButtonBoard(RobotMap.OI.BUTTON_BOARD);
        boardButtons = new JoystickButton[15];

        ejectBalls = new JoystickButton(bb, RobotMap.OI.EJECT_BALLS);
        ejectBalls.whenPressed(new InstantCommand(() -> new ReverseIntake().withInterrupt(() -> !ejectBalls.get()).schedule(false)));

        runShooter = new JoystickButton(secondaryXbox, RobotMap.OI.RUN_SHOOTER);
        runShooter.whileHeld(new Shoot());

        intakingRoutine = new JoystickButton(secondaryXbox, RobotMap.OI.INTAKING_ROUTINE);
        intakingRoutine.whenPressed(new IntakingRoutine().withTimeout(10).andThen(new InstantCommand(() -> System.out.println("Finish Intaking Routine"))));

/*
        floorPickup = new JoystickButton(bb, RobotMap.OI.FLOOR_PICKUP);
        floorPickup.whileHeld(new FloorPickup());
        
        //quickTurretLimits = new JoystickButton(bb, RobotMap.OI.QUICK_TURRET_LIMITS);
        //quickTurretLimits.whenPressed(new QuickTurretLimit());
        enableAimbot = new JoystickButton(bb, RobotMap.OI.ENABLE_AIM_BOT);
        enableAimbot.whenPressed(new InstantCommand(() ->
                SmartDashboard.putBoolean("Enable AimBot", !SmartDashboard.getBoolean("Enable AimBot", true))));

        aimBot = new JoystickButton(bb, RobotMap.OI.AIM_BOT);
        aimBot.toggleWhenPressed(new AimBot());
        //turnDegrees = new JoystickButton(bb, RobotMap.OI.TURN_DEGREES);
        //turnDegrees.whenPressed(new TurnDegrees(80));
        //turretToStartPos = new JoystickButton(bb, RobotMap.OI.TURRET_TO_START_POS);
        //turretToStartPos.whenPressed(new SpinToPosition(0.0));
        //distanceDrive = new JoystickButton(secondaryXbox, RobotMap.OI.DISTANCE_DRIVE);
        //distanceDrive.whenPressed(new DistanceDrive(40));

        safetyClimber = new JoystickButton(bb, RobotMap.OI.SAFETY_CLIMBER);
        retractClimber = new JoystickButton(bb, RobotMap.OI.REVERSE_CLIMBER).and(safetyClimber);
        extendClimber = new JoystickButton(bb, RobotMap.OI.DRIVE_CLIMBER).and(safetyClimber);
        releaseClimber = new JoystickButton(bb, RobotMap.OI.RELEASE_CLIMBER).and(safetyClimber);

        releaseClimber.whenActive(new ReleaseClimb());
        retractClimber.whileActiveContinuous(new DriveClimb(RobotMap.CLIMBER.CLIMB_DOWN_POWER));
        extendClimber.whileActiveContinuous(new DriveClimb(RobotMap.CLIMBER.CLIMB_UP_POWER));

        intakingRoutine = new JoystickButton(bb, RobotMap.OI.INTAKING_ROUTINE);
        intakingRoutine.whenPressed(new IntakingRoutine().withTimeout(10).andThen(new InstantCommand(() -> System.out.println("Finish Intaking Routine"))));

        spinHopperMagazine = new JoystickButton(secondaryXbox, RobotMap.OI.SPIN_HOPPER_MAGAZINE);
        spinHopperMagazine.whenPressed(new RunHopperMagazine());
        spinHopperMagazine.whenReleased(new StopHopperMagazine());

        leftTriggerPressed = new Trigger(() -> getSecondaryXboxLeftTriggerPressed());
        shooterLow = new JoystickButton(bb, RobotMap.OI.SHOOTER_LOW).and(leftTriggerPressed);
        shooterMed = new JoystickButton(bb, RobotMap.OI.SHOOTER_MED).and(leftTriggerPressed);
        shooterHigh = new JoystickButton(bb, RobotMap.OI.SHOOTER_HIGH).and(leftTriggerPressed);

        shooterLow.whenActive(new InstantCommand(() -> Shooter.getInstance().setPosition(Shooter.Position.ONE)));
        shooterMed.whenActive(new InstantCommand(() -> Shooter.getInstance().setPosition(Shooter.Position.TWO)));
        shooterHigh.whenActive(new InstantCommand(() -> Shooter.getInstance().setPosition(Shooter.Position.THREE)));

        aimAndShoot = new JoystickButton(bb, RobotMap.OI.AIM_AND_SHOOT);
        aimAndShoot.whileHeld(new AimAndShoot(5).withInterrupt(() -> !this.aimAndShoot.get()));*/
    }

    public double getLeftFlightY(){
        return this.leftFlight.getY();
    }

    public double getRightFlightY(){
        return this.rightFlight.getY();
    }

    public double getLeftFlightX(){
        return this.leftFlight.getX();
    }

    public double getRightFlightX(){
        return this.rightFlight.getX();
    }

    public double getPrimaryXboxLeftTrigger(){
        return this.primaryXbox.getTriggerAxis(GenericHID.Hand.kLeft);
    }

    public double getPrimaryXboxRightTrigger(){
        return this.primaryXbox.getTriggerAxis(GenericHID.Hand.kRight);
    }

    public double getPrimaryXboxLeftY(){
        return this.primaryXbox.getY(GenericHID.Hand.kLeft);
    }

    public double getPrimaryXboxRightY(){
        return this.primaryXbox.getY(GenericHID.Hand.kRight);
    }

    public double getPrimaryXboxLeftX(){
        return this.primaryXbox.getX(GenericHID.Hand.kLeft);
    }

    public boolean getPrimaryXboxA(){
        return this.primaryXbox.getAButton();
    }

    public boolean getSecondaryXboxB(){
        return this.secondaryXbox.getAButton();
    }

    public double getXboxRightY(){
        return this.secondaryXbox.getY(GenericHID.Hand.kRight);
    }

    public double getXboxRightX() {
        return this.secondaryXbox.getX(GenericHID.Hand.kRight);
    }

    public double getXboxRightTrigger(){
        return this.secondaryXbox.getTriggerAxis(GenericHID.Hand.kRight);
    }

    public double getXboxLeftTrigger(){
        return this.secondaryXbox.getTriggerAxis(GenericHID.Hand.kLeft);
    }

    public boolean getXboxRightBumper(){
        return this.secondaryXbox.getBumper(GenericHID.Hand.kRight);
    }

    public boolean getXboxLeftBumper(){
        return this.secondaryXbox.getBumper(GenericHID.Hand.kLeft);
    }

    public boolean getXboxLeftBumperPressed(){
        return this.secondaryXbox.getBumperPressed(GenericHID.Hand.kLeft);
    }

    public boolean getXboxLeftBumperReleased(){
        return this.secondaryXbox.getBumperReleased(GenericHID.Hand.kLeft);
    }

    public boolean getSecondaryXboxLeftTriggerPressed(){
        return this.secondaryXbox.getTriggerAxis(GenericHID.Hand.kLeft) > 0.05;
    }

    public boolean getXboxDpadUp(){
        return this.secondaryXbox.getPOV() == 0;
    }

    public boolean getXboxDpadDown(){
        return this.secondaryXbox.getPOV() == 180;
    }

    public boolean getXboxDpadLeft(){
        return this.secondaryXbox.getPOV() == 270;
    }

    public boolean getXboxDpadRight(){
        return this.secondaryXbox.getPOV() == 90;
    }

    public static OI getINSTANCE(){
        return INSTANCE;
    }
}
