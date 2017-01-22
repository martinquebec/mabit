package mabit.gui.javafx.table;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import mabit.gui.javafx.table.columnfactory.ColumnFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by martin on 15/9/2016.
 */
//http://stackoverflow.com/questions/22417370/working-with-datetime-as-stringproperty
public class GenericOrderPanelBuilder {
    private static final Logger Log = Logger.getLogger(GenericOrderPanelBuilder.class);

    final BorderPane logPane;
    final ObservableList<TableMapRow> data;
   /* Parent root = null; */

    public GenericOrderPanelBuilder(List<ColumnDef> colDefs) throws IOException {

        logPane = new BorderPane();
        logPane.setPrefSize(1000.0, 1400);


        TableView table = new TableView();
        for(ColumnDef colDef : colDefs) {
            TableColumn<TableMapRow, Object> col = new TableColumn<>(colDef.getProperty());
            col.setCellValueFactory(new AbstractPropertyValueFactory.ObjectPropertyValueFactory(colDef));
            col.setEditable(false);
            col.setPrefWidth(colDef.getPrefWith());
            if(colDef.getType() != ColumnFactory.CellFactoryType.NONE ) {
               col.setCellFactory( new ColumnFactory.CellFactoryCallBack(colDef.getType()));
            }
            table.getColumns().add(col);
        }
        this.data = FXCollections.observableArrayList();
        table.setItems(data);
  //      table.setEditable(true);
        logPane.setCenter(table);
    }

    public BorderPane getLogPane() {
        return logPane;
    }

    public ObservableList<TableMapRow> getData() {
        return data;
    }
}

