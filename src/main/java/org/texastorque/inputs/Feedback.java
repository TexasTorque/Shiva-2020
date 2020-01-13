package org.texastorque.inputs;

import org.texastorque.constants.*;

public class Feedback {

    private static volatile Feedback instance;

    private Feedback(){

    } // constructor

    public void update(){

    } // update 

    public void smartDashboard(){

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
