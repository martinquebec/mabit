package mabit.dispatcher;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
	Map<EventType,List<IEventListener>> listenersByTypes;
	ThreadPoolExecutor exec;
		
	public Dispatcher() {
		listenersByTypes = new IdentityHashMap<EventType,List<IEventListener>>();
		exec = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public void register(EventType type, IEventListener listener,int priority) {
		//TODO: priority
		List<IEventListener> listeners = listenersByTypes.get(type);
		if(listeners==null) {
			listeners = new LinkedList<IEventListener>();
			listenersByTypes.put(type, listeners);
		}
		listeners.add(listener);
		listeners.sort((l1,l2) -> {return l1.getPriority().compareTo(l2.getPriority()); });
		
	}
	
	public void post(IEvent event) {
		//TODO change to use lambda
		List<IEventListener> listeners = listenersByTypes.get(event.getEventType());
		if(listeners!=null && listeners.size() > 0) {
			exec.execute(new Runnable() {
				@Override
				public void run() {
					for(IEventListener listener : listeners) listener.onEvent(event); 
				}
			});
		}
	}
}
