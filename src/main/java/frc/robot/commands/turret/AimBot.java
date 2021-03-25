package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

//Continuously rotate turret to face target
public class AimBot extends CommandBase {
    private double power;
    private double targetOffset;

    public AimBot() {
        addRequirements(Turret.getInstance());
        addRequirements(Shooter.getInstance());
        this.power = RobotConfig.TURRET.DEFAULT_SPIN_POWER;
        this.targetOffset = 0;
    }

    @Override
    public void initialize() {
        Shooter.getInstance().setLedRings(true);
        
        if (!SmartDashboard.getBoolean("Shooter/Enable AimBot", false)){
            this.end(false);
        }
    }

    @Override
    public void execute() {
        this.targetOffset = SmartDashboard.getNumber("Turret/target-x", 0);

        if(targetOffset < 1.0 && targetOffset > -1.0){
            this.power = Math.abs(this.targetOffset) * .6;
        }else{
            this.power = 0;
        }
        if(Math.abs(targetOffset) < 0.03){
            this.power = 0;
        }else{
            this.power = Math.min(Math.max(this.power, 0.15), 0.4);
        }

        if(this.targetOffset < 0){
            Turret.getInstance().spin(-this.power);
        }else{
            Turret.getInstance().spin(this.power);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrrupted) {
        CommandScheduler.getInstance().schedule(new SpinTurret());
        Shooter.getInstance().setLedRings(false);
    }
}