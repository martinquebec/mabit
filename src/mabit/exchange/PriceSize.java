package mabit.exchange;

import java.util.Comparator;

import mabit.oms.order.Side;

public class PriceSize {
	double price;
	double qty;

	public double getPrice() { return price; }
	
	public double getSize() { return qty; }
	
	public static class PriceComparator implements Comparator<PriceSize> {

		private final int mult;
		
		public PriceComparator(boolean isBuy) {
			this.mult = (isBuy)? 1 : -1;
		}
		
		@Override
		public int compare(PriceSize o1, PriceSize o2) {
			
			return mult * Double.compare(o1.getPrice(), o2.getPrice());
		}
		
		public static final PriceComparator BUY_PRICE_COMPARATOR = new PriceComparator(true);
		
		public static final PriceComparator SELL_PRICE_COMPARATOR = new PriceComparator(false);
		
		public static PriceComparator priceComparator(Side side) {
			return (side == Side.BUY)? BUY_PRICE_COMPARATOR : SELL_PRICE_COMPARATOR;
		}
		
	}
	
}
