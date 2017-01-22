package mabit.gui.javafx.table;

import org.joda.time.DateTime;

import static mabit.gui.javafx.table.columnfactory.ColumnFactory.CellFactoryType;

/**
 * Created by martin on 13/10/2016.
 */
public enum ColumnDef {

    INSTRUMENT("Instrument", String.class, 150, CellFactoryType.NONE),
    LAST("Last", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    DATE("Time", DateTime.class, 100, CellFactoryType.TIME_FACTORY),
    QTY("Qty", Double.class, 75, CellFactoryType.QTY_FACTORY),
    ORDER_PRICE("Order Price", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    LAST_PRICE("Price", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    INITIAL_QTY("Ini Qty", Double.class, 75, CellFactoryType.QTY_FACTORY),
    BUY_QTY("Buy Qty", Double.class, 75, CellFactoryType.QTY_FACTORY),
    BUY_PRICE("Buy Price", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    SELL_QTY("Sell Qty", Double.class, 75, CellFactoryType.QTY_FACTORY),
    SELL_PRICE("Sell Price", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    PENDING_BUY_QTY("Buy Qty", Double.class, 75, CellFactoryType.QTY_FACTORY),
    PENDING_SELL_QTY("SellQty", Double.class, 75, CellFactoryType.QTY_FACTORY),
    BID_1_QTY("Bid", Double.class, 75, CellFactoryType.QTY_FACTORY),
    ASK_1_QTY("Ask", Double.class, 75, CellFactoryType.QTY_FACTORY),
    BID_1_PRICE("Bid Price", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    ASK_1_PRICE("Ask Price", Double.class, 75, CellFactoryType.PRICE_FACTORY),
    SEQ_NUMBER("Seq",Integer.class,75, CellFactoryType.QTY_FACTORY);


    final String property;
    final Class<?> clazz;
    final int prefWith;
    final CellFactoryType type;

    ColumnDef(String property, Class<?> clazz, int prefWith, CellFactoryType type) {
        this.property = property;
        this.clazz = clazz;
        this.prefWith = prefWith;
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public int getPrefWith() {
        return prefWith;
    }

    public CellFactoryType getType() {
        return type;
    }
}