package mabit.oms.position;

import java.util.IdentityHashMap;
import java.util.Map;

import javax.swing.text.Position;

import mabit.data.instruments.IInstrument;
import mabit.oms.order.Exec;

public class PositionManager {
	Map<IInstrument,Position> positions = new IdentityHashMap<IInstrument, Position>();

	public void addExec(Exec exec) {
		
	}
	
	
}
