package mabit.exchange;

public interface IExchangeRule {

	public double getUpTickPrice(double price);
	
	public double getDownTickPrice(double price);
	
	public double getMinAmount();
}