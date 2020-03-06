package org.texastorque.inputs;

public class State{
    private static volatile State instance;

    public enum RobotState {
        AUTO, TELEOP, VISION, SHOOTING, MAGLOAD;
    } // the different states that the robot can be in

    private RobotState robotState = RobotState.TELEOP;

    public RobotState getRobotState(){
        return robotState;
    } // returns the state the robot is in

    public void setRobotState(RobotState state) {
        synchronized (this){
            this.robotState = state;
        }
    } // set the robot state to what is passed to it

    public static State getInstance(){
        if (instance == null) {
            synchronized (State.class){
                if (instance == null)
                    instance = new State();
            }
        }
        return instance;
    }
}