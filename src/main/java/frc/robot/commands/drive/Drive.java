package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.DriveTrain;

//Rocket League Drive
public class Drive extends CommandBase {
    private DriveTrain m_drivetrain;

    public Drive(DriveTrain drivetrain){
        addRequirements(drivetrain);
        m_drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        m_drivetrain.tankDrive(0,0);
    }

    @Override
    public void execute() {

        String driveScheme = SmartDashboard.getString("Controls/Drive Scheme", RobotConfig.DRIVE.DRIVE_SCHEMES[0]);

        if (driveScheme.equals(RobotConfig.DRIVE.DRIVE_SCHEMES[0])) {
            double speed = OI.getINSTANCE().getPrimaryXboxLeftY();
            double rotation = OI.getINSTANCE().getPrimaryXboxLeftX();
            speed *= SmartDashboard.getNumber("Controls/Drive Max Power", 1.0);
            rotation *= SmartDashboard.getNumber("Controls/Turn Max Power", 1.0);

            if (OI.getINSTANCE().getPrimaryXboxA()) speed *= SmartDashboard.getNumber("Controls/Slow Mode Percent", 0.2);

            m_drivetrain.arcadeDrive(speed, rotation, true);
        } else if (driveScheme.equals(RobotConfig.DRIVE.DRIVE_SCHEMES[1])) {
            double speed = -1 * (OI.getINSTANCE().getPrimaryXboxRightTrigger() - OI.getINSTANCE().getPrimaryXboxLeftTrigger());
            double rotation = OI.getINSTANCE().getPrimaryXboxLeftX();
            speed *= SmartDashboard.getNumber("Controls/Drive Max Power", 1.0);
            rotation *= SmartDashboard.getNumber("Controls/Turn Max Power", 1.0);

            if (OI.getINSTANCE().getPrimaryXboxA()) speed *= SmartDashboard.getNumber("Controls/Slow Mode Percent", 0.2);

            m_drivetrain.arcadeDrive(speed, rotation, true);
        } else if (driveScheme.equals(RobotConfig.DRIVE.DRIVE_SCHEMES[2])) {
            double leftSpeed = OI.getINSTANCE().getLeftFlightY();
            double rightSpeed = OI.getINSTANCE().getRightFlightY();
            leftSpeed *= SmartDashboard.getNumber("Controls/Drive Max Power", 1.0);
            rightSpeed *= SmartDashboard.getNumber("Controls/Drive Max Power", 1.0);

            if (OI.getINSTANCE().getPrimaryXboxA()) {
                leftSpeed *= SmartDashboard.getNumber("Controls/Slow Mode Percent", 0.2);
                rightSpeed *= SmartDashboard.getNumber("Controls/Slow Mode Percent", 0.2);
            }

            m_drivetrain.tankDrive(leftSpeed, rightSpeed);
        } else if (driveScheme.equals(RobotConfig.DRIVE.DRIVE_SCHEMES[3])) {
            double speed = -1 * OI.getINSTANCE().getLeftFlightY();
            double rotation = OI.getINSTANCE().getLeftFlightX();
            speed *= SmartDashboard.getNumber("Controls/Drive Max Power", 1.0);
            rotation *= SmartDashboard.getNumber("Controls/Turn Max Power", 1.0);

            if (OI.getINSTANCE().getPrimaryXboxA()) speed *= SmartDashboard.getNumber("Controls/Slow Mode Percent", 0.2);

            m_drivetrain.arcadeDrive(speed, rotation, true);
        }
    }

    private static double powerCurve(double x) {
        double curve = Math.pow(Math.sin(Math.PI / 2 * Math.abs(x)), RobotConfig.DRIVE.POWER_CURVE_EXPONENT) * Math.signum(x);
        return Math.copySign(curve, x);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_drivetrain.stop();
    }
}
