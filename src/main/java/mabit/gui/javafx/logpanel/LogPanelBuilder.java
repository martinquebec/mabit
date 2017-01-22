package mabit.gui.javafx.logpanel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import mabit.time.TimeUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;

/**
 * Created by martin on 15/9/2016.
 */
//http://stackoverflow.com/questions/22417370/working-with-datetime-as-stringproperty
public class LogPanelBuilder {
    private static final Logger Log = Logger.getLogger(LogPanelBuilder.class);

    final BorderPane logPane;

   /* Parent root = null; */

    public LogPanelBuilder() throws IOException {

        logPane = new BorderPane();
        logPane.setPrefSize(1000.0, 1400);

        TableView table = new TableView();
        final TableColumn<LogData, DateTime> timeCol = createTableColumn("time", "Time", DateTime.class, 75);
        final TableColumn<LogData, String> typeCol = createTableColumn("type", "Type", String.class, 75);
        final TableColumn<LogData, String> messageCol = createTableColumn("message", "Message", String.class, 500);
 //       typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
  //      messageCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<DateTime>() {
            @Override
            public String toString(DateTime dt) {

                return (dt!=null) ? dt.toString(TimeUtils.HMS) : "no time";
            }

            @Override
            public DateTime fromString(String string) {
                try {
                    return DateTime.parse(string, DateTimeFormat.forPattern(TimeUtils.HMS));
                } catch(Exception e) {
                    return new DateTime();
                }
            }
        }));
        ObservableList<LogData> data = FXCollections.observableArrayList();
        table.getColumns().addAll(timeCol  , typeCol, messageCol);
        table.setItems(data);
        table.setEditable(true);
        logPane.setCenter(table);
        new LogPanelControler(data);
    }

    private <T> TableColumn<LogData, T> createTableColumn(String property, String title, Class<T> type, int prefWidth) {
        TableColumn<LogData, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setEditable(true);
        col.setPrefWidth(prefWidth);
        return col;
    }

    public BorderPane getLogPane() {
        return logPane;
    }
}

