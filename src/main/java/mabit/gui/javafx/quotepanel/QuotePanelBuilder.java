package mabit.gui.javafx.quotepanel;

import javafx.scene.layout.BorderPane;
import mabit.gui.javafx.table.ColumnDef;
import mabit.gui.javafx.table.GenericOrderPanelBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by martin on 15/9/2016.
 */
//http://stackoverflow.com/questions/22417370/working-with-datetime-as-stringproperty
public class QuotePanelBuilder {
    private static final Logger Log = Logger.getLogger(QuotePanelBuilder.class);
    private final GenericOrderPanelBuilder builder;


   /* Parent root = null; */

    public QuotePanelBuilder() throws IOException {
        List<ColumnDef> colDefs = Arrays.asList(
                ColumnDef.INSTRUMENT,
                ColumnDef.BID_1_QTY,
                ColumnDef.BID_1_PRICE,
                ColumnDef.ASK_1_PRICE,
                ColumnDef.ASK_1_QTY,
                ColumnDef.DATE,
                ColumnDef.SEQ_NUMBER);
        this.builder = new GenericOrderPanelBuilder(colDefs);
        new QuotePanelControler(builder.getData());

    }
    public BorderPane getLogPane() {
        return builder.getLogPane();
    }
}

