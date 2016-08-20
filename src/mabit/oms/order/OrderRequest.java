package mabit.oms.order;

import java.util.List;

import mabit.exchange.ExchangeName;

public class OrderRequest {
	IInstrument intrument;
	Side side;
	Label label;
	ExchangeName exchange;
	
	double qty;
	double price;
	public OrderRequest(IInstrument intrument, Side side, Label label, ExchangeName exchange, double qty,
			double price) {
		super();
		this.intrument = intrument;
		this.side = side;
		this.label = label;
		this.exchange = exchange;
		this.qty = qty;
		this.price = price;
	}
}
