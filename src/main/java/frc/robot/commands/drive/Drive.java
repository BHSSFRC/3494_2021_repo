package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.DriveTrain;

//Rocket League Drive
public class Drive extends CommandBase {

    public Drive() {
        addRequirements(DriveTrain.getInstance());
    }

    @Override
    public void initialize() {
        DriveTrain.getInstance().tankDrive(0,0);
    }

    @Override
    public void execute() {
        double rotation_factor = 0.8;

        //Emerson Rocket League Drive
        String driveScheme = SmartDashboard.getString("Controls/Drive Scheme", "Bork");

        double speed = 0;
        double rotation = 0;

        if (driveScheme.equals(RobotConfig.DRIVE.DRIVE_SCHEMES[0])) {
            speed = OI.getINSTANCE().getPrimaryXboxLeftY();
            rotation = OI.getINSTANCE().getPrimaryXboxLeftX();
        } else if (driveScheme.equals(RobotConfig.DRIVE.DRIVE_SCHEMES[1])) {
            speed = -1*(OI.getINSTANCE().getPrimaryXboxRightTrigger() - OI.getINSTANCE().getPrimaryXboxLeftTrigger());
            rotation = OI.getINSTANCE().getPrimaryXboxLeftX();
            speed *= SmartDashboard.getNumber("Controls/Drive Max Power", 1.0);
            rotation *= SmartDashboard.getNumber("Controls/Turn Max Power", 1.0);
        }

        if (OI.getINSTANCE().getPrimaryXboxA()) speed *= SmartDashboard.getNumber("Controls/Slow Mode Percent", 0.2);
        rotation *= rotation_factor;

        DriveTrain.getInstance().arcadeDrive(speed, rotation, true);
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
        DriveTrain.getInstance().stop();
    }
}
