package mabit.marketdata;

import java.util.List;
import mabit.oms.order.IInstrument;
import mabit.oms.order.Side;
import mabit.utils.XNumber;

public class Quote {
	IInstrument instrument;
	Boolean isValid =null;
	
	final List<QuoteLine> bids;
	final List<QuoteLine> asks;
	
	public Quote(List<QuoteLine> buys, List<QuoteLine> sells, IInstrument instrument) {
		super();
		this.bids = buys;
		this.asks = sells;
		this.instrument = instrument;
	}

	public IInstrument getInstrument() {
		return instrument;
	}

	public List<QuoteLine> getBuys() {
		return bids;
	}

	public List<QuoteLine> getSells() {
		return asks;
	}
	
	public List<QuoteLine> getMySide(Side side) {
		return side.isBuy() ? bids : asks;
	}
	
	public List<QuoteLine> getOppSide(Side side) {
		return side.isBuy() ? asks : bids;
	}
	
	
	public double getQtyAtMyLevel(Side side, double price) {
		for(QuoteLine line :  getMySide(side)) {
			if(XNumber.isEqual(price, line.getPrice())) {
				return line.getQty();
			} else if (side.isMoreOrSameAggressiveThan(price, line.getPrice())) {
				break;
			}
		}	
		return 0.0;
	}
	
	public boolean isValid() {
		if(isValid == null) {
			return isValid = bids.size() > 0
				&& asks.size() > 0 
				&& bids.get(0).getPrice() > 0 
				&& asks.get(0).getPrice() > bids.get(0).getPrice();
		} else {
			return isValid;
		}
	}


}
