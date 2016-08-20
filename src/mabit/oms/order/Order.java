package mabit.oms.order;

import java.util.List;

public class Order {
	int orderId;
	OrderState state;
	double execQty;
	List<Exec> exec;
	OrderRequest request;
	
	public Order(OrderRequest request, int orderId, OrderState state, double execQty, List<Exec> exec) {
		this.request = request;
		this.orderId = orderId;
		this.state = state;
		this.execQty = execQty;
		this.exec = exec;
	}
	
	
	
}
