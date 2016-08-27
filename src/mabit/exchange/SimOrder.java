package mabit.exchange;

import mabit.marketdata.Quote;
import mabit.marketdata.Trade;
import mabit.oms.order.Exec;
import mabit.oms.order.Order;
import mabit.oms.order.OrderRequest;
import mabit.oms.order.OrderState;
import mabit.oms.order.QueueTracker;
import mabit.oms.order.Side;
import mabit.utils.XNumber;

public class SimOrder {

	private final Order order;
	private double execAmount;
	private OrderState ordStatus;
	final private Side side;
	final double price;
	final QueueTracker tracker;	
	double currentQueuePos;
	

	public SimOrder(Order order,Quote quote) {
		this(order,quote,0);
	}
	public SimOrder(Order order, Quote quote, int execAmount) {
		this.order = order;
		this.execAmount = execAmount;
		this.ordStatus = OrderState.ON_MARKET;
		this.side = order.getSide();
		this.price = order.getPrice();
		this.tracker = new QueueTracker(order.getPrice(), side, quote.getQtyAtMyLevel(side, price));
		currentQueuePos = tracker.getQueuePosition();
	}
	
	public Exec onQuote(Quote quote) {
		tracker.onQuote(quote);
		return updateAndCheckForExec();
	}
	
	public Exec onTrade(Trade trade) {
		tracker.onTrade(trade);
		return updateAndCheckForExec();
	}

	private Exec updateAndCheckForExec() {
		double newQueuePos = tracker.getQueuePosition();
		double executionQty = XNumber.minMax(0, order.getQty(), tracker.getQueuePosition() - newQueuePos);
		if(executionQty>0) {
			currentQueuePos = newQueuePos;
			execAmount += executionQty;	
			if(execAmount == order.getQty()) ordStatus = OrderState.FILLED;
			return new Exec(order, newQueuePos, price);
		}
		return null;
	}
	
	
	public boolean isLive() {
		return !ordStatus.isTerminal();
	}
	public Order getOrder() {
		return order;
	}
	public double getExecAmount() {
		return execAmount;
	}
	public OrderState getOrdStatus() {
		return ordStatus;
	}
	public Side getSide() {
		return side;
	}
	public double getPrice() {
		return price;
	}
	public QueueTracker getTracker() {
		return tracker;
	}
	public double getCurrentQueuePos() {
		return currentQueuePos;
	}
	
	public void setOrderStatus(OrderState orderState) {
		
	}
	
	
	

}