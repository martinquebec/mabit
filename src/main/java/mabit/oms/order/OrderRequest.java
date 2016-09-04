package mabit.oms.order;

import mabit.data.instruments.IInstrument;
import mabit.oms.exchange.ExchangeName;

public class OrderRequest {
	private final IInstrument intrument;
	private final Side side;
	private final Label label;
	private final ExchangeName exchange;
	
	private final double qty;
	private final double price;
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
	public IInstrument getIntrument() {
		return intrument;
	}
	public Side getSide() {
		return side;
	}
	public Label getLabel() {
		return label;
	}
	public ExchangeName getExchange() {
		return exchange;
	}
	public double getQty() {
		return qty;
	}
	public double getPrice() {
		return price;
	}
	
	
}
