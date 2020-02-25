package org.texastorque.inputs;
import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.subsystems.DriveBase;
import org.texastorque.subsystems.Subsystem;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;
import org.texastorque.inputs.Input;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Feedback {
    //Feedback is still used as the hub for all encoders... but now they need to be updated through subsystems 
    private static volatile Feedback instance;
    private static double targetArea;
    private static double hOffset;
    private static double vOffset;

    //mag infared sensors
    private static DigitalInput magHighCheck;
    private static DigitalInput magLowCheck;

    // Conversions
    public final double DISTANCE_PER_PULSE = Math.PI * Constants.WHEEL_DIAMETER / Constants.PULSES_PER_ROTATION;
    public final double ANGLE_PER_PULSE = 360.0 / Constants.PULSES_PER_ROTATION;
    public final double LF_FEET_CONVERSION = Math.PI * (1.0 / 20) / Constants.PULSES_PER_ROTATION; // Using approximate
                                                                                                   // shaft diameter
    public final double ULTRA_CONVERSION = 1.0 / 84;

private String targetColorString;

    // NetworkTables
    private NetworkTableInstance NT_instance;
    private NetworkTableEntry NT_offsetEntry;

    // Analog Input
    // private AnalogInput mag_ultra = new AnalogInput(0);

    // private Subsystem drivebase = DriveBase.getInstance();

    private Feedback() {
        magHighCheck = new DigitalInput(Ports.MAG_SENSOR_HIGH);
        magLowCheck = new DigitalInput(Ports.MAG_SENSOR_LOW);
        NX_gyro = new AHRS(SPI.Port.kMXP);
    } // constructor

    public void update() {
        updateLimelight();
        updateNavX();
        smartDashboard();
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
    public static boolean getMagHigh() {
        return magHighCheck.get();
    }

    public static boolean getMagLow(){
        return magHighCheck.get();
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

    // ======== Other stuff =========
    
    public void smartDashboard(){
        SmartDashboard.putNumber("hOffset", hOffset);
        SmartDashboard.putNumber("rotaryLeft_position", rotaryPosition_left);
        SmartDashboard.putNumber("rotaryRight_position", rotaryPosition_right);
        SmartDashboard.putBoolean("magcheckHigh", magHighCheck.get());
        SmartDashboard.putBoolean("magcheckLow", magLowCheck.get());
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