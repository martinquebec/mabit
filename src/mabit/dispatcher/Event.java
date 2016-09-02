package mabit.dispatcher;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import mabit.data.marketdata.Quote;
import mabit.data.marketdata.Trade;
import mabit.oms.order.OrderUpdate;
import mabit.time.TimeUtils;

public class Event {

	protected static class GenericEvent<T> implements IEvent {
		DateTime timestamp;
		EventType eventType;
		T payload;


		protected GenericEvent(DateTime timestamp, EventType type, T payload) {
			this.timestamp = timestamp;
			this.eventType =type;
			this.payload = payload;
		}

		@Override
		public DateTime getDateTime() {
			return timestamp;
		}

		@Override
		public EventType getEventType() {
			// TODO Auto-generated method stub
			return this.eventType;
		}


		public T getPayLoad() {
			return payload;
		}
		
		public String toString() {
			return "[ " + TimeUtils.toFullDateTimeNoMillis(timestamp) + "\t" + payload.toString() + "]";
		}

	}

	public static class QuoteEvent extends GenericEvent<Quote> {
		public QuoteEvent(DateTime timestamp,Quote quote) { super(timestamp,EventType.QUOTE,quote); }
		public Quote getQuote() { return payload; }
	}


	public static class TradeEvent extends GenericEvent<Trade> {
		public TradeEvent(DateTime timestamp,Trade trade) { super(timestamp,EventType.TRADE,trade); }
		public Trade getTrade() { return payload; }
	}	


	public static class OrderEvent extends GenericEvent<OrderUpdate> {
		public OrderEvent(DateTime timestamp,OrderUpdate update) { super(timestamp,EventType.ORDER,update); }
		public OrderUpdate getOrderUpdate() { return payload; }
	}
	
	public static class TimeEvent extends GenericEvent<Runnable> {
		public TimeEvent(DateTime timestamp,Runnable runnable) { super(timestamp,EventType.TIME,runnable); }
		public void run() { payload.run(); }
	}
	
}
