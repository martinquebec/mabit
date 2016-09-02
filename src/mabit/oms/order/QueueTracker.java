package mabit.oms.order;

import mabit.data.marketdata.Quote;
import mabit.data.marketdata.Trade;

public class QueueTracker {
	final double price;
	final Side side;
	
	double queuePos;
	
	
	public QueueTracker(double price, Side side, double queuePos) {
		super();
		this.price = price;
		this.side = side;
		this.queuePos = queuePos;
	}
	
	public void onQuote(Quote quote) {
		if (!quote.isValid()) return;
		if (side.isMoreOrSameAggressiveThan(price, quote.getOppSide(side).get(0).getPrice())) {
			queuePos = -1e8;
		}
		queuePos = Math.min(queuePos, quote.getQtyAtMyLevel(side, price));
	}	
		
	
	public void onTrade(Trade trade) {
		if(side.isMoreOrSameAggressiveThan(price, trade.getPrice())) {
			queuePos -= trade.getQty();
		}
	}
	
	public double getQueuePosition() {
		return queuePos;
	}
	
	

}
