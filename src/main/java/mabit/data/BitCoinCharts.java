package mabit.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mabit.util.XNumber;

class BitCoinCharts {
	private final static String URL = "http://bitcoincharts.com/markets/";
	final public static String LOCAL_FILE = "/Users/martin/Documents/dev/mabit/market.html";
	private final static String LOCAL_CSV = "/Users/martin/Documents/dev/mabit/marketStats.csv";
	final public static String CLOUD_FILE = "";
	private List<BccRow> allRows;
//~~~~~~~~~~~~~~~~~~~~~~ BccRow
	public static class BccRow {
		String name;
		final String ccy;
		int trend;
		final String timestamp;
		long latestTrade;
		String url;
		double latestPrice;
		String ago;
		double average30d;
		double change30d;
		double pct_change30d;
		double volumeBtc30d;
		double volumeCcy30d;
		String ccy30d;
		double high30d;
		double low30d;
		double bid;
		double ask;
		double average24h;
		double change24h;
		double pct_change24h;
		double volumeBtc24h;
		double volumeCcy24h;
		String	ccy24h;
		double high24h;
		double low24h;
		
		
		

		public BccRow(Element tr) throws IOException {
//		  <tr id="okcoinCNY" currency="CNY" market="OKCoin" class=" CNY" timestamp="2c57d7a3df8db513ed31c31be6d10ffb18846c702cd9862274d52715" onclick="window.location='/markets/okcoinCNY.html';return false;"> 
			Elements fields = tr.getElementsByTag("td");
			ccy = tr.attr("currency");
			timestamp = tr.attr("timestamp");
			
//			   <td class="arrow" change="up" latest_trade="1468804824"> <span class="up">▲</span> <span class="sub">CNY</span> </td> 
			Element arrow = fields.get(0);
			if(!arrow.attr("class").equals("arrow")) throw new IOException("First element should be class arrow" + arrow.toString());
			trend = (arrow.attr("change").equals("up"))?1:-1;
			latestTrade = Long.parseLong(arrow.attr("latest_trade"));

//			   <td class="symbol"> 
//		    <nobr> 
//		     <a href="/markets/okcoinCNY.html">OKCoin</a> 
//		    </nobr> <span class="sub"><a href="/markets/okcoinCNY.html">okcoinCNY</a></span> </td> 
			Element field = fields.get(1);
			if(!field.attr("class").equals("symbol")) throw new IOException("Element should be class symbol" + field.toString());
			Element a = field.getElementsByTag("span").first().getElementsByTag("a").first();//		latestPrice = Double.parseDouble(arrow.attr("))
			name = a.text();
			url = a.attr("href");

			//<td>4553.4 <span class="sub">0 min ago</span> </td> 
			field = fields.get(2);
			String[] ss = field.text().split(" ");
			latestPrice = Double.parseDouble(ss[0]);
			ago = "";
		
			// skipping element 3: it is a minichart
			
			 //<td>4386.65 <span class="sub change">166.75 3.80%</span> </td> 
			field = fields.get(4); 
			ss = field.text().split(" ");
			average30d = XNumber.getDouble(ss[0]);
			change30d = XNumber.getDouble(ss[1]);
			pct_change30d = XNumber.getDouble(ss[2].replaceAll("%", ""));
			
//			 <td>13,085,249.47 <span class="sub">57,400,403,893.02 CNY</span> </td> 
			field = fields.get(5);
			ss = field.text().split(" ");
			volumeBtc30d = XNumber.getDouble(ss[0].replaceAll(",", ""));
			volumeCcy30d = XNumber.getDouble(ss[1].replaceAll(",", ""));
			ccy30d = ss[2];
			
//			 <td><span class="split">3550</span> <span class="split">5134.1</span> </td> 
			field = fields.get(6);
			ss = field.text().split(" ");
			low30d = XNumber.getDouble(ss[0]);
			high30d = XNumber.getDouble(ss[1]);
			
//			 <td class="break">4552.26</td> 
			bid = XNumber.getDouble(fields.get(7).text());
			
//			 <td>4552.31</td> 
			ask = XNumber.getDouble(fields.get(8).text());
			
			
//			 <td class="break">4503.50 <span class="sub change">49.90 1.11%</span> </td> 
			field = fields.get(9); 
			ss = field.text().split(" ");
			if(ss.length>2) {
				average24h = XNumber.getDouble(ss[0]);
				change24h = XNumber.getDouble(ss[1]);
				pct_change24h = XNumber.getDouble(ss[2].replaceAll("%", ""));	
			} else {
				average24h=0;
				change24h=0;
				pct_change24h=0;
			}
			
			
//			 <td>705,170.67 <span class="sub">3,175,733,767.13 CNY</span> </td> 
			field = fields.get(10);
			ss = field.text().split(" ");
			volumeBtc24h = XNumber.getDouble(ss[0].replaceAll(",", ""));
			volumeCcy24h = XNumber.getDouble(ss[1].replaceAll(",", ""));
			ccy24h = ss[2];

//			 <td> <span class="split">4428.58</span> <span class="split">4594.97</span> </td> 
			field = fields.get(11);
			ss = field.text().split(" ");
			low24h = XNumber.getDouble(ss[0]);
			high24h = XNumber.getDouble(ss[1]);

}
		
