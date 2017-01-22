package mabit.dispatcher;

public interface IEventListener {

	enum Priority {
		TOP(0),
		HIGH(1),
		NORMAL(2),
		LOW(3),
		LAST(4);
		
		final int rank;
		Priority(int rank) { this.rank = rank; }
		public int getRank() {return rank; }		
	}
	
	void onEvent(IEvent event);
	Priority getPriority();
}
