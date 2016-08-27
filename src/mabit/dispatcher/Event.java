package mabit.dispatcher;

import org.joda.time.DateTime;

import mabit.exchange.OrderUpdate;
import mabit.marketdata.Quote;
import mabit.marketdata.Trade;
import mabit.oms.order.Exec;

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
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EventType getEventType() {
			// TODO Auto-generated method stub
			return null;
		}


		public T getPayLoad() {
			return payload;
		}
	}

	public static class QuoteEvent extends GenericEvent<Quote> {
		public QuoteEvent(DateTime timestamp,Quote quote) { super(timestamp,EventType.QUOTE,quote); }
	}


	public static class TradeEvent extends GenericEvent<Trade> {
		public TradeEvent(DateTime timestamp,Trade trade) { super(timestamp,EventType.TRADE,trade); }
	}	

	public static class AckEvent extends GenericEvent<OrderUpdate> {
		public AckEvent(DateTime timestamp,OrderUpdate ack) { super(timestamp,EventType.ACKNAK,ack); }
	}	

	public static class ExecEvent extends GenericEvent<Exec> {
		public ExecEvent(DateTime timestamp,Exec exec) { super(timestamp,EventType.EXEC,exec); }
	}
}
