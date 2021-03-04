/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Climber extends SubsystemBase {

	private Solenoid pancake;
	private Solenoid wheelOfFortune;
	private TalonSRX motor1;
	private TalonSRX motor2;
	//private Encoder encoder;

	/**
	 * Creates a new Climber.
	 */
	private final static Climber INSTANCE = new Climber();
	
	public Climber() {
		pancake = new Solenoid(RobotMap.COMPRESSOR.PCM1, RobotMap.CLIMBER.PANCAKE);
		wheelOfFortune = new Solenoid(RobotMap.COMPRESSOR.PCM1, RobotMap.CLIMBER.WHEEL_OF_FORTUNE);
		motor1 = new TalonSRX(RobotMap.CLIMBER.MOTOR1);
		motor2 = new TalonSRX(RobotMap.CLIMBER.MOTOR2);
		this.motor1.setInverted(true);
		this.motor2.setInverted(true);
		motor1.configClosedloopRamp(RobotMap.CLIMBER.CLIMB_UP_RAMPRATE);
		motor2.configClosedloopRamp(RobotMap.CLIMBER.CLIMB_UP_RAMPRATE);
	}

	public void pancake(boolean out) {
		pancake.set(out);
	}

	public void drive(double power) {
		motor1.set(ControlMode.PercentOutput, power);
		motor2.set(ControlMode.PercentOutput, power);
	}

	public void stop() {
		motor1.set(ControlMode.PercentOutput, 0);
		motor2.set(ControlMode.PercentOutput, 0);
	}

	@Override
	public void periodic() {
	}

	public static Climber getInstance() {
        return INSTANCE;
	}
}
