package mabit.gui.javafx.simulpanel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mabit.dispatcher.IDispatcher;
import mabit.dispatcher.ServiceProvider;
import mabit.dispatcher.SimulationDispatcher;
import mabit.gui.javafx.controller.IEventBusFX;
import mabit.gui.javafx.quotepanel.FxQuoteEvent;
import mabit.time.TimeUtils;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by martin on 6/9/2016.
 */
public class SimulPaneController implements Initializable, EventHandler<FxQuoteEvent> {
    IDispatcher dispatcher;
    DateTime lastQuoteUpdate = null;
    SimpleStringProperty timeProp = new SimpleStringProperty();

    @FXML
    Button stopButt;
    @FXML
    Button quitButt;
    @FXML
    Button runButt;
    @FXML
    Label timeLabel;
    @FXML
    Button oneEventButt;


    public SimulPaneController() {
        System.out.println("Contructing the SimilPaneControl");
        this.dispatcher = null;
    }

    public SimulPaneController(IDispatcher simulDispatcher) {
        setSimulDispatcher(simulDispatcher);
    }

    public void setSimulDispatcher(IDispatcher simulDispatcher) {
        //   System.out.println("Setting dispather to " + simulDispatcher.toString());
        this.dispatcher = simulDispatcher;
    }

    public void stopAction() {
        System.out.println("Stop");
        System.out.println(Thread.currentThread().getName());
    }

    public void startAction() {
        this.dispatcher.changeState(SimulationDispatcher.State.RUN, "GUI");
    }

    public void pushOneEvent() {
        this.dispatcher.changeState(SimulationDispatcher.State.ONEEVENT, "Gui");
    }

    public void quit() {
        this.dispatcher.changeState(SimulationDispatcher.State.TERMINATED, "GUI");
        ((Stage) quitButt.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stopButt.setOnAction(e -> stopAction());
        runButt.setOnAction(e -> startAction());
        oneEventButt.setOnAction(e -> pushOneEvent());
        quitButt.setOnAction(e -> quit());
        timeLabel.textProperty().bind(timeProp);

        IEventBusFX eventBus = ServiceProvider.INSTANCE.getService(IEventBusFX.class);
        eventBus.addEventHandler(FxQuoteEvent.QUOTE_EVENT, this);
        this.dispatcher = ServiceProvider.INSTANCE.getService(IDispatcher.class);
    }

    @Override
    public void handle(FxQuoteEvent event) {
//        if(lastQuoteUpdate == null) { // || (new DateTime().getMillis() - lastQuoteUpdate.getMillis() > 5000)) {
        String timeString = TimeUtils.toFullDateTimeNoMillis(event.getTimestamp());
        Platform.runLater(() -> timeProp.setValue(timeString));
        lastQuoteUpdate = new DateTime();
//        }
//        System.out.println("In PanelControl " + event.toString());


    }
}
