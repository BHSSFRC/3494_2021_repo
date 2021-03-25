package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.PreShooter;
import frc.robot.subsystems.Shooter;
import frc.robot.util.QuadTimer;

public class TeleopShooter extends CommandBase {
    private double targetRPM;
    private QuadTimer timer;
    private Shooter.Position shooterPosition = Shooter.Position.ONE;

    //Run shooter at the RPM specified by the Smart Dashboard
    public TeleopShooter() {
        addRequirements(Shooter.getInstance(), PreShooter.getInstance());
        this.timer = new QuadTimer();
        this.targetRPM = SmartDashboard.getNumber("Shooter/Shooter RPM Target", 1);
    }

    @Override
    public void initialize() {
        timer.start();
    }

    @Override
    public void execute() {
        //Run shooter at either specified power or RPM
        //if (!this.fixedSpeed) {
        //shootPower = OI.getINSTANCE().getXboxLeftTrigger() * SmartDashboard.getNumber("Shooter/Shooter Max Power", 1);
        //Shooter.getInstance().shoot(shootPower);
        double currentRPM = Shooter.getInstance().getLeftRPM();
        targetRPM = OI.getINSTANCE().getXboxLeftTrigger() * SmartDashboard.getNumber("Shooter/Shooter RPM Target", 1);
        Shooter.getInstance().setRPM(targetRPM);

        if (OI.getINSTANCE().getXboxDpadDown()) shooterPosition = Shooter.Position.ONE;
        else if (OI.getINSTANCE().getXboxDpadLeft()) shooterPosition = Shooter.Position.TWO;
        else if (OI.getINSTANCE().getXboxDpadRight()) shooterPosition = Shooter.Position.THREE;
        else if (OI.getINSTANCE().getXboxDpadUp()) shooterPosition = Shooter.Position.FOUR;

        if (targetRPM > 0) {
            Shooter.getInstance().setPosition(shooterPosition);
        } else {
            Shooter.getInstance().setPosition(Shooter.Position.ONE);
        }

        //}

        /*if(this.goToRPM || (shootPower > 0.05 && targetRPM != -1)){
            Shooter.getInstance().setRPM(targetRPM);
        }else{
            Shooter.getInstance().shoot(shootPower);
        }*/

        /**Preshooter only runs if the shooter is active and has been active for a fixed amount of time(and so is running at full power)
         * this is to avoid having the preshooter send balls into the shooter when the shooter isn't yet at full power
         * timer tracks consecutive duration that shooter has been active for
         */
        if (timer.get() > RobotConfig.SHOOTER.PRESHOOTER_DELAY && Shooter.getInstance().atTargetSpeed(targetRPM) && targetRPM != 0) {
            PreShooter.getInstance().spin(SmartDashboard.getNumber("Magazine/Preshooter Power", RobotConfig.SHOOTER.PRESHOOTER_POWER));
        }else{
            PreShooter.getInstance().stop();
            if (targetRPM == 0) timer.reset();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted){
        PreShooter.getInstance().stop();
        Shooter.getInstance().stop();
    }
}
