/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

//run the climber at the given power, retract pancake piston and stop climber
//once command ends
public class DriveClimb extends CommandBase {
	private double power;

	public DriveClimb(double power) {
		addRequirements(Climber.getInstance());
		this.power = power;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		Climber.getInstance().drive(this.power);
	}

	@Override
	public void end(boolean interrupted) {
		Climber.getInstance().pancake(false);
		Climber.getInstance().stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}
