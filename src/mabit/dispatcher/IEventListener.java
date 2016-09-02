package mabit.dispatcher;

public interface IEventListener {

	public static enum Priority {
		TOP(0),
		HIGH(1),
		NORMAL(2),
		LOW(3),
		LAST(4);
		
		int rank;
		Priority(int rank) { this.rank = rank; }
		public int getRank() {return rank; }		
	}
	
	public void onEvent(IEvent event);
	Priority getPriority();
}
