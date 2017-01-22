package mabit.oms.order;

import com.google.common.collect.Lists;
import mabit.data.instruments.IInstrument;
import mabit.oms.exchange.ExchangeName;

import java.util.List;

public class Order {
	private final Long orderId;
	private final List<Exec> execs = Lists.newArrayList();
	private final OrderRequest request;
	OrderState state;
	private double execQty;
	private double execPrice;
	
	public Order(OrderRequest request, Long orderId, OrderState state) {
		// immutable
		this.request = request;
		this.orderId = orderId;
		// mutable
		this.state = state;
		this.execQty =0.0;
		this.execPrice = 0.0;
	}
	private void addExec(Exec exec) {
		execPrice = (this.execQty * this.execPrice + exec.getAbsQty() + exec.getPrice()) / (this.execPrice + exec.getAbsQty() );
		execQty += exec.getAbsQty();
		this.execs.add(exec);
	}
	
	public void addExecs(List<Exec> execs) {
		if(execs==null || execs.size()==0) return;
		execs.forEach(this::addExec);
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

	private double getExecQty() {
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

	public Order copy() {
		Order copy = new Order(getRequest(),getOrderId(),getState());
		copy.execPrice = this.execPrice;
		copy.execQty = this.execQty;
		return copy;
	}
	
	public String toShortString() {
		StringBuilder sb =new StringBuilder();
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
