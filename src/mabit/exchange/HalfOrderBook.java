package mabit.exchange;

import java.util.List;
import java.util.Map;

import mabit.oms.order.IInstrument;

public class HalfOrderBook {
	Map<IInstrument,List<SimOrder>> instrumentMap;
	Map<Long, SimOrder> liveOrders;
	Map<Long, SimOrder> deadOrders;
	
	

}
