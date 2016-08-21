package mabit.oms.order;

public enum Side {
	BUY,
	SELL;
	
	public boolean isBuy() {
		return this == BUY;
	}
	
	public int sign() { 
		return (isBuy())? 1 : -1;
	}
}
