package org.texastorque.auto.sequences;

import java.util.ArrayList;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DriveTime;
import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.commands.ShooterPercentSet;

public class ShootButWorse extends Sequence{

	@Override
	protected void init() {
        ArrayList<Command> block1 = new ArrayList<>();
        block1.add(new ShooterPercentSet(0, 0.72, 5, 3));
        block1.add(new MagazineSet(1.5, 0.5, true, 3.5, true, 3.5, true, 3.5));
        
        ArrayList<Command> block2 = new ArrayList<>();
        block2.add(new DriveTime(0,0.7, -0.3));

        addBlock(block1);
        addBlock(block2);
	}
}
