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
        double speed = -1*(OI.getINSTANCE().getPrimaryXboxRightTrigger() - OI.getINSTANCE().getPrimaryXboxLeftTrigger());
        double rotation = OI.getINSTANCE().getPrimaryXboxLeftX();
        speed *= SmartDashboard.getNumber("Drive Max Power", 1.0);
        
         // Brock Arcade Drive Xbox
         //double speed = OI.getINSTANCE().getPrimaryXboxLeftY();
         //double rotation = OI.getINSTANCE().getPrimaryXboxLeftX();

        if (OI.getINSTANCE().getPrimaryXboxA()) speed *= SmartDashboard.getNumber("Slow Mode Percent", 0.2);
        rotation *= rotation_factor;

        System.out.println("Drive() speed:");
        System.out.println(speed);
        System.out.println(rotation);

        //DriveTrain.getInstance().arcadeDrive(0,0, false);
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
