package mabit.dispatcher;

/**
 * Created by martin on 10/9/2016.
 */
public interface IDispatcherFxBusBridge {

    void addConverterFromMyDispacher(mabit.dispatcher.EventType type, IConverter converter);
    void addConverterFromEventBus(javafx.event.EventType type, IConverter converter);
}
