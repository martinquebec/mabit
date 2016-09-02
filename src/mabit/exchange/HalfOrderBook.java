package mabit.exchange;

import java.util.List;
import java.util.Map;

import mabit.data.instruments.IInstrument;

public class HalfOrderBook {
	Map<IInstrument,List<SimOrder>> instrumentMap;
	Map<Long, SimOrder> liveOrders;
	Map<Long, SimOrder> deadOrders;
	
	

}
