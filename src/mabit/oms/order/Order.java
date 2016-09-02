package mabit.oms.order;

import java.util.List;

import com.google.common.collect.Lists;

import mabit.data.instruments.IInstrument;
import mabit.oms.exchange.ExchangeName;

public class Order {
	final Long orderId;
	final List<Exec> execs = Lists.newArrayList();
	final OrderRequest request;
	OrderState state;
	double execQty;
	double execPrice;
	
	public Order(OrderRequest request, Long orderId, OrderState state) {
		// immutable
		this.request = request;
		this.orderId = orderId;
		// mutable
		this.state = state;
		this.execQty =0.0;
		this.execPrice = 0.0;
	}
	public void addExec(Exec exec) {
		execPrice = (this.execQty * this.execPrice + exec.getAbsQty() + exec.getPrice()) / (this.execPrice + exec.getAbsQty() );
		execQty += exec.getAbsQty();
		this.execs.add(exec);
	}
	
	public void addExecs(List<Exec> execs) {
		if(execs==null || execs.size()==0) return;
		for(Exec exec : execs) {
			addExec(exec);
		}
	}

	
	public Long getOrderId() {
		return orderId;
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

	public List<Exec> getExecs() {
		return execs;
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
	
	public String toShortString() {
		StringBuffer sb =new StringBuffer();
		sb.append("[ id=").append(this.orderId);
		sb.append(", instr=").append(this.getIntrument().getName());
		sb.append(", qty=").append(this.getQty());
		sb.append(", price=").append(this.getPrice());
		sb.append(", side=").append(this.getSide());
		sb.append(", execqty=").append(this.getExecQty());
		sb.append(", state=").append(this.getState());
		sb.append(" ]");
		return sb.toString();
		
	}
}
