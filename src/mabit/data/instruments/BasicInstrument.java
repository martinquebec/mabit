package mabit.data.instruments;

public class BasicInstrument implements IInstrument {
	final int id;
	final String name;
	final int externalId;
	
	public BasicInstrument(int id, String name) {
		this(id,name,id);
	}
	
	public BasicInstrument(int id, String name, int externalId) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
	}
	@Override
	public int getId() {
		return this.id;
	}	
	@Override
	public int getExternalId() {
		return this.externalId;
	}

	@Override
	public String getName() {
		return this.name;
	}
	public String toString() {
		return "[" + id + ", " + name + ", " + externalId + "]";
	}
}
