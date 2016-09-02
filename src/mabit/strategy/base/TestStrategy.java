package mabit.strategy.base;

import mabit.dispatcher.Dispatcher;
import mabit.dispatcher.EventType;
import mabit.dispatcher.IEvent;
import mabit.dispatcher.IEventListener;
import mabit.oms.order.Oms;
import mabit.time.ITimeManager;

public class TestStrategy implements IEventListener {
	private final Oms oms;
	private final ITimeManager tm;
	private final Dispatcher dispatcher;
	private final String name;
	
	public TestStrategy(Oms oms, ITimeManager tm, Dispatcher dispatcher, String name) {
		super();
		this.oms = oms;
		this.tm = tm;
		this.dispatcher = dispatcher;
		this.name = name;
		init();
	}
	
	public void init() {
		dispatcher.register(EventType.QUOTE, this);
		dispatcher.register(EventType.QUOTE, this);
	}

	@Override
	public void onEvent(IEvent event) {
		System.out.println(event.toString());
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}
	
}
