package org.texastorque.auto.sequences;

// ====== imports =======
import org.texastorque.auto.Sequence;
import org.texastorque.auto.Command;
import org.texastorque.auto.commands.*;
import org.texastorque.inputs.State;
import org.texastorque.inputs.State.RobotState;

import java.util.ArrayList;

// ================= Baseline =====================
// crosses the line in auto, most basic autonomous
public class PowerPort extends Sequence{
    //private volatile State state;

    @Override
    protected void init(){
        //state = State.getInstance();
        ArrayList<Command> block1 = new ArrayList<>();

        //state.setRobotState(RobotState.VISION);
        block1.add(new ShooterPercentSet(0, .8, 3,2));

        //block1.add(new MagazineSet(0, 0.5, true, 3.5, true, 3.5, true, 3.5));
        //block1.add(new DriveTime(0, 2, -.8));
        //block1.add(new AutoMag());
        //block1.add(new DriveTime(5, 2, .8));


        



        //block1.add(new ShooterSet(0, .8, 1, 2));
        //addBlock(block1);
        //addBlock(block1);
        addBlock(block1);
    }
}
