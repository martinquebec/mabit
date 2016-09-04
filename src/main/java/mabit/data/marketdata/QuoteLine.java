package mabit.data.marketdata;

import java.util.Comparator;

import mabit.oms.order.Side;

public class QuoteLine {
	private final double price;
	private final double qty;
	
	public QuoteLine(double price, double qty) {
		this.price = price;
		this.qty = qty;
	}
	
	
	public double getPrice() { return price; }
	public double getQty() { return qty; }

	public static class QuoteLineComparator implements Comparator<QuoteLine> {

		private final int mult;

		public QuoteLineComparator(boolean isBuy) {
			this.mult = (isBuy)? 1 : -1;
		}

		@Override
		public int compare(QuoteLine o1, QuoteLine o2) {

			return mult * Double.compare(o1.getPrice(), o2.getPrice());
		}

		public static final QuoteLineComparator BUY_PRICE_COMPARATOR = new QuoteLineComparator(true);

		public static final QuoteLineComparator SELL_PRICE_COMPARATOR = new QuoteLineComparator(false);

		public static QuoteLineComparator priceComparator(Side side) {
			return (side == Side.BUY)? BUY_PRICE_COMPARATOR : SELL_PRICE_COMPARATOR;
		}

	}
}
