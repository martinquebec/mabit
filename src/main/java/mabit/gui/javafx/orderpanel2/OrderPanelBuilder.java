package mabit.gui.javafx.orderpanel2;

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
public class OrderPanelBuilder {
    private static final Logger Log = Logger.getLogger(OrderPanelBuilder.class);
    private final GenericOrderPanelBuilder builder;
   /* Parent root = null; */

    public OrderPanelBuilder() throws IOException {

        List<ColumnDef> colDefs = Arrays.asList(
                ColumnDef.DATE,
                ColumnDef.INSTRUMENT,
                ColumnDef.LAST,
                ColumnDef.QTY);
        this.builder = new GenericOrderPanelBuilder(colDefs);
        new OrderPanelControler(builder.getData());
    }

    public BorderPane getLogPane() {
        return builder.getLogPane();
    }
}

