package mabit.oms.position;

import mabit.data.instruments.IInstrument;
import mabit.oms.order.Exec;
import mabit.oms.order.Order;

class BasicPositon implements IPositionManager, IPosition {
	private final IInstrument instrument;
	private final double initialQty;
	private double buyQty;
	private double buyPrice;
	private double sellQty;
	private double sellPrice;
	private double pendingBuyQty;
	private double pendingSellQty;
	private double last;
	private final double prevPrice;

	public BasicPositon(IInstrument instrument, double initialQty, double prevPrice) {
		this.instrument = instrument;
		this.initialQty = initialQty;
		this.prevPrice = prevPrice;
	}

	private double getQty() { return initialQty + buyQty + sellQty; }
	
	double getPnl(double last) { 
		return buyQty * (last -buyPrice) + sellQty * (last - sellPrice) + initialQty * (last - prevPrice);
	}
	
	double getDelta() { return getQty() * last; }	

	@Override
	public void addExec(Exec exec) {
		addPriceQty(exec.getOrder().getSide().isBuy(), exec.getSgndQty(),exec.getPrice());
		addPendingQty(exec.getOrder().getSide().isBuy(), -1 * exec.getSgndQty());
	}
	@Override
	public void newOrder(Order order) {
		addPendingQty(order.getSide().isBuy(), order.getRequest().getQty());
	}

	@Override
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

	public IInstrument getInstrument() {
		return instrument;
	}

	public double getInitialQty() {
		return initialQty;
	}

	public double getBuyQty() {
		return buyQty;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public double getSellQty() {
		return sellQty;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public double getPendingBuyQty() {
		return pendingBuyQty;
	}

	public double getPendingSellQty() {
		return pendingSellQty;
	}

	public double getLast() {
		return last;
	}

	public double getPrevPrice() {
		return prevPrice;
	}
}
