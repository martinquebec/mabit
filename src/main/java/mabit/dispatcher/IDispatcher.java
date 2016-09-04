package mabit.dispatcher;

public interface IDispatcher {

	void register(EventType type, IEventListener listener,int priority) ;
	void unregister(EventType type, IEventListener listener);
	void post(IEvent event);
}
