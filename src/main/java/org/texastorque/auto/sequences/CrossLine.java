package org.texastorque.auto.sequences;

// ====== imports =======
import org.texastorque.auto.Sequence;
import org.texastorque.auto.Command;
import org.texastorque.auto.commands.*;

import java.util.ArrayList;

// ================= Cross Line =====================
// crosses the line in auto, most basic autonomous
public class CrossLine extends Sequence{
    @Override
    protected void init(){
        ArrayList<Command> block1 = new ArrayList<>();
        block1.add(new DriveTime(0, 1,.5));
        addBlock(block1);
    }
} // cross line 
