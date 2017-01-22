package mabit.gui.javafx.logpanel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import mabit.dispatcher.ServiceProvider;
import mabit.gui.javafx.controller.IEventBusFX;
import org.apache.log4j.Logger;


/**
 * Created by martin on 15/9/2016.
 */
public class LogPanelControler implements EventHandler<FxLogEvent> {
    private static final Logger Log = Logger.getLogger(LogPanelControler.class);
    final ObservableList<LogData> data;

    public LogPanelControler(ObservableList<LogData> data) {
        this.data = data;
        init();
    }

    public LogPanelControler() {
        this(FXCollections.observableArrayList());
    }

    private void init() {
        IEventBusFX eventBus = ServiceProvider.INSTANCE.getService(IEventBusFX.class);
        eventBus.addEventHandler(FxLogEvent.LOG_EVENT, this);
    }

    @Override
    public void handle(FxLogEvent event) {
        System.out.println("Received FX log event");
        LogData logData =new LogData(event.getDt(), event.getLog4jEvent().getLevel().toString(), event.getLog4jEvent().getRenderedMessage());
        Platform.runLater(()-> data.add(logData));
    }
}
