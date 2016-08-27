package mabit.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import mabit.exchange.IExchangeRule;
import mabit.marketdata.QuoteLine;
import mabit.oms.order.Side;

public class TradingUtils {

	private TradingUtils() {}

	
	public static double getFirstTicGreaterThanOrEqual(double requestedPrice, IExchangeRule exchgRule) {
		double price = exchgRule.getDownTickPrice(requestedPrice);
		while(price < requestedPrice) {
			price = exchgRule.getUpTickPrice(price);
		}
		return price;
	}
	
	public static double getFirstTicLessThanOrEqual(double requestedPrice, IExchangeRule exchgRule) {
		double price = exchgRule.getUpTickPrice(requestedPrice);
		while(price > requestedPrice) {
			price = exchgRule.getDownTickPrice(price);
		}
		return price;
	}
	
	public static boolean isBetterPrice(Side side, double price1, double price2) {
		if(side == Side.BUY) {
			return price1 > price2;
		} else {
			return price1 < price2;
		}
	}
	
	public static Double getBestPrice(List<QuoteLine> priceAmounts) { 
		QuoteLine priceAmount = priceAmounts.get(0);
		return (priceAmount != null)? priceAmount.getPrice() : null;
	}
	
	public static Double getBestAmount(List<QuoteLine> priceAmounts) {
		QuoteLine priceAmount = priceAmounts.get(0);
		return (priceAmount != null)? priceAmount.getQty() : null;
	}
	
	
	public static Double getLastLevelPrice(List<QuoteLine> priceAmounts) {
		return (priceAmounts.size() > 0)? priceAmounts.get(priceAmounts.size() - 1).getPrice() : null;
	}
	
	public static double valuePriceToSize(Side side, double value, double price) {
		if(side == Side.BUY)
			return value;
		else 
			return value/price;
	}
	
	public static double avgPxForAmount(double amount, List<QuoteLine> priceSizes) {
		double cumAmount = 0;
		double cumValue = 0;
		Iterator<QuoteLine> priceSizeIt = priceSizes.iterator();
		while(priceSizeIt.hasNext() && cumAmount < amount) {
			QuoteLine priceSize = priceSizeIt.next();
			double levelAmount = Math.min(priceSize.getQty(), amount - cumAmount);
			cumValue += levelAmount * priceSize.getPrice();
			cumAmount += levelAmount;
		}
		
		return (cumAmount > 0.0)? cumValue / cumAmount : 0.0;
	}
	
	public static LinkedHashMap<Double,QuoteLine> priceSizesByAmount(List<QuoteLine> priceSizes) {
		LinkedHashMap<Double, QuoteLine> map = new LinkedHashMap<Double, QuoteLine>();
		if(priceSizes != null) {
			for(QuoteLine priceSize : priceSizes) {
				map.put(priceSize.getPrice(), priceSize);
			}
		}
		return map;
	}
	
}
