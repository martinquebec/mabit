package mabit.gui.javafx.table.columnfactory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import mabit.gui.javafx.table.TableMapRow;
import mabit.time.TimeUtils;
import org.joda.time.DateTime;

/**
 * Created by martin on 25/9/2016.
 */
public class ColumnFactory {

        public enum CellFactoryType {
            PRICE_FACTORY,
            TIME_FACTORY,
            QTY_FACTORY,
            NONE

        };

    public static class CellFactoryCallBack implements Callback<TableColumn<TableMapRow, Object>, TableCell<TableMapRow, Object>> {
        private final CellFactoryType type;
        public CellFactoryCallBack(CellFactoryType type) {
            super();
            this.type =type;
        }

        @Override
        public TableCell<TableMapRow, Object> call(TableColumn<TableMapRow, Object> param) {
            switch (type) {
                case PRICE_FACTORY:
                    return new ColumnFactory.DoubleCellFactory(2,false,false);
                case QTY_FACTORY:
                    return new ColumnFactory.DoubleCellFactory(1,false,false);
                case TIME_FACTORY:
                    return new ColumnFactory.DateTimeCellFactory();
            }
            return null;
        }
    }
        public static class DoubleCellFactory extends TableCell<TableMapRow, Object> {
        int digits = -1;
        boolean red_neg = false;
        boolean pct = false;

        public DoubleCellFactory(int digits, boolean red_neg, boolean pct) {
            this.digits = digits;
            this.red_neg = red_neg;
            this.pct = pct;
        }
        @Override
        protected void updateItem(Object obj, boolean empty) {
            super.updateItem(obj, empty);
            Double item = (Double)obj;
            setStyle("");
            if (item == null || empty) {
                setText(null);
            } else {
                // Format date.
                double use_value = (pct) ? item * 100 : item;
                String number = String.format("%." + digits + "f", item);
                setText(number);

                if (red_neg && item < 0) {
                    setTextFill(Color.RED);
                } else {
                    setTextFill(Color.BLACK);
                }
            }
        }
    }

    public static class IntegerCellFactory extends TableCell<TableMapRow, Object> {
        int digits = -1;
        boolean red_neg = false;
        boolean pct = false;

        public IntegerCellFactory(boolean red_neg) {
            this.red_neg = red_neg;
        }
        @Override
        protected void updateItem(Object obj, boolean empty) {
            super.updateItem(obj, empty);
            if (obj == null || empty || !(obj instanceof Integer)) {
                setText(null);
            } else {
                Integer n = (Integer) obj;
                String formated = String.format("%,d",n);
                setText(formated);

                if (red_neg && n < 0) {
                    setTextFill(Color.RED);
                } else {
                    setTextFill(Color.BLACK);
                }
            }
        }
    }


    public static class DateTimeCellFactory extends TableCell<TableMapRow, Object> {
        public DateTimeCellFactory() {
            super();
        }

        @Override
        protected void updateItem(Object obj, boolean empty) {
            super.updateItem(obj, empty);
            if (obj == null || empty || !(obj instanceof DateTime)) {
                setText(null);
            } else {
                DateTime dt = (DateTime)obj;
                String text = dt.toString(TimeUtils.HMS);
                setText(text);
                setTextFill(Color.STEELBLUE);
                setVisible(true);
            }
        }
    }
}
