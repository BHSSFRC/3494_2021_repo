/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Intake extends SubsystemBase {

    private TalonSRX motor;

    private DoubleSolenoid cylinder = new DoubleSolenoid(RobotMap.COMPRESSOR.PCM1, RobotMap.INTAKE.CYLINDER_IN, RobotMap.INTAKE.CYLINDER_OUT);

    private final static Intake INSTANCE = new Intake();

  	public Intake() {
  	    this.motor = new TalonSRX(RobotMap.INTAKE.MOTOR);
  	}

    public void runIntake(double power) {
        this.motor.set(ControlMode.PercentOutput, power);
    }

    public void stop(){
  	    this.runIntake(0);
    }

    public void setDeployed(boolean deployed) {
        if (deployed) {
            this.cylinder.set(DoubleSolenoid.Value.kForward);
        } else {
            this.cylinder.set(DoubleSolenoid.Value.kReverse);
        }
    }
	
  	@Override
  	public void periodic() {
  	}

    public static Intake getInstance() {
        return INSTANCE;
    }
}