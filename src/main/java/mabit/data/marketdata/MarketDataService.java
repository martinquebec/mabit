package mabit.data.marketdata;

import java.util.Map;

import com.google.common.collect.Maps;

import mabit.data.instruments.IInstrument;
import mabit.dispatcher.Dispatcher;
import mabit.dispatcher.Event.QuoteEvent;
import mabit.dispatcher.EventType;
import mabit.dispatcher.IEvent;
import mabit.dispatcher.IEventListener;

public class MarketDataService implements IEventListener {
	private final Map<IInstrument, Quote> cache = Maps.newIdentityHashMap();
	
	public MarketDataService(Dispatcher dispatcher) {
		dispatcher.register(EventType.QUOTE, this);
	}
	
	public Quote getLastQuote(IInstrument instrument) {
		return cache.get(instrument);
	}

	@Override
	public void onEvent(IEvent event) {
		if(event.getEventType()==EventType.QUOTE) {
			QuoteEvent quoteEv = (QuoteEvent)event;
			cache.put(quoteEv.getQuote().getInstrument(),quoteEv.getQuote());
		}
		
	}

	@Override
	public Priority getPriority() {
		return Priority.TOP;
	}

}
