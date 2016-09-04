package mabit.data.marketdata;

import com.google.common.collect.Lists;
import mabit.data.instruments.IInstrument;
import mabit.oms.order.Side;
import mabit.utils.XNumber;

import java.util.List;

public class Quote {
	private final IInstrument instrument;
	private Boolean isValid =null;
	
	private final List<QuoteLine> bids;
	private final List<QuoteLine> asks;
	
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
	
	private List<QuoteLine> getMySide(Side side) {
		return side.isBuy() ? bids : asks;
	}
	
	public List<QuoteLine> getOppSide(Side side) {
		return side.isBuy() ? asks : bids;
	}

	public double aPrice(int i) {
		return asks.get(i).getPrice();
	}
	public double bPrice(int i) {
		return bids.get(i).getPrice();
	}
	public double aQty(int i) {
		return asks.get(i).getQty();
	}
	public double bQty(int i) {
		return bids.get(i).getQty();
	}
	
	public double bPrice() { return bPrice(0); }
	public double aPrice() { return aPrice(0); }
	public double bQty() { return bQty(0); }
	public double aQty() { return aQty(0); }
	
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ").append(instrument.getName()).append("\t");
		sb.append(XNumber.round(bQty(), 0)).append(" @ ").append(XNumber.round(bPrice(), 3));
		sb.append("\t||\t");
		sb.append(XNumber.round(aQty(), 0)).append(" @ ").append(XNumber.round(aPrice(), 3));
		return sb.toString();
	}
	
	public String toFullBaikai() {
		StringBuilder sb = new StringBuilder();
		for(QuoteLine line : Lists.reverse(asks)) {
			sb.append("\t\t").append(XNumber.round(line.getPrice(),3)).append("\t").append(XNumber.round(line.getQty(), 0)).append("\n");		
		}

		for(QuoteLine line : bids) {
			sb.append("\t").append(XNumber.round(line.getQty(),0)).append("\t").append(XNumber.round(line.getPrice(), 3)).append("\n");		
		}
		return sb.toString();	
		
	}
	
	public static boolean isValid(Quote quote) {
		return (quote != null) && quote.isValid();
	}


}
