package mabit.gui.javafx.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

/**
 * Created by martin on 8/9/2016.
 */
public interface IEventBusFX {
    void fireEvent(Event e);
    <T extends Event>  void addEventHandler(EventType<T> type,EventHandler<T> handler);
    <T extends Event>  void removeEventHandler(EventType<T> type ,EventHandler<T> handler);

}
