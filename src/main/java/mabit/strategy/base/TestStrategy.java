package mabit.strategy.base;

import mabit.data.marketdata.IMarketDataService;
import mabit.data.marketdata.Quote;
import mabit.dispatcher.*;
import mabit.oms.exchange.ExchangeName;
import mabit.oms.order.Oms;
import mabit.oms.order.OrderRequest;
import mabit.oms.order.Side;
import mabit.time.ITimeManager;
import org.apache.log4j.Logger;

import static mabit.dispatcher.EventType.ORDER;

public class TestStrategy implements IStrategyInterface,IEventListener {
	private static Logger Log = Logger.getLogger(TestStrategy.class);
	private final Oms oms;
	private final ITimeManager tm;
	private final IDispatcher dispatcher;
	private final String name;
	private final IMarketDataService mds;

	private boolean orderSent = false;
	public TestStrategy() {
		this(
				ServiceProvider.INSTANCE.getService(Oms.class),
				ServiceProvider.INSTANCE.getService(ITimeManager.class),
				ServiceProvider.INSTANCE.getService(IDispatcher.class),
				ServiceProvider.INSTANCE.getService(IMarketDataService.class),
				"Test Strategy");
	}
	public TestStrategy(Oms oms, ITimeManager tm, IDispatcher dispatcher, IMarketDataService mds, String name) {
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
					Log.info("Sending BUY order" + buyRequest.toString());
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
