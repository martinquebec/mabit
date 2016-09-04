package mabit.oms.order;

public class Exec {
	private final Order order;
	private final double price;
	private final double qty;
	
	public Exec(Order order, double price, double qty) {
		super();
		this.order = order;
		this.price = price;
		this.qty = qty;
	}
	
	public Order getOrder() {
		return order;
	}
	public double getPrice() {
		return price;
	}
	public double getAbsQty() {
		return qty;
	}
	
	public double getSgndQty() {
		return qty * order.getSide().sign();
	}
}
