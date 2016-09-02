package mabit.dispatcher;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
	Map<EventType,List<IEventListener>> listenersByTypes;
	final ExecutorService exec;
		
	public Dispatcher(boolean sameThread) {
		listenersByTypes = new IdentityHashMap<EventType,List<IEventListener>>();
		this.exec = (sameThread) ? null : new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
		public void register(EventType type, IEventListener listener) {
		List<IEventListener> listeners = listenersByTypes.get(type);
		if(listeners==null) {
			listeners = new LinkedList<IEventListener>();
			listenersByTypes.put(type, listeners);
		}
		listeners.add(listener);
		listeners.sort((l1,l2) -> {return l1.getPriority().compareTo(l2.getPriority()); });
	}
	
	public void unregister(EventType type, IEventListener listener) {
		List<IEventListener> listeners = listenersByTypes.get(type);	
		if(listeners !=null && listeners.size() >0) {
			listeners.remove(listener);
		}
	}
	
	public void post(IEvent event) {
		//TODO change to use lambda
		List<IEventListener> listeners = listenersByTypes.get(event.getEventType());
		if(listeners!=null && listeners.size() > 0) {
			if(exec!=null) {
				exec.execute(()-> {for(IEventListener listener : listeners) listener.onEvent(event);});
			} else {
				for(IEventListener listener : listeners) 
					listener.onEvent(event);
			}
			
	
		}
	}
}
