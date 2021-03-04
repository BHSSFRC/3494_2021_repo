package frc.robot.commands.teleop;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.util.QuadTimer;

public class CountBallsShot extends CommandBase {
    private int ballsShotSoFar;
    private int ballsShotGoal;
    private boolean topLinebreakTripped;
    private QuadTimer timer;

    /**
     * command runs until the given number of balls have all been shot by the shooter
     * @param goal - number of balls that need to be shot
     */
    public CountBallsShot(int goal) {
        addRequirements();
        this.ballsShotSoFar = 0;
        this.ballsShotGoal = goal;
        this.topLinebreakTripped = false;
        this.timer = new QuadTimer();
        this.timer.reset();
        this.timer.stop();
    }

    @Override
    public void initialize() {
        this.ballsShotSoFar = 0;
        this.timer.reset();
        this.timer.stop();
        System.out.println("Timer init: " + this.timer.get());
    }

    @Override
    public void execute() {
        if(!this.topLinebreakTripped && Robot.getLinebreakTop().lineBroken()){
            this.ballsShotSoFar++;
            this.topLinebreakTripped = true;
            System.out.println("Balls shot: " + this.ballsShotSoFar);
        }
        if(this.topLinebreakTripped && !Robot.getLinebreakTop().lineBroken()){
            this.topLinebreakTripped = false;
        }
        if (this.ballsShotSoFar == this.ballsShotGoal && this.timer.get() == 0.0){
            this.timer.start();
        }
    }

    @Override
    public boolean isFinished() {
        return (this.timer.get() > 0.5) && !Robot.getLinebreakTop().lineBroken();
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("All balls shot!");
    }
}
