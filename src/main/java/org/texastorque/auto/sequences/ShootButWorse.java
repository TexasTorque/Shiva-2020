package org.texastorque.auto.sequences;

import java.util.ArrayList;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DriveTime;
import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.commands.ShooterPercentSet;
import org.texastorque.auto.commands.ShooterSet;

public class ShootButWorse extends Sequence{

	@Override
	protected void init() {

        //ArrayList<Command> block0 = new ArrayList<>();
        //block0.add(new ShooterPercentSet(0, 1, 5, 2));
        
        ArrayList<Command> block1 = new ArrayList<>();
        //block1.add(new ShooterPercentSet(0, 1, 5, 2));
        //block1.add(new ShooterPercentSet(0, .8, 10, 2));
        block1.add(new ShooterPercentSet(0, 0.80, 5, 3));
        block1.add(new MagazineSet(1.5, 0.5, true, 3.5, true, 3.5, true, 3.5));
        
        ArrayList<Command> block2 = new ArrayList<>();
        block2.add(new DriveTime(0,0.7, -0.3));
        //block2.add(new ShooterSet(.5, .8, 1, 2));
        addBlock(block1);
        addBlock(block2);
	}
}
