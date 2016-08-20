package mabit.dispatcher;

public interface IEventListener {
	public static enum Priority {
		TOP,
		HIGH,
		NORMAL,
		LOW,
		LAST
	}
	
	public void onEvent(IEvent event);
	Priority getPriority();
}
