package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
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
    }

    @Override
    public void execute() {
        Magazine.getInstance().runFront(this.runFront, this.frontReverse);
        Magazine.getInstance().runBottom(this.runBottom, this.bottomReverse);
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
