package mabit.dispatcher;

public interface IDispatcher {

	public void register(EventType type, IEventListener listener,int priority) ;
	public void unregister(EventType type, IEventListener listener);
	public void post(IEvent event);
}
