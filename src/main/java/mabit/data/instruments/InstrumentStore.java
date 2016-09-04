package mabit.data.instruments;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;

public class InstrumentStore {
	final private static Logger Log = Logger.getLogger(InstrumentStore.class);


	final private static InstrumentStore me = new InstrumentStore();
	final private Map<Integer,IInstrument> idMap = Maps.newHashMap();
	final private Map<String,IInstrument> nameMap = Maps.newHashMap();
	final private Map<Integer,IInstrument> extIdMap = Maps.newHashMap();
	private static int idCounter = 0;

	private InstrumentStore() {}

	public static InstrumentStore get() { return me; }

	public IInstrument getInstrumentById(int id) { return idMap.get(id); }
	public IInstrument getInstrumentByName(String name) { return nameMap.get(name); }
	public IInstrument getInstrumentByExternalId(int extId) { return extIdMap.get(extId); }

	private IInstrument createInstrument(String name, int externalId) {
		BasicInstrument instrument = null;
		if(extIdMap.containsKey(externalId)) {
			Log.error(
					"Cannot create intrument with " + name + " and " 
							+ externalId + ". External Id already exist: " + extIdMap.get(externalId).toString());
		} else if(nameMap.containsKey(name)) {
			Log.error(
					"Cannot create intrument with " + name + " and " 
							+ externalId + ". Name already exist: " + extIdMap.get(externalId).toString());

		} else {
			instrument = new BasicInstrument(idCounter++, name,externalId);
			idMap.put(instrument.getId(), instrument);
			nameMap.put(instrument.getName(), instrument);		
			extIdMap.put(externalId, instrument);
			Log.info("Created instrument" + instrument.toString());
		}
		return instrument;
	}

	public IInstrument getInstrument(String name, int externalId) {
		IInstrument instrument = extIdMap.get(externalId);
		if(instrument ==null) {
			instrument =createInstrument(name, externalId);
		}
		return instrument;
	}
	
	public Collection<IInstrument> getAllInstrument() { return idMap.values(); }
}
