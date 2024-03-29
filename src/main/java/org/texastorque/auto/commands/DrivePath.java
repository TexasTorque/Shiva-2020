package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.constants.Constants;

import jaci.pathfinder.*;
import jaci.pathfinder.modifiers.TankModifier;
import jaci.pathfinder.followers.DistanceFollower;

public class DrivePath extends Command {

    private DistanceFollower leftFollower;
    private DistanceFollower rightFollower;
    private boolean forward;

    /**
     * Position is relative to initial Waypoint, heading angle is set to 0 in robotInit() and remains absolute
     * 
     * Resources
     * https://www.chiefdelphi.com/t/pathfinder-coordinate-system/159870
     * https://www.chiefdelphi.com/t/problems-with-pathfinder-motion-profiling/163830
     * https://www.chiefdelphi.com/t/tuning-pathfinder-pid-talon-motion-profiling-magic-etc/162516/4
     * https://www.thorlabs.com/tutorials.cfm?tabID=5dfca308-d07e-46c9-baa0-4defc5c40c3e
     */
    public DrivePath(double delay, Waypoint[] points, boolean forward) {
        super(delay);
        this.forward = forward;

        /**
         * Fit method: HERMITE_CUBIC or HERMITE_QUINTIC
         * Sample count: SAMPLES_HIGH (100000), SAMPLES_LOW (10000), SAMPLES_FAST (1000)
         * Time step (s)
         * Max velocity (ft/s)
         * Max Acceleration (ft/s/s) 
         * Max Jerk (ft/s/s/s)
         */
            Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 1.0/80, Constants.DB_MAX_SPEED, Constants.DB_MAX_ACCEL, Constants.DB_MAX_JERK);
            
            try{
                Trajectory path = Pathfinder.generate(points, config);
                TankModifier modifier = new TankModifier(path);
                modifier.modify(Constants.DB_WIDTH); // DriveBase width (ft)

                leftFollower = new DistanceFollower(modifier.getLeftTrajectory());
                rightFollower = new DistanceFollower(modifier.getRightTrajectory());
                leftFollower.configurePIDVA(0.8, 0.0, 0.0, 1/Constants.DB_MAX_SPEED, 0);
                rightFollower.configurePIDVA(0.8, 0.0, 0.0, 1/Constants.DB_MAX_SPEED, 0);
            }
            catch (Exception e){
                System.out.println("exception (swathi): " + e);
            }
    }

    @Override
    protected void init() {
        feedback.resetDriveEncoders();
        feedback.zeroYaw();
    }

	@Override
	protected void continuous() {
        // Heading values are absolute
        double currentHeading = -feedback.getYaw();
        double targetHeading = Pathfinder.r2d(leftFollower.getHeading());
        double angleDifference = Pathfinder.boundHalfDegrees(targetHeading - currentHeading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;

        double leftSpeed, rightSpeed;
        if (forward) {
            leftSpeed = leftFollower.calculate(feedback.getDBLeftDistance()) + turn;
            rightSpeed = rightFollower.calculate(feedback.getDBRightDistance()) - turn;
        }
        else {
            leftSpeed = -rightFollower.calculate(-feedback.getDBLeftDistance()) + turn;
            rightSpeed = -leftFollower.calculate(-feedback.getDBRightDistance()) - turn;
        }

        input.setDBLeftSpeed(leftSpeed);
        input.setDBRightSpeed(rightSpeed);
        // System.out.println("L: " + leftSpeed + " | R: " + rightSpeed);
	}

	@Override
	protected boolean endCondition() {
		return leftFollower.isFinished() || rightFollower.isFinished();
	}

	@Override
	protected void end() {
		input.setDBLeftSpeed(0);
        input.setDBRightSpeed(0);
	}
}