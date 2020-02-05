package org.texastorque.torquelib.component;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.texastorque.util.KPID;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.controller.PIDController;

import java.util.ArrayList;

public class TorqueMotor {
	private ControllerType type;

	public enum ControllerType {
		VICTOR, TALONSRX, SPARKMAX;
	}

	private SpeedController victor;
	private TalonSRX talon;
	private CANSparkMax spark;

	private CANEncoder sparkEncoder;

	private int port;
	private double minOutput;
	private double maxOutput;
	private boolean currentLimit;

	// ----------------- Constructor -----------------
	public TorqueMotor(ControllerType type, int port) {
		this.type = type;
		this.port = port;
		switch (type) {
		case VICTOR:
			victor = new VictorSP(port);
			break;
		case TALONSRX:
			talon = new TalonSRX(port);
			break;
		case SPARKMAX:
			spark = new CANSparkMax(port, MotorType.kBrushless);
			sparkEncoder = spark.getEncoder();
			break;
		}
	} // constructor 

	// ------------------ Set Methods ------------------
	//for setting raw outputs to all kinds of motors
	public void set(double output){
		switch (type) {
			case VICTOR:
				victor.set(output);
				break;
			case TALONSRX:
				talon.set(ControlMode.PercentOutput, output);
				for(TalonSRX talonSRX : talonFollowers){
					talonSRX.set(ControlMode.PercentOutput, port);
				}
				break;
			case SPARKMAX:
				spark.set(output);
				// for(CANSparkMax)
				break;
		}
	} // generic set method 

	//for setting a talonsrx to a control mode and output (Position, Velocity, Lead/Follower)
	public void set(double output, ControlMode modeTalon){
		switch (type) {
			case TALONSRX:
				if (modeTalon == ControlMode.Follower){
					output = (int)output;
				} // if follower 
				talon.set(modeTalon, output);
				for(TalonSRX talonSRX : talonFollowers){
					talonSRX.set(modeTalon, port);
				}
				break;
		} // outside switch statement 
	} // set with control mode for talon 

	public void set(double output, ControlType ctrlTypeSparkMax){
		switch(type){
			case SPARKMAX:
				try{
					sparkPID.setReference(output, ctrlTypeSparkMax);
				} catch (Exception e){
					System.out.println(e);
					System.out.println("You need to configure the PID");
				}
				break;
		}
	} // set with control type for spark max 

	// ----------------------------- Followers --------------------------
	private ArrayList<TalonSRX> talonFollowers = new ArrayList<>();
	private ArrayList<CANSparkMax> sparkMaxFollowers = new ArrayList<>();

	public void addFollower(int port){
		switch(type) {
			case TALONSRX:
				talonFollowers.add(new TalonSRX(port));
				break;
			case SPARKMAX:
				sparkMaxFollowers.add(new CANSparkMax(port, MotorType.kBrushless));
		}
	} // add follower 
	
	// ----------------------------- PID Stuff ----------------------------
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
				talon.configPeakOutputForward(kPID.max());
				talon.configPeakOutputReverse(kPID.min());
				break;
			//need to figure out control modes for sparkmax
			case SPARKMAX:
				sparkPID = spark.getPIDController();
				sparkPID.setP(kPID.p());
				sparkPID.setI(kPID.i());
				sparkPID.setD(kPID.d());
				sparkPID.setFF(kPID.f());
				sparkPID.setOutputRange(kPID.min(), kPID.max());
				break;
		} // type switch
	} // configure PID

	public void updatePID(KPID kPID){
		switch (type) {
			case TALONSRX:
				talon.config_kP(0, kPID.p());
				talon.config_kI(0, kPID.i());
				talon.config_kD(0, kPID.d());
				talon.config_kF(0, kPID.f());
				break;
			//need to figure out control modes for sparkmax
			case SPARKMAX:
				sparkPID.setP(kPID.p());
				sparkPID.setI(kPID.i());
				sparkPID.setD(kPID.d());
				sparkPID.setFF(kPID.f());
				sparkPID.setOutputRange(kPID.min(), kPID.max());
				break;
		} // type switch
	} // update PID

	// ----------------------- Encoder Stuff ---------------------

	public double getVelocity(){
		switch(type){
			case TALONSRX:
				try{
					return talon.getSelectedSensorVelocity();
				} catch (Exception e){
					System.out.println(e);
					System.out.println("There is no encoder present, you need to put one in");
				}
			case SPARKMAX:
				return sparkEncoder.getVelocity();
		}
		return 0;
	} // get velocity

	public double getPosition(){
		switch(type){
			case TALONSRX:
				try{
					return talon.getSelectedSensorPosition();
				} catch (Exception e){
					System.out.println(e);
					System.out.println("There is no encoder present, you need to put one in");
				}
				
			case SPARKMAX:
				return sparkEncoder.getPosition();
		}
		return 0;
	} // get position

} // TorqueMotor 