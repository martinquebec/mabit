package mabit.exchange;

public interface IExchangeRule {

	double getUpTickPrice(double price);
	
	double getDownTickPrice(double price);
	
	double getMinAmount();
}