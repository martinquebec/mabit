package mabit.dispatcher;

@SuppressWarnings("ALL")
public interface IDispatcher {
	enum State {
		STOP,
		RUN,
		ONEEVENT,
		TERMINATED
	}

	void register(EventType type, IEventListener listener) ;
	void unregister(EventType type, IEventListener listener);
	void put(IEvent event);
	void changeState(State state, String message);

}
