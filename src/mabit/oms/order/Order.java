package mabit.oms.order;

import java.util.List;

public class Order {
	int orderId;
	OrderState state;
	double execQty;
	List<Exec> exec;
	OrderRequest request;
	
	public Order(OrderRequest request, int orderId, OrderState state) {
		this.request = request;
		this.orderId = orderId;
		this.state = state;
		
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public double getExecQty() {
		return execQty;
	}

	public void setExecQty(double execQty) {
		this.execQty = execQty;
	}

	public List<Exec> getExec() {
		return exec;
	}

	public void setExec(List<Exec> exec) {
		this.exec = exec;
	}

	public OrderRequest getRequest() {
		return request;
	}

	public void setRequest(OrderRequest request) {
		this.request = request;
	}
	
	public Side getSide() {
		return request.getSide();
	}
	
	public double getPendingQty() {
		return request.getQty() - getExecQty();
	}
	
	
}
