package mabit.dispatcher;

/**
 * Created by martin on 5/9/2016.
 */



public class DispatcherBridge implements IEventListener {
    final private Dispatcher dispatcherFrom, dispatcherTo;
    final private IEventConverter converter;
    final private Priority priority;

    public DispatcherBridge(Dispatcher dispatcherFrom, Dispatcher dispatcherTo, IEventConverter converter, Priority priority) {
        this.dispatcherFrom = dispatcherFrom;
        this.dispatcherFrom.register(converter.getFromType(),this);
        this.dispatcherTo = dispatcherTo;
        this.converter = converter;
        this.priority = priority;
    }

    @Override
    public void onEvent(IEvent event) {
        dispatcherFrom.put(converter.convert(event));
    }

    @Override
    public Priority getPriority() {
        return priority;
    }
}
