package mabit.dispatcher;

import javafx.event.EventHandler;
import mabit.gui.javafx.controller.IEventBusFX;
import mabit.gui.javafx.logpanel.FxLogEvent;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by martin on 9/9/2016.
 */
public class DispatcherFXBusBridge implements IDispatcherFxBusBridge,IEventListener, EventHandler<javafx.event.Event> {
    private static final Logger Log = Logger.getLogger(DispatcherFXBusBridge.class);

    final private IDispatcher myDispatcher;
    final private IEventBusFX fxDispatcher;
    final private Map<mabit.dispatcher.EventType, IConverter<IEvent, javafx.event.Event>> convertersFromMyDispatcher = new HashMap<>();
    final private Map<javafx.event.EventType, IConverter<javafx.event.Event, IEvent>> convertersFromEventBuxFX = new HashMap<>();
    final private Priority priority = Priority.LOW;

    public DispatcherFXBusBridge() {
       this(
               ServiceProvider.INSTANCE.getService(IDispatcher.class),
               ServiceProvider.INSTANCE.getService(IEventBusFX.class));
    }
    public DispatcherFXBusBridge(IDispatcher myDispatcher, IEventBusFX fxDispatcher) {
        this.myDispatcher = myDispatcher;
        this.fxDispatcher = fxDispatcher;
    }

    public void addConverterFromMyDispacher(mabit.dispatcher.EventType type, IConverter converter) {
        convertersFromMyDispatcher.put(type, converter);
        myDispatcher.register(type, this);

    }

    public void addConverterFromEventBus(javafx.event.EventType type, IConverter converter) {
        convertersFromEventBuxFX.put(type, converter);
        if(type == FxLogEvent.LOG_EVENT) {
            System.out.println("Sending logEvent");
        }
        fxDispatcher.addEventHandler(type, this);
    }

    @Override
    public void onEvent(IEvent event) {
        IConverter<IEvent, javafx.event.Event> converter = this.convertersFromMyDispatcher.get(event.getEventType());
        if (converter != null) {
//            Log.info("Converting " + event.toString());
            javafx.event.Event fxEvent = converter.convert(event);
            if (fxEvent != null) {
                fxDispatcher.fireEvent(fxEvent);
            } else {
 //               Log.trace("Null conversion for " + event.getEventType());
            }
        } else {
 //           Log.trace("No converter for " + event.getEventType());
        }
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void handle(javafx.event.Event fxevent) {
        IConverter<javafx.event.Event, IEvent> converter = this.convertersFromEventBuxFX.get(fxevent.getEventType());
        if (converter != null) {
            Log.info("Converting " + fxevent.toString());
            IEvent event = converter.convert(fxevent);
            if (event != null) {
                myDispatcher.put(event);
            } else {
                Log.trace("Null conversion for " + event.getEventType());
            }
        } else {
            Log.trace("No converter for " + fxevent.getEventType());
        }
    }

}



