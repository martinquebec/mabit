package mabit.marketdata;

import java.util.List;
import mabit.oms.order.IInstrument;

public class Quote {
	IInstrument instrument;
	
	final List<QuoteLine> buys;
	final List<QuoteLine> sells;
	
	public Quote(List<QuoteLine> buys, List<QuoteLine> sells, IInstrument instrument) {
		super();
		this.buys = buys;
		this.sells = sells;
		this.instrument = instrument;
	}
	

}
