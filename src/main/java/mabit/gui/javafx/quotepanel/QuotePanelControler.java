package mabit.gui.javafx.quotepanel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import mabit.dispatcher.ServiceProvider;
import mabit.gui.javafx.controller.IEventBusFX;
import mabit.gui.javafx.table.ColumnDef;
import mabit.gui.javafx.table.TableMapRow;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mabit.gui.javafx.table.ColumnDef.SEQ_NUMBER;


/**
 * Created by martin on 15/9/2016.
 */
public class QuotePanelControler implements EventHandler<FxQuoteEvent> {
    private final Map<String,TableMapRow> rowMap = new HashMap<>();
    private static final Logger Log = Logger.getLogger(QuotePanelControler.class);
    final ObservableList<TableMapRow> data;

    public QuotePanelControler(ObservableList<TableMapRow> data) {
        this.data = data;
        init();
    }

    public QuotePanelControler() {
        this(FXCollections.observableArrayList());
    }

    private void init() {
        IEventBusFX eventBus = ServiceProvider.INSTANCE.getService(IEventBusFX.class);
        eventBus.addEventHandler(FxQuoteEvent.QUOTE_EVENT, this);
    }

    /*
    public void handle2(FxQuoteEvent event) {
        QuoteData prevQuote = rowMap.get(event.getQuote().getInstrument().getName());
        if(prevQuote==null) {
            QuoteData quoteData = new QuoteData(event.getQuote(), event.getTimestamp());
            rowMap.put(event.getQuote().getInstrument().getName(),quoteData);
            Platform.runLater(() -> {
                data.add(quoteData);
            });
        } else {
            Platform.runLater(() -> prevQuote.update(event.getTimestamp(),event.getQuote()));

        }
    }
*/
    @Override
    public void handle(FxQuoteEvent event) {
        List<TableMapRow.ColumnDefValuePair> values = new ArrayList<TableMapRow.ColumnDefValuePair>(7);
        values.add(new TableMapRow.ColumnDefValuePair(ColumnDef.INSTRUMENT,event.getQuote().getInstrument().getName()));
        values.add(new TableMapRow.ColumnDefValuePair(ColumnDef.BID_1_QTY,event.getQuote().getBuys().get(0).getQty()));
        values.add(new TableMapRow.ColumnDefValuePair(ColumnDef.BID_1_PRICE,event.getQuote().getBuys().get(0).getPrice()));
        values.add(new TableMapRow.ColumnDefValuePair(ColumnDef.ASK_1_QTY,event.getQuote().getSells().get(0).getQty()));
        values.add(new TableMapRow.ColumnDefValuePair(ColumnDef.ASK_1_PRICE,event.getQuote().getSells().get(0).getPrice()));
        values.add(new TableMapRow.ColumnDefValuePair(ColumnDef.DATE,event.getTimestamp()));


         TableMapRow row = rowMap.get(event.getQuote().getInstrument().getName());
        if(row==null) {
            values.add(new TableMapRow.ColumnDefValuePair(SEQ_NUMBER,0.0));
            final TableMapRow row2 = new TableMapRow(values);
            Platform.runLater(()->{data.add(row2);});
            rowMap.put(event.getQuote().getInstrument().getName(),row2);
        } else {
            Double seq = (Double)row.getObservableObject(ColumnDef.SEQ_NUMBER).getValue();
            if(seq==null) seq = -999.0;
            row.updateProperty(ColumnDef.SEQ_NUMBER, seq+1.0);

            Platform.runLater(()->{row.updateProperties(values);});
        }
     }
}
