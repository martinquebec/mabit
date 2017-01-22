package mabit.gui.javafx.table;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;



public abstract class AbstractPropertyValueFactory<T> implements Callback<TableColumn.CellDataFeatures<TableMapRow,T>, ObservableValue<T>> {

    private final ColumnDef property;

   // public AbstractPropertyValueFactory() {
    //    this.property = "none";
    //}

    public AbstractPropertyValueFactory(ColumnDef property) {
        this.property = property;
    }

    public final ColumnDef getProperty() { return property; }

  /*  public static class NumberPropertyValueFactory extends AbstractPropertyValueFactory<Number> {
        public NumberPropertyValueFactory(ColumnDef property) {
            super(property);
        }

        @Override
        public ObservableValue<Number> call(TableColumn.CellDataFeatures<TableMapRow, Number> param) {
            TableMapRow tmr = param.getValue();
            return tmr.getObservableNumber(getProperty());
        }
    }*/

    public static class ObjectPropertyValueFactory extends AbstractPropertyValueFactory<Object> {
        public ObjectPropertyValueFactory(ColumnDef property) {
            super(property);
        }

        @Override
        public ObservableValue<Object> call(TableColumn.CellDataFeatures<TableMapRow, Object> param) {
            TableMapRow tmr = param.getValue();
            return tmr.getObservableObject(getProperty());
        }
    }
}
