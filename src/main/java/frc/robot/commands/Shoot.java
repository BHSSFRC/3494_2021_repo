package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.PreShooter;
import frc.robot.subsystems.Shooter;
import frc.robot.util.QuadTimer;

public class Shoot extends CommandBase {
    private double targetRPM;
    private QuadTimer timer;
    private double shootPower;
    private boolean fixedSpeed;
    private boolean goToRPM;

    //Run shooter at the RPM specified by the Smart Dashboard
    public Shoot() {
        addRequirements(Shooter.getInstance(), PreShooter.getInstance());
        this.timer = new QuadTimer();
        this.fixedSpeed = false;
        this.targetRPM = SmartDashboard.getNumber("Shooter RPM Target", -1);
        this.goToRPM = false;
    }

    /**
     * Shouldn't be used as power is less reliable than RPM
     * Run shooter at a constant speed
     * @param fixedPower - the power between 0 and 1 to run the shooter at
     */
    public Shoot(double fixedPower) {
        addRequirements(Shooter.getInstance(), PreShooter.getInstance());
        this.timer = new QuadTimer();
        this.fixedSpeed = true;
        this.shootPower = fixedPower;
        this.targetRPM = -1;
        this.goToRPM = false;
    }

    /**
     * Run shooter at a constant RPM
     * @param targetRPM - the target RPM
     * @param RPM - can be either true or false, the variable only exists to allow a second constructor with another double input
     */
    public Shoot(double targetRPM, boolean RPM){
        addRequirements(Shooter.getInstance(), PreShooter.getInstance());
        this.targetRPM = targetRPM;
        this.fixedSpeed = true;
        this.shootPower = 0.5;
        this.timer = new QuadTimer();
        this.goToRPM = true;
    }

    @Override
    public void initialize() {
        timer.start();
    }

    @Override
    public void execute() {
        //Run shooter at either specified power or RPM
        if(!this.fixedSpeed){
            if(SmartDashboard.getNumber("Shooter Max Power", .8) != -1){
                shootPower = OI.getINSTANCE().getXboxLeftTrigger() *
                        SmartDashboard.getNumber("Shooter Max Power", 1);
            }else{
                shootPower = OI.getINSTANCE().getXboxLeftTrigger() *
                        RobotConfig.SHOOTER.SHOOTER_MAX_POWER;
            }
        }

        if(this.goToRPM || (shootPower > 0.05 && targetRPM != -1)){
            Shooter.getInstance().setRPM(targetRPM);
        }else{
            Shooter.getInstance().shoot(shootPower);
        }

        /**Preshooter only runs if the shooter is active and has been active for a fixed amount of time(and so is running at full power)
         * this is to avoid having the preshooter send balls into the shooter when the shooter isn't yet at full power
         * timer tracks consecutive duration that shooter has been active for
         */
        if (timer.get() > RobotConfig.SHOOTER.PRESHOOTER_DELAY && shootPower > 0.05) {
            PreShooter.getInstance().spin(SmartDashboard.getNumber("Preshooter Power", RobotConfig.SHOOTER.PRESHOOTER_POWER));
        }else{
            PreShooter.getInstance().stop();
            if (shootPower < .05 && !this.goToRPM) {
                timer.reset();
            }
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
