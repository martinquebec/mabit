package mabit.oms.position;

import mabit.data.instruments.IInstrument;

/**
 * Created by martin on 9/10/2016.
 */
public interface IPosition {

    public IInstrument getInstrument();
    public double getInitialQty();
    public double getBuyQty();
    public double getBuyPrice();
    public double getSellQty() ;
    public double getSellPrice();
    public double getPendingBuyQty();
    public double getPendingSellQty();
    public double getLast();
    public double getPrevPrice();
}
