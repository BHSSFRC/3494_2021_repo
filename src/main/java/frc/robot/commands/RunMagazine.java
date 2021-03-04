package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Magazine;

public class RunMagazine extends CommandBase {
    private boolean runFront;
    private boolean runBottom;
    private boolean runTop;

    public RunMagazine() {
        this(false, false);
    }

    /**
     * run front and bottom motors in magazine at zero or constant speeds
     * @param front - whether the front motor spins or is at rest
     * @param bottom - whether the bottom motor spins or is at rest
     */
    public RunMagazine(boolean front, boolean bottom){
        addRequirements(Magazine.getInstance());
        this.runFront = front;
        this.runBottom = bottom;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Magazine.getInstance().runFront(this.runFront);
        Magazine.getInstance().runBottom(this.runBottom);
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
