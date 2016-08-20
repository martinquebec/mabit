package mabit.oms.order;

public enum OrderState {
	ONMARKET,
	PENDING,
	CANCELED;
	
	public boolean isLive() { return this == ONMARKET || this == PENDING; }
	
}
