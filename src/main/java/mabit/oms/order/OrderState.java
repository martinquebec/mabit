package mabit.oms.order;


public enum OrderState {
	REJECTED(true,false),
	PENDING_NEW(false,true),
	ON_MARKET(false,false),
	PENDING_AMEND(false,true),
	PENDING_CANCEL(false,true),
	CANCELLED(true,false),
	FILLED(true,false);
	
	private final boolean isTerminal;
	
	private final boolean isPending;
	
	OrderState(boolean isTerminal, boolean isPending) {
		this.isTerminal = isTerminal;
		this.isPending = isPending;
	}
	
	public boolean isTerminal() {
		return isTerminal;
	}
	
	public boolean isPending() {
		return isPending;
	}
}
