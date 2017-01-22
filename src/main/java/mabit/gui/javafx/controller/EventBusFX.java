package mabit.gui.javafx.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import mabit.dispatcher.IDispatcher;
import org.apache.log4j.Logger;


/**
 * Created by martin on 9/9/2016.
 */
public class EventBusFX implements IEventBusFX {
    private final static Logger Log = Logger.getLogger(EventBusFX.class);
    private Group group = new Group();
    private IDispatcher dispatcher;

    public EventBusFX() {
        Log.info("Creating EventBusFx");
    }

    @Override
    public void fireEvent(Event e) {
        group.fireEvent(e);

    }

    @Override
    public <T extends Event> void addEventHandler(EventType<T> type, EventHandler<T> handler) {
        group.addEventHandler(type,handler);

    }

    @Override
    public <T extends Event> void removeEventHandler(EventType<T> type, EventHandler<T> handler) {
        group.removeEventHandler(type, handler);

    }
}
