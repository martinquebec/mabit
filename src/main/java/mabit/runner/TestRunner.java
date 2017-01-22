package mabit.runner;

import mabit.data.marketdata.MarketDataService;
import mabit.data.marketdata.QuoteFileLoader;
import mabit.dispatcher.Event.QuoteEvent;
import mabit.dispatcher.SimulationDispatcher;
import mabit.exchange.ExchangeSimulator;
import mabit.oms.order.Oms;
import mabit.strategy.base.TestStrategy;
import mabit.time.ITimeManager;
import mabit.time.SimulationTimeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

class TestRunner {
	public static void main(String[] s) {
		SimulationDispatcher dispatcher = new SimulationDispatcher("MySimul",true);
		MarketDataService mds = new MarketDataService(dispatcher);
		ITimeManager tm = new SimulationTimeManager(dispatcher);
		ExchangeSimulator exchange = new ExchangeSimulator(dispatcher,tm,mds);
		Oms oms = new Oms(exchange, dispatcher, tm);
		TestStrategy strategy = new TestStrategy(oms,tm,dispatcher,mds,"Martin");
		File file = new File("/Users/martin/git/cloned/mabit/data/Match Odds.csv");
		try {
			List<QuoteEvent> quoteEvs = QuoteFileLoader.readFile(file);
			dispatcher.addEvents(quoteEvs);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()).execute(() ->Application.launch(example.class,s));
	//	Application.launch(example.class,s);
		dispatcher.consumeAllEvents();
		System.out.println("Done");

	}
}
