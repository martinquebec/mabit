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
	//TODO: use XNumber
	public boolean isMoreOrSameAggressiveThan(double p1, double p2) {
		return isBuy() ? p1 >= p2 : p1 <= p2;
	}
	
	public boolean isStricklyMoreAggressiveThan(double p1, double p2) {
		return isBuy() ? p1 > p2 : p1 < p2;
	}

}
