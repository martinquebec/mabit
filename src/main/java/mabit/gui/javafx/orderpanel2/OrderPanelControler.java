package mabit.gui.javafx.orderpanel2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import mabit.dispatcher.ServiceProvider;
import mabit.gui.javafx.controller.IEventBusFX;
import mabit.gui.javafx.table.ColumnDef;
import mabit.gui.javafx.table.TableMapRow;
import org.apache.log4j.Logger;


/**
 * Created by martin on 15/9/2016.
 */
public class OrderPanelControler implements EventHandler<FxOrderEvent> {
    private static final Logger Log = Logger.getLogger(OrderPanelControler.class);
    final ObservableList<TableMapRow> data;

    public OrderPanelControler(ObservableList<TableMapRow> data) {
        this.data = data;
        init();
    }

    public OrderPanelControler() {
        this(FXCollections.observableArrayList());
    }

    private void init() {
        IEventBusFX eventBus = ServiceProvider.INSTANCE.getService(IEventBusFX.class);
        eventBus.addEventHandler(FxOrderEvent.ORDER_EVENT, this);
    }

    @Override
    public void handle(FxOrderEvent event) {
        System.out.println("Received FX Order event");
        TableMapRow row = new TableMapRow();
        row.addProperty(ColumnDef.DATE,event.getTimestamp());
        row.addProperty(ColumnDef.INSTRUMENT,event.getOrder().getIntrument().getName());
        row.addProperty(ColumnDef.LAST,event.getOrder().getPrice());
        row.addProperty(ColumnDef.QTY,event.getOrder().getQty());

      Platform.runLater(()->{data.add(row);});
    }
}
