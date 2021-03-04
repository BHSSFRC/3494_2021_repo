package frc.robot.commands.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.subsystems.Turret;

public class SpinToPosition extends CommandBase {
    private double setpoint;
    private double power;

    //spin turret to center position
    public SpinToPosition() {
        addRequirements(Turret.getInstance());
        this.power = RobotConfig.TURRET.DEFAULT_SPIN_POWER;
        this.setpoint = RobotMap.TURRET.RANGE_OF_MOTION / 2;
    }

    /**
     * spin turret to specified position
     * @param degreesPosGoal - goal position in degrees for the turret
     */
    public SpinToPosition(double degreesPosGoal){
        addRequirements(Turret.getInstance());
        this.power = RobotConfig.TURRET.DEFAULT_SPIN_POWER;
        this.setpoint = degreesPosGoal;
    }

    @Override
    public void initialize() {
        SmartDashboard.putBoolean("Go To Setpoint?", true);
        Turret.getInstance().setsetpoint(this.setpoint);
    }

    @Override
    public void execute() {
        double degreesOffset = Turret.getInstance().getDegreesPosition() - setpoint;
        if(degreesOffset < 0){
            Turret.getInstance().spin(-this.power);
        }else{
            Turret.getInstance().spin(this.power);
        }
    }

    @Override
    public boolean isFinished() {
        return Turret.getInstance().atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        SmartDashboard.putBoolean("Go To Setpoint?", false);
    }
}