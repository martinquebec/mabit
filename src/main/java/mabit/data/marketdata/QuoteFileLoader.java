package mabit.data.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;

import mabit.data.instruments.IInstrument;
import mabit.data.instruments.InstrumentStore;
import mabit.dispatcher.Event.QuoteEvent;

public class QuoteFileLoader {
	private static final Logger Log = Logger.getLogger(QuoteFileLoader.class);

	String header = "Timestamp\";\"Inplay\";\"delay\";\"Market\";\"ID\";\"Market\";\"status\";\"Selection\";\"ID\";\"Selection\";\"name\";\"BP1\";\"BV1\";\"BP2\";\"BV2\";\"BP3\";\"BV3\";\"LP1\";\"LV1\";\"LP2\";\"LV2\";\"LP3\";\"LV3\";\"Total\";\"matched\";\"LPM\"";

	public static List<QuoteEvent> readFile(File file) throws FileNotFoundException {
		List<QuoteEvent> quoteEvents = Lists.newArrayList();
		Scanner scanner = new Scanner(file);
		int lineCount = 0;
		while(scanner.hasNextLine()) {
			String line= scanner.nextLine();
			QuoteEvent quoteEvent = getQuoteFromLine(line);
			//			if(quoteEvent == null) {
			//				Log.error("Cannot parse line: " + line);
			//			} else {
			//				if(!quoteEvent.getQuote().isValid()) {
			//					System.out.println("---- QUOTE NOT Valid---");
			//					System.out.println("\tLine:"+ line);
			//					System.out.println("\t" + quoteEvent.toString());
			//				}
			if(quoteEvent !=null && quoteEvent.getQuote().isValid() && quoteEvent.getDateTime() !=null) {
				quoteEvents.add(quoteEvent);
			}	
			lineCount++;
		}
		Log.info("Loaded " + quoteEvents.size() + " from " + lineCount + " lines");
		return quoteEvents;

	}
	private static QuoteEvent getQuoteFromLine(String line) {
		line = line.replace("\";\"","");
		String tokens[] = line.split(";");
		try {
			DateTime timestamp = new DateTime(Long.parseLong(tokens[0]));
			String status = tokens[3];
			int externalId = Integer.parseInt(tokens[4]);
			String name = tokens[5];
			double BP1 = Double.parseDouble(tokens[6]);
			double BV1 = Double.parseDouble(tokens[7]);
			double BP2 = Double.parseDouble(tokens[8]);
			double BV2 = Double.parseDouble(tokens[9]);
			double BP3 = Double.parseDouble(tokens[10]);
			double BV3 = Double.parseDouble(tokens[11]);
			double LP1 = Double.parseDouble(tokens[12]);
			double LV1 = Double.parseDouble(tokens[13]);
			double LP2 = Double.parseDouble(tokens[14]);
			double LV2 = Double.parseDouble(tokens[15]);
			double LP3 = Double.parseDouble(tokens[16]);
			double LV3 = Double.parseDouble(tokens[17]);

			List<QuoteLine> sells = Lists.newArrayListWithCapacity(3);
			sells.add(new QuoteLine(1/BP1,BV1));
			sells.add(new QuoteLine(1/BP2,BV2));
			sells.add(new QuoteLine(1/BP3,BV3));
			List<QuoteLine> buys = Lists.newArrayListWithCapacity(3);
			buys.add(new QuoteLine(1/LP1,LV1));
			buys.add(new QuoteLine(1/LP2,LV2));
			buys.add(new QuoteLine(1/LP3,LV3));
			IInstrument instrument = InstrumentStore.get().getInstrument(name, externalId);
			return new QuoteEvent(timestamp, new Quote(buys, sells, instrument));

		}	catch(Exception e ) {
			//		e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] s) {
		String line = "1325415633144;0;104570252;\"ACTIVE\";56344;\"Sunderland\";7;1080.35;6.8;1286.65;6.6;865.38;7.2;468.19;7.4;2103.3;7.6;2191.83;40815.36;7.2;;;;;;";
		QuoteEvent event = getQuoteFromLine(line);
		System.out.println(event.getQuote().toFullBaikai());
		//		System.exit(0);
		File file = new File("/Users/martin/git/cloned/mabit/data/Match Odds.csv");
		try {
			List<QuoteEvent> events = readFile(file);
			for(QuoteEvent ev : events) {
				System.out.println(ev.toString());
			}
			InstrumentStore.get().getAllInstrument().forEach(System.out::println);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
