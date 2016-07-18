package mabit;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Snippet {
	public static void main(String[] s) {
		URL url;
		try {
			url = new URL("http://bitcoincharts.com/markets/");

//			URLConnection con = url.openConnection();
//			InputStream in = con.getInputStream();
//			String encoding = con.getContentEncoding();
//			encoding = encoding == null ? "UTF-8" : encoding;
//			String body = IOUtils.toString(in, encoding);
//			Document doc = Jsoup.parse(body);
			Document doc = Jsoup.connect(url.toString()).get();
			String body = doc.toString();
			PrintWriter writter = new PrintWriter("/Users/martin/Documents/dev/mabit/market.html");
			writter.print(body);
			writter.close();
			System.out.println(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

