package mabit.dispatcher;

/**
 * Created by martin on 5/9/2016.
 */
public interface IEventConverter {
    IEvent convert(IEvent event);
    EventType getFromType();
    EventType getToType();

}
