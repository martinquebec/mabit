package mabit.gui.javafx.table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import org.apache.log4j.Logger;

import java.util.IdentityHashMap;
import java.util.List;


/**
 * Created by martin on 1/11/2016.
 */
public class TableMapRow {
    public static class ColumnDefValuePair {
        private final ColumnDef property;
        private final Object value;

        public ColumnDefValuePair(ColumnDef property, Object value) {
            this.property = property;
            this.value = value;
        }

        public ColumnDef getProperty() {
            return property;
        }

        public Object getValue() {
            return value;
        }
    }

    private final Logger Log = Logger.getLogger(TableMapRow.class);
    IdentityHashMap<ColumnDef, SimpleObjectProperty<Object>> observableObjects = new IdentityHashMap<>();

    public TableMapRow() {
    }

    public TableMapRow(List<ColumnDefValuePair> pairs) {
        updateProperties(pairs);
    }

    public ObservableValue<Object> getObservableObject(ColumnDef property) {
        SimpleObjectProperty<Object> obsObj = this.observableObjects.get(property);
        if (obsObj == null) {
            obsObj = new SimpleObjectProperty<Object>(null);
            observableObjects.put(property, obsObj);
        }
        return obsObj;
    }

    public void addProperty(ColumnDef property, Object value) {
        observableObjects.put(property, new SimpleObjectProperty<Object>(value));
    }

    public void updateProperty(ColumnDef property, Object value) {
        SimpleObjectProperty<Object> row = observableObjects.get(property);
        if(row == null) {
            addProperty(property,value);
        } else {
            row.setValue(value);
        }
    }

    public void updateProperties(List<ColumnDefValuePair> propertyList) {
        for(ColumnDefValuePair pair : propertyList) {
            updateProperty(pair.getProperty(),pair.getValue());
        }
    }

}
