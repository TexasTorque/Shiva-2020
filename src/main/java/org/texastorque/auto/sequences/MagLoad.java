package org.texastorque.auto.sequences;

import java.util.ArrayList;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.AutoMag;
import org.texastorque.auto.commands.MagAutoLoad;

public class MagLoad extends Sequence {

    @Override
    protected void init() {
        ArrayList<Command> block1 = new ArrayList<>();
        block1.add(new AutoMag());
        addBlock(block1);
    }
}
