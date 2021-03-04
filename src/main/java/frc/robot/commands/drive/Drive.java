package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.DriveTrain;

//Arcade Drive
public class Drive extends CommandBase {

    public Drive() {
        addRequirements(DriveTrain.getInstance());
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        /*double xSpeed = powerCurve(OI.getINSTANCE().getPrimaryXboxRightTrigger() - OI.getINSTANCE().getPrimaryXboxLeftTrigger());
        double zRotation = OI.getINSTANCE().getPrimaryXboxLeftX();
        if(zRotation > 0.1){
            zRotation = .9;
        }else if(zRotation < -0.1){
            zRotation = -.9;
        }
        if(OI.getINSTANCE().getPrimaryXboxA()){
            xSpeed *= 0.2;
            //zRotation *= 0.2;
        }else{
            xSpeed *= SmartDashboard.getNumber("Drive Max Power", 1.0);
            //zRotation *= SmartDashboard.getNumber("Drive Max Power", 1.0);
        }
        DriveTrain.getInstance().arcadeDrive(xSpeed, zRotation, false);*/

        double leftSpeed = powerCurve(OI.getINSTANCE().getLeftFlightY());
        double rightSpeed = powerCurve(OI.getINSTANCE().getRightFlightY());

        DriveTrain.getInstance().tankDrive(leftSpeed, rightSpeed);
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
