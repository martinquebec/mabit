package mabit.oms.order;

import java.util.LinkedList;
import java.util.List;

import mabit.oms.exchange.ExchangeName;

public class Order {
	Long orderId;
	OrderState state;
	double execQty;
	final List<Exec> exec;
	final OrderRequest request;
	
	public Order(OrderRequest request, Long orderId, OrderState state) {
		this.request = request;
		this.orderId = orderId;
		this.state = state;
		exec = new LinkedList<>();
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
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

	public List<Exec> getExec() {
		return exec;
	}

	public OrderRequest getRequest() {
		return request;
	}
	
	public double getPendingQty() {
		return request.getQty() - getExecQty();
	}
	

	public IInstrument getIntrument() {
		return request.getIntrument();
	}
	public Side getSide() {
		return request.getSide();
	}
	public Label getLabel() {
		return request.getLabel();
	}
	public ExchangeName getExchange() {
		return request.getExchange();
	}
	public double getQty() {
		return request.getQty();
	}
	public double getPrice() {
		return request.getPrice();
	}
}
