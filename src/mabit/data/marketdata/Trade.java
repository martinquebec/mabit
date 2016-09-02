package mabit.data.marketdata;

import mabit.data.instruments.IInstrument;

public class Trade {
	enum Condition {
		NONE,
		AUCTION
	}
	IInstrument instrument;
	double qty;
	double price;
	Condition condition;
	
	public Trade(IInstrument instrument, double qty, double price, Condition condition) {
		super();
		this.instrument = instrument;
		this.qty = qty;
		this.price = price;
		this.condition = condition;
	}

	public IInstrument getInstrument() {
		return instrument;
	}

	public double getQty() {
		return qty;
	}

	public double getPrice() {
		return price;
	}

	public Condition getCondition() {
		return condition;
	}
	
	
	
	
	
	

}
