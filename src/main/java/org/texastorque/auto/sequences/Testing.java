package org.texastorque.auto.sequences;

import java.util.ArrayList;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DrivePath;
import org.texastorque.auto.commands.MagazineSet;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class Testing extends Sequence {

    @Override
    protected void init() {
        ArrayList<Command> block1 = new ArrayList<>();
        Waypoint[] points = new Waypoint[] {
            new Waypoint(0,0,Pathfinder.d2r(0)),
            new Waypoint(-6,6,Pathfinder.d2r(0))
        };
        block1.add(new DrivePath(0, points, true));

        addBlock(block1);
    }
}
