package mabit.oms.position;

import mabit.data.instruments.IInstrument;
import mabit.oms.order.Exec;
import mabit.oms.order.Order;

public class BasicPositon {
	IInstrument instrument;
	double initialQty;
	double buyQty;
	double buyPrice;
	double sellQty;
	double sellPrice;
	double pendingBuyQty;
	double pendingSellQty;
	double last;
	double prevPrice;
	
	double getQty() { return initialQty + buyQty + sellQty; }
	
	double getPnl(double last) { 
		return buyQty * (last -buyPrice) + sellQty * (last - sellPrice) + initialQty * (last - prevPrice);
	}
	
	double getDelta() { return getQty() * last; }	
	
	public void exec(Exec exec) {
		addPriceQty(exec.getOrder().getSide().isBuy(), exec.getSgndQty(),exec.getPrice());
		addPendingQty(exec.getOrder().getSide().isBuy(), -1 * exec.getSgndQty());
	}
	
	public void newOrder(Order order) {
		addPendingQty(order.getSide().isBuy(), order.getRequest().getQty());
	}
	
	public void cancelOrder(Order order) {
		addPendingQty(order.getSide().isBuy(), -1 * order.getPendingQty());		
	}
	
	private void addPriceQty(boolean isBuy, double qty, double price) {
		if(isBuy) {
			buyPrice = (buyQty * buyPrice + price * qty) / (buyQty + qty);
			buyQty += qty; 
			
		} else {
			sellPrice = (sellQty * sellPrice + price * qty) / (sellQty + qty);
			sellQty += qty;
		}		
	}
	
	private void addPendingQty(boolean isBuy, double qty) {
		if(isBuy) {
			pendingBuyQty += qty; 
			
		} else {
			pendingSellQty += qty;
		}		
	}

}
