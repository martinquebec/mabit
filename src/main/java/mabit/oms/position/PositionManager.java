package mabit.oms.position;

import mabit.data.instruments.IInstrument;
import mabit.dispatcher.Event;
import mabit.dispatcher.IDispatcher;
import mabit.dispatcher.ServiceProvider;
import mabit.oms.order.Exec;
import mabit.oms.order.Order;
import mabit.time.ITimeManager;
import org.apache.log4j.Logger;

import java.util.IdentityHashMap;
import java.util.Map;


class PositionManager implements IPositionManager {
	final IDispatcher dispatcher;
	final ITimeManager tm;
	Logger Log = Logger.getLogger(PositionManager.class);
	Map<IInstrument,IPositionManager> positions = new IdentityHashMap<>();

	public PositionManager() {
		dispatcher = ServiceProvider.INSTANCE.getService(IDispatcher.class);
		tm = ServiceProvider.INSTANCE.getService(ITimeManager.class);
	}
	@Override
	public void addExec(Exec exec) {
				IPositionManager pos = positions.get(exec.getOrder().getIntrument());
			if(pos==null) {
				Log.error("Cannot add Exec, no position for instrument " + exec.getOrder().getIntrument().getName());
			} else {
			pos.addExec(exec);
		}
		
	}

	@Override
	public void newOrder(Order order) {
		IPositionManager pos = positions.get(order.getIntrument());
		if(pos==null) {
			pos = new BasicPositon(order.getIntrument(),0,0);
			positions.put(order.getIntrument(),pos);
		}
		pos.newOrder(order);
	}

	@Override
	public void cancelOrder(Order order) {
			IPositionManager pos = positions.get(order.getIntrument());
			if(pos==null) {
				Log.error("Cannot cancel order, no position for instrument " + order.getIntrument().getName());
			} else {
				pos.cancelOrder(order);
			}
	}

	private void publishPosition(IPosition pos) {
		if((tm!=null) && (dispatcher!=null)) {
			dispatcher.put(new Event.PositionEvent(tm.getTime(), pos));
		}

	}
}
