package org.texastorque.auto.sequences;

// ====== imports =======
import org.texastorque.auto.Sequence;
import org.texastorque.auto.Command;
import org.texastorque.auto.commands.*;

import java.util.ArrayList;

// ================= Baseline =====================
// crosses the line in auto, most basic autonomous
public class Baseline extends Sequence{
    @Override
    protected void init(){
        ArrayList<Command> block1 = new ArrayList<>();
        block1.add(new DriveTime(0, 0.7,-0.3));
        addBlock(block1);
    }
} // cross line 
