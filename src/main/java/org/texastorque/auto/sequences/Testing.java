package org.texastorque.auto.sequences;

import java.util.ArrayList;

// import org.texastorque.auto.Command;
// import org.texastorque.auto.Sequence;
// import org.texastorque.auto.commands.DrivePath;
// import org.texastorque.auto.commands.MagazineSet;
// import org.texastorque.auto.commands.ShooterSet;

// import jaci.pathfinder.Pathfinder;
// import jaci.pathfinder.Waypoint;
// import org.texastorque.auto.commands.IntakeSet;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DriveDistance;
import org.texastorque.auto.commands.DrivePath;
import org.texastorque.auto.commands.DriveTime;
import org.texastorque.auto.commands.IntakeSet;
import org.texastorque.auto.commands.AutoMag;
import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.commands.ShooterPercentSet;
import org.texastorque.auto.commands.VisionShoot;
import org.texastorque.inputs.State;
import org.texastorque.inputs.State.RobotState;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class Testing extends Sequence {
    //private volatile State state;
    @Override
    protected void init() {
        //state = State.getInstance();
        //ArrayList<Command> block1 = new ArrayList<>();
        // Waypoint[] points = new Waypoint[] {
        //     new Waypoint(0,0,0),
        //     new Waypoint(0,-3,0)
        // };
        //block1.add(new DrivePath(0, points, true));

        //addBlock(block1);

        // ArrayList<Command> block2 = new ArrayList<>();
        // block2.add(new IntakeSet(0, 1));

        // block2.add(new DriveDistance(0, 150));
        // block2.add(new MagAutoLoad());

        // //addBlock(block1);
        // addBlock(block2);

        ArrayList<Command> block1 = new ArrayList<>();
        block1.add(new ShooterPercentSet(0, .8, 5,3));
        //block1.add(new VisionShoot(0, 5, .8, 3));
        //state.setRobotState(RobotState.VISION);

        block1.add(new MagazineSet(1.5, 0.5, true, 3.5, true, 3.5, true, 3.5));

        ArrayList<Command> block2 = new ArrayList<>();
        //block2.add(new DriveTime(0, .8, -.8));

        block2.add(new DriveDistance(0, -200));
        block2.add(new AutoMag());
        //block2.add(new DriveTime(5, .8, .8));
        block2.add(new DriveDistance(10, 50));
        ArrayList<Command> block3 = new ArrayList<>();
        block3.add(new ShooterPercentSet(0, .8, 5,3));
        //block1.add(new VisionShoot(0, 5, .8, 3));
        //state.setRobotState(RobotState.VISION);

        block3.add(new MagazineSet(1.5, 0.5, true, 3.5, true, 3.5, true, 3.5));

        



        //block1.add(new ShooterSet(0, .8, 1, 2));
        //addBlock(block1);
        //addBlock(block1);
        addBlock(block1);
        addBlock(block2);
        addBlock(block3);
        //addBlock(block1);

    }
}
