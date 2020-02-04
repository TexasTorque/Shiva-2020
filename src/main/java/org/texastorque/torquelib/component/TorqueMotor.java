package org.texastorque.torquelib.component;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.texastorque.util.KPID;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.controller.PIDController;



public class TorqueMotor {
	private ControllerType type;

	public enum ControllerType {
		VICTOR, TALONSRX, SPARKMAX;
	}
	private SpeedController victor;
	private TalonSRX talon;
	private CANSparkMax spark;

	public TorqueMotor(ControllerType type, int port) {
		this.type = type;
		switch (type) {
		case VICTOR:
			victor = new VictorSP(port);
			break;
		case TALONSRX:
			talon = new TalonSRX(port);
			break;
		case SPARKMAX:
			spark = new CANSparkMax(port, MotorType.kBrushless);
			break;
		}
	}

	//for setting raw outputs to all kinds of motors
	public void set(double output){
		switch (type) {
			case VICTOR:
				victor.set(output);
				break;
			case TALONSRX:
				talon.set(ControlMode.PercentOutput, output);
				break;
			case SPARKMAX:
				spark.set(output);
				break;
		}
	}

	//for setting a talonsrx to a control mode and output (Position, Velocity, Lead/Follower)
	public void set(double output, ControlMode mode){
		switch (type) {
			case TALONSRX:
				talon.set(mode, output);
				break;
			//need to figure out control modes for sparkmax
			case SPARKMAX:
				spark.set(output);
				break;
		}
	}
	private PIDController talonPID;
	private CANPIDController sparkPID;
	public void configurePID(KPID kPID){
		switch (type) {
			case TALONSRX:
				// talonPID = new PIDController(kPID.p(), kPID.i(), kPID.d());
				// talonPID.enable()
				talon.config_kP(0, kPID.p());
				talon.config_kI(0, kPID.i());
				talon.config_kD(0, kPID.d());
				talon.config_kF(0, kPID.f());
				break;
			//need to figure out control modes for sparkmax
			case SPARKMAX:
				sparkPID = spark.getPIDController();
				sparkPID.setP(kPID.p());
				sparkPID.setI(kPID.i());
				sparkPID.setD(kPID.d());
				sparkPID.setFF(kPID.f());
				
				break;
		}
	}


}
