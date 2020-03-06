package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;

import org.texastorque.constants.*;
import org.texastorque.subsystems.DriveBase;
import org.texastorque.subsystems.Subsystem;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Feedback {

  private static volatile Feedback instance;
  private static double targetArea;
  private static double hOffset;
  private static double vOffset;
  private static int count;

  private String sentColor;

  TCS34725_I2C colorSensor;
  // TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();

  ArrayList<String> colSeq = new ArrayList<>();
  String[] scripts = new String [] {"R","G","B","Y"};
  int chosIndex;
  int numOfColorCount = 0;
  private String targetColorString;

  private static DigitalInput magHighCheck;
  private static DigitalInput magLowCheck;

  public final double DISTANCE_PER_PULSE = Math.PI * Constants.WHEEL_DIAMETER / Constants.PULSES_PER_ROTATION;
  public final double ANGLE_PER_PULSE = 360.0 / Constants.PULSES_PER_ROTATION;
  public final double LF_FEET_CONVERSION = Math.PI * (1.0 / 20) / Constants.PULSES_PER_ROTATION;

  public final double ULTRA_CONVERSION = 1.0 / 84;

  private final AHRS NX_gyro;

  private NetworkTableInstance NT_instance;
  private NetworkTableEntry NT_offsetEntry;

  private Feedback(){
      magHighCheck = new DigitalInput(Ports.MAG_SENSOR_HIGH);
      magLowCheck = new DigitalInput(Ports.MAG_SENSOR_LOW);
      NX_gyro = new AHRS(SPI.Port.kMXP);
      colorSensor  = new TCS34725_I2C(false);
      try{
          colorSensor.enable();
      } catch (Exception e) {}
    } // constructor

  public void update(){
      updateLimelight();
      updateNavX();
      smartDashboard();
      updateMagazine();
      colorSensorUpdate();
      shouldGateRun();
      shouldRunToColor();
  } // update 

  // ==============Drive Train===============
    // list of variables DriveTrain
    private double leftTare = 0;
    private double rightTare = 0;
    private double leftPositionDT;
    private double rightPositionDT;
    private double leftVelocityDT;
    private double rightVelocityDT;

    // set methods DriveTrain
    public void setLeftPositionDT(double leftPositionDT) {
        this.leftPositionDT = leftPositionDT / Constants.TICKS_PER_FOOT_DB;
    }

    public void setRightPositionDT(double rightPositionDT) {
        this.rightPositionDT = rightPositionDT / Constants.TICKS_PER_FOOT_DB;
    }

    public void setLeftVelocityDT(double leftVelocityDT) {
        this.leftVelocityDT = leftVelocityDT;
    }

    public void setRightVelocityDT(double rightVelocityDT) {
        this.rightVelocityDT = rightVelocityDT;
    }


    // accessor methods DriveTrain
    public double getDBLeftDistance() {
        return -leftPositionDT + leftTare; 
    }

    public double getDBRightDistance() {
        return rightPositionDT - rightTare;
    }

    public double getLeftVelocityDT() {
        return leftVelocityDT;
    }

    public double getRightVelocityDT() {
        return rightVelocityDT;
    }

    public void resetDriveEncoders(){
        leftTare = leftPositionDT / Constants.TICKS_PER_FOOT_DB;
        rightTare = rightPositionDT / Constants.TICKS_PER_FOOT_DB;
    }


// ===========Intake=================
    // variables list Intake
    private double rotaryPosition_left;
    private double rotaryPosition_right;

    // set methods Intake
    public void setRotaryPositionLeft(double rotaryPosition_left) {
        this.rotaryPosition_left = rotaryPosition_left;
    }

    public void setRotaryPositionRight(double rotaryPosition_right) {
        this.rotaryPosition_right = rotaryPosition_right;
    }

    // accessor methods Intake
    public double getRotaryPositionLeft() {
        return rotaryPosition_left;
    }

    public double getRotaryPositionRight() {
        return rotaryPosition_right;
    }

    // =============Shooter (Hood and Flywheel)=================
    // variable list shooter
    private double shooterVelocity;
    private double hoodPosition;

    // set methods shooter
    public void setShooterVelocity(double shooterVelocity) {
        this.shooterVelocity = shooterVelocity;
    }

    public void setHoodPosition(double hoodPosition) {
        this.hoodPosition = hoodPosition;
    }

    // accessor methods shooter
    public double getShooterVelocity() {
        return shooterVelocity;
    }

    public double getHoodPosition() {
        return hoodPosition;
    }

    // ==========Magazine==========
    // this is where ultrasonic stuff would go once we add them, don't think we will
    // need any values from motors themselves

    private static boolean highMag = true;
    private static boolean lowMag = true;
    private static boolean highMagPast = false;

    private boolean ballLast;

    public void updateMagazine(){
        highMag = magHighCheck.get();
        lowMag = magLowCheck.get();

        if (ballLast != lowMag){
            if (!lowMag){
                count++;
            }
            ballLast = lowMag;
        } // should count how many balls have started to be read through the lower sensor 
        if (!highMagPast && !highMag){
            highMagPast = true;
        }
    }

    public static boolean getHighMagPast(){
        return highMagPast;
    }

    public static int getCount(){
        return count;
    }

    public void resetCount(){
        count = 0;
        highMagPast = false;
    }

    public static boolean getMagHigh() { // returns true for seeing ball
        return !highMag;
    }

    public static boolean getMagLow(){ // returns true for seeing a ball
        return !lowMag;
    }
    // ======== limelight ========

    public void updateLimelight(){
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
        targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        hOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        vOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }

    public static double getXOffset(){
        return hOffset;
    }

    public static double getYOffset(){
        return vOffset;
    }

    public static double getDistanceAway(){
        return Math.abs(7.56*vOffset + 56.866);
    }

    // ========== Gyro ==========

    private double NX_pitch;
    private double NX_yaw;
    private double NX_roll;

    public void resetNavX() {
        NX_gyro.reset();
    }

    public void updateNavX() {
        NX_pitch = NX_gyro.getPitch();
        NX_yaw = NX_gyro.getYaw();
        NX_roll = NX_gyro.getRoll();
    }

    public double getPitch() {
        return NX_pitch;
    }

    public double getYaw() {
        return NX_yaw;
    }

    public double getRoll() {
        return NX_roll;
    }

    public void zeroYaw() {
        NX_gyro.zeroYaw();
    }

  //================== Color Sensor Stuff ================
  public void colorSensorUpdate(){    
          try{
              var values = colorSensor.getRawData();
              if(values.getR() >= 10200 & values.getR() < 10400 &
                  values.getB() <= 6000 & values.getB() >= 5000 & 
                  values.getG() > 10000 & values.getG() < 10400){
                SmartDashboard.putString("color", "yellow");
                colSeq.add("Y");
                chosIndex = 0;
              }
              else if(values.getR() >= 1300 & values.getR() <= 2000 & 
                      values.getB() <= 1000 & values.getB() > 0 &
                      values.getG() <= 1000 & values.getG() > 0){
                SmartDashboard.putString("color", "red");
                colSeq.add("R");
                chosIndex = 1;
              }
              else if(values.getR() >= 800 & values.getR() <= 1500 & 
                      values.getB() >= 500 & values.getB() <= 1000 & 
                      values.getG() >= 800 & values.getG() <= 1500){
                SmartDashboard.putString("color", "green");
                colSeq.add("G");
                chosIndex = 2;
              }
              else if(values.getR() >= 900 & values.getR() <= 2000 & 
                      values.getB() >= 1000 & values.getB() <= 2000 & 
                      values.getG() >= 900 & values.getG() <= 1800){
                SmartDashboard.putString("color", "blue");
                colSeq.add("B");
                chosIndex = 3;
              }
              else{
                SmartDashboard.putString("color", "stop it rip");
              }
              
              System.out.println(colorSensor.getRawData());
            }
            catch(Exception e){}
      }

  public boolean shouldRunToColor(){
    sentColor = DriverStation.getInstance().getGameSpecificMessage();
    if(sentColor.length() > 0)
    {
      switch (sentColor.charAt(0))
      {
        case 'B' :
          if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
            return true;
          }
          break;
        case 'G' :
          if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
            return true;
          }
          break;
        case 'R' :
          if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
            return true;
          }
          break;
        case 'Y' :
          if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
            return true;
          }
          break;
        default :
          return false;
      }
    }
    return false;
  }

  public boolean shouldGateRun(){
    if(Input.colorIsUp){
      if((colSeq.get(colSeq.size()-1)).equals(scripts[chosIndex])){
        numOfColorCount++;
        while(numOfColorCount <= 35){
          return true;
        }
      }
    }
    return false;
  }

  // ======== Other stuff =========
  public void smartDashboard(){
    SmartDashboard.putNumber("hOffset", hOffset);
    SmartDashboard.putNumber("rotaryLeft_position", rotaryPosition_left);
    SmartDashboard.putNumber("rotaryRight_position", rotaryPosition_right);
    SmartDashboard.putBoolean("magcheckHigh", magHighCheck.get());
    SmartDashboard.putBoolean("magcheckLow", magLowCheck.get());
    SmartDashboard.putNumber("NumOfColorCount", numOfColorCount);
    SmartDashboard.putString("SentColor", sentColor);
  } // stuff to put in smart dashboard

  public static Feedback getInstance() {
      if (instance == null){ 
          synchronized (Feedback.class){
              if (instance == null)
                  instance = new Feedback();
          }
      }
      return instance;
  } // getInstance 
  } // Feedback