		public static String getCsvHeader() {
			return "name,ccy,trend,timestamp,latestTrade,url,latestPrice,ago,average30d,change30d,pct_change30d,volumeBtc30d,"
					+"volumeCcy30d,ccy30d,high30d,low30d,bid,ask,average24h,change24h,pct_change24h,volumeBtc24h,volumeCcy24h,"
					+" ccy24h,high24h,low24h";
		}
		
		String toCsvRow() {
			StringBuilder sb = new StringBuilder();
			sb.append(name).append(",");
			sb.append(ccy).append(",");
			sb.append(trend).append(",");
			//	sb.append(timestamp).append(",");
			sb.append(0).append(",");
			sb.append(latestTrade).append(",");
			sb.append(url).append(",");
			sb.append(latestPrice).append(",");
			sb.append(ago).append(",");
			sb.append(average30d).append(",");
			sb.append(change30d).append(",");
			sb.append(pct_change30d).append(",");
			sb.append(volumeBtc30d).append(",");
			sb.append(volumeCcy30d).append(",");
			sb.append(ccy30d).append(",");
			sb.append(high30d).append(",");
			sb.append(low30d).append(",");
			sb.append(bid).append(",");
			sb.append(ask).append(",");
			sb.append(average24h).append(",");
			sb.append(change24h).append(",");
			sb.append(pct_change24h).append(",");
			sb.append(volumeBtc24h).append(",");
			sb.append(volumeCcy24h).append(",");
			sb.append(ccy24h).append(",");
			sb.append(high24h).append(",");
			sb.append(low24h);
			return sb.toString();					
		}
		
		
	}
	private Document doc = null;
	private BitCoinCharts(String urlString) throws IOException {
		doc = Jsoup.connect(urlString).get();
	}
	BitCoinCharts(File file,String name) throws IOException {
		doc = Jsoup.parse(file, "UTF-8", name);
	}
	
	private void parse() throws IOException {
		
		Element marketsTable = doc.getElementById("markets");
		Element marketsBody = marketsTable.getElementsByTag("tbody").first();
		Elements rows = marketsBody.getElementsByTag("tr");
		allRows= new Vector<>(rows.size());
	//	System.out.println("There are " + rows.size() + "rows");
		System.out.println("row#,"+BccRow.getCsvHeader());
		for(Element row : rows) {
			BccRow bccRow = new BccRow(row);
			allRows.add(bccRow);
		//	System.out.println(""+i+","+ bccRow.toCsvRow());
		//	i++;
		}
	//	PrintWriter writter = new PrintWriter("/Users/martin/Documents/dev/mabit/row.html");
	//	writter.print(row);
	//	writter.close();
	}

	
	public void saveHtml(String filename) throws FileNotFoundException {
	PrintWriter writter = new PrintWriter(filename);
		writter.print(doc); 
		writter.close();

	}
	private void saveCSV(String filename) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(filename);
		writer.println(BccRow.getCsvHeader());
		for(BccRow row: allRows) {
			writer.println(row.toCsvRow());	
		}
		writer.close();
	}
		
	public static void main(String[] s) {
//		System.out.println(Double.parseDouble("13,085,249.47".replaceAll(",", "")));
//		System.exit(0);
		try {
//			BitCoinCharts bcc = new BitCoinCharts(new File(LOCAL_FILE),URL);
			BitCoinCharts bcc = new BitCoinCharts(URL);
			bcc.parse();
			bcc.saveCSV(LOCAL_CSV);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
