package mabit.time;

import mabit.dispatcher.Event.TimeEvent;
import mabit.dispatcher.EventType;
import mabit.dispatcher.IEvent;
import mabit.dispatcher.IEventListener;

public class TimeEventRunner implements IEventListener {

	@Override
	public void onEvent(IEvent event) {
		if(event.getEventType() == EventType.TIME) {
			((TimeEvent)event).run();
		}
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

}
