package mabit.strategy.base;

import mabit.data.marketdata.MarketDataService;
import mabit.data.marketdata.Quote;
import mabit.dispatcher.*;
import mabit.oms.exchange.ExchangeName;
import mabit.oms.order.Oms;
import mabit.oms.order.OrderRequest;
import mabit.oms.order.Side;
import mabit.time.ITimeManager;

import static mabit.dispatcher.EventType.ORDER;

public class TestStrategy implements IEventListener {
	private final Oms oms;
	private final ITimeManager tm;
	private final Dispatcher dispatcher;
	private final String name;
	private final MarketDataService mds;

	private boolean orderSent = false;
	
	public TestStrategy(Oms oms, ITimeManager tm, Dispatcher dispatcher, MarketDataService mds, String name) {
		super();
		this.oms = oms;
		this.tm = tm;
		this.dispatcher = dispatcher;
		this.name = name;
		this.mds = mds;
		init();
	}
	
	private void init() {
		dispatcher.register(EventType.QUOTE, this);
		dispatcher.register(ORDER, this);
	}

	@Override
	public void onEvent(IEvent event) {

		switch (event.getEventType()) {
			case QUOTE:
				if(!orderSent) {
				orderSent =true;
					Quote quote = ((Event.QuoteEvent) event).getQuote();
					double buyPrice = quote.bPrice();
					double sellPrice = quote.bPrice();
					OrderRequest buyRequest = new OrderRequest(
							quote.getInstrument(),
							Side.BUY,
							null,
							ExchangeName.Betfair,
							1.0,
							buyPrice);
					OrderRequest sellRequest = new OrderRequest(
							quote.getInstrument(),
							Side.SELL,
							null,
							ExchangeName.Betfair,
							1.0,
							sellPrice);
					oms.sendOrder(buyRequest);
				}
				break;
			case ORDER:
				System.out.println("In Strategy: " + ((Event.OrderEvent)event).getOrderUpdate().toString());
				break;

		}
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}
	
}
