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
        Intake.getInstance().runIntake(OI.getINSTANCE().getXboxRightTrigger() * RobotConfig.MAGAZINE.INTAKE_DEFAULT_POWER);
        Intake.getInstance().setDeployed(OI.getINSTANCE().getXboxRightTrigger() > 0);
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
