package mabit.runner;

import mabit.data.marketdata.IMarketDataService;
import mabit.data.marketdata.MarketDataService;
import mabit.data.marketdata.QuoteFileLoader;
import mabit.dispatcher.*;
import mabit.dispatcher.Event.QuoteEvent;
import mabit.exchange.ExchangeSimulator;
import mabit.exchange.IExchangeInterface;
import mabit.gui.javafx.logpanel.FxLogEvent;
import mabit.gui.javafx.orderpanel2.FxOrderEvent;
import mabit.gui.javafx.quotepanel.FxQuoteEvent;
import mabit.oms.order.Oms;
import mabit.strategy.base.IStrategyInterface;
import mabit.strategy.base.TestStrategy;
import mabit.time.ITimeManager;
import mabit.time.SimulationTimeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Executors;

public class Simulation {
    SimulationDispatcher dispatcher;
    IMarketDataService mds;
    ITimeManager tm;
    IExchangeInterface exchange;
    Oms oms;
    IStrategyInterface strategy;
    //	new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()).execute(() ->Application.launch(example.class,s));
    //	Application.launch(example.class,s);


    public Simulation(boolean b) {
        this.dispatcher = new SimulationDispatcher("MySimul", true);   //done
        this.mds = new MarketDataService(dispatcher); //done
        this.tm = new SimulationTimeManager(dispatcher); //done
        this.exchange = new ExchangeSimulator(dispatcher, tm, mds); //done
        this.oms = new Oms(exchange, dispatcher, tm);// done
        this.strategy = new TestStrategy(oms, tm, dispatcher, mds, "Martin");
        File file = new File("/Users/martin/git/cloned/mabit/data/Match Odds.csv");
        try {
            List<QuoteEvent> quoteEvs = QuoteFileLoader.readFile(file);
            //	dispatcher.addEvents(quoteEvs);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        addRepublishers();
    }

    public Simulation() {
        this.dispatcher = (SimulationDispatcher) ServiceProvider.INSTANCE.getService(IDispatcher.class);
        this.mds = ServiceProvider.INSTANCE.getService(IMarketDataService.class);
        this.tm = ServiceProvider.INSTANCE.getService(ITimeManager.class);
        this.exchange = ServiceProvider.INSTANCE.getService(IExchangeInterface.class);
        addRepublishers();
    }

    private void addRepublishers() {
        IDispatcherFxBusBridge bridge = ServiceProvider.INSTANCE.getService(IDispatcherFxBusBridge.class);
        bridge.addConverterFromMyDispacher(EventType.LOG, new IConverter<Event.LogEvent, FxLogEvent>() {
            @Override
            public FxLogEvent convert(Event.LogEvent e) {
                System.out.println("Convert to LogEvent");
                return new FxLogEvent(e.getDateTime(), e.getLog4jEvent());
            }
        });

        bridge.addConverterFromMyDispacher(EventType.QUOTE, new IConverter<Event.QuoteEvent, FxQuoteEvent>() {
            @Override
            public FxQuoteEvent convert(Event.QuoteEvent e) {
                return new FxQuoteEvent(e.getQuote(), e.getDateTime());
            }
        });       //TODO: we should public event instead of doing direct call

        bridge.addConverterFromMyDispacher(EventType.ORDER, new IConverter<Event.OrderEvent, FxOrderEvent>() {
            @Override
            public FxOrderEvent convert(Event.OrderEvent orderEvent) {
                return new FxOrderEvent(
                        orderEvent.getOrderUpdate().getOrder(),
                        orderEvent.getOrderUpdate().getMessage(),
                        orderEvent.getDateTime());
            }
        });


//        IDispatcherFxBusBridge bridge = ServiceProvider.INSTANCE.getService(IDispatcherFxBusBridge.class);
//        bridge.addConverterFromMyDispacher(EventType.LOG, new IConverter<Event.LogEvent, FxLogEvent>() {
//            @Override
//            public FxLogEvent convert(Event.LogEvent e) {
//                System.out.println("Received LogEvent");
//                return new FxLogEvent(e.getDateTime(), e.getLog4jEvent());
//            }
//        });
        this.strategy = ServiceProvider.INSTANCE.getService(IStrategyInterface.class);
    }

    public void pushTestMarketData() {
        this.pushMarketData(new File("/Users/martin/git/cloned/mabit/data/Match Odds.csv"));
    }

    public void launch() {
        Executors.newScheduledThreadPool(1).execute(() -> this.dispatcher.consumeAllEvents());

    }

    public void pushMarketData(File file) {
        try {
            List<QuoteEvent> quoteEvs = QuoteFileLoader.readFile(file);
            dispatcher.addEvents(quoteEvs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startMainLoop() {
        //TODO use start/stop/pause states instead
        ((SimulationDispatcher) dispatcher).consumeAllEvents();
    }

    public IDispatcher getDispatcher() {
        return dispatcher;
    }

    public static void main(String[] s) {
        Simulation simulation = new Simulation();
        simulation.startMainLoop();
        simulation.getDispatcher().changeState(SimulationDispatcher.State.RUN, "MAIN");
    }
}
