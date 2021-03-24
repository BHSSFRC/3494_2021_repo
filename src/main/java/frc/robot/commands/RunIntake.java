/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Intake;

//spin intake at speed according to xbox right trigger input
public class RunIntake extends CommandBase {

    public RunIntake() {
        addRequirements(Intake.getInstance());
    }

    @Override
    public void initialize() {
        Intake.getInstance().runIntake(0);
    }

    @Override
    public void execute() {
        double running = OI.getINSTANCE().getXboxRightTrigger() + (OI.getINSTANCE().getSecondaryXboxY() ? -1 : 0);
        boolean reverse = OI.getINSTANCE().getSecondaryXboxY();
        Intake.getInstance().runIntake(running * RobotConfig.MAGAZINE.INTAKE_DEFAULT_POWER + (OI.getINSTANCE().getXboxRightBumper() ? RobotConfig.MAGAZINE.INTAKE_DEFAULT_POWER : 0));
        Intake.getInstance().setDeployed(running != 0);
    }

    @Override
    public void end(boolean interrupted) {
        Intake.getInstance().stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
