package mabit.data.marketdata;

import com.google.common.collect.Maps;
import mabit.data.instruments.IInstrument;
import mabit.dispatcher.*;
import mabit.dispatcher.Event.QuoteEvent;

import java.util.Map;

public class MarketDataService implements IMarketDataService,IEventListener {
	private final Map<IInstrument, Quote> cache = Maps.newIdentityHashMap();

	public MarketDataService() {
		this(ServiceProvider.INSTANCE.getService(IDispatcher.class));
	}

	public MarketDataService(IDispatcher dispatcher) {
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
