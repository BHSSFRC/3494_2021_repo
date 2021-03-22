package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Magazine;

public class RunMagazine extends CommandBase {
    private boolean runFront;
    private boolean runBottom;
    private boolean frontReverse;
    private boolean bottomReverse;

    public RunMagazine() {
        this(false, false, false, false);
    }
    
    /**
     * run front and bottom motors in magazine at zero or constant speeds
     * @param front - whether the front motor spins or is at rest
     * @param bottom - whether the bottom motor spins or is at rest
     */
    public RunMagazine(boolean front, boolean bottom){
        this(front, false, bottom, false);
    }

    /**
     * run front and bottom motors in magazine at zero or constant speeds
     * @param front - whether the front motor spins or is at rest
     * @param frontReverse - whether the front motor spins forwards or backwards
     * @param bottom - whether the bottom motor spins or is at rest
     * @param bottomReverse - whether the bottom motor spins forwards or backwards
     */
    public RunMagazine(boolean front, boolean frontReverse, boolean bottom, boolean bottomReverse){
        addRequirements(Magazine.getInstance());

        this.runFront = front;
        this.runBottom = bottom;
        this.frontReverse = frontReverse;
        this.bottomReverse = bottomReverse;
    }

    @Override
    public void initialize() {
        Magazine.getInstance().stop();
    }

    @Override
    public void execute() {
        double front = this.runFront ? RobotConfig.MAGAZINE.FRONT_MOTOR_DEFAULT_POWER : 0;
        double bottom = this.runBottom ? RobotConfig.MAGAZINE.BOTTOM_MOTOR_DEFAULT_POWER : 0;
        
        if (this.frontReverse) front = -front;
        if (this.bottomReverse) bottom = -bottom;

        Magazine.getInstance().runFront(front);
        Magazine.getInstance().runBottom(bottom);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Magazine.getInstance().stop();
    }
}
