package spedwards.futurefight.htmlreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import spedwards.futurefight.handler.Config;

public class Reviews {
	
	private static JSONArray all = null;
	private static Config config = null;
	
	private static volatile boolean manage = false;
	
	private static HashMap<String, ArrayList<String>> reviews = new HashMap<>();
	private static HashMap<String, ArrayList<Element>> reviewElements = new HashMap<>();
	
	private static Logger lgr = Logger.getLogger(Reviews.class.getName());
	
	public static void getReviews() throws IOException {
		if (config == null) {
			config = new Config(".futurefight", "reviews.json", JSONArray.class);
		}
		
		all = connect("http://www.reddit.com/r/FutureFight/comments/36tw10/character_reviews/.api");
		JSONObject data = all.getJSONObject(0).getJSONObject("data").getJSONArray("children").getJSONObject(0).getJSONObject("data");
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeEcmaScript(StringEscapeUtils.unescapeHtml4(data.getString("selftext_html"))));
		Elements body = doc.select(".md h4 + p");
		for (Element review : body) {
			Element tmp = review;
			ArrayList<String> rvw = new ArrayList<>();
			rvw.add(tmp.child(0).attr("href"));
			while (tmp.previousElementSibling().tag().toString() != "h4") {
				tmp = tmp.previousElementSibling();
				rvw.add(tmp.child(0).attr("href"));
			}
			String character = tmp.previousElementSibling().text();
			reviews.put(character, rvw);
		}
		lgr.log(INFO, "Completed!", reviews);
		manageElements();
		
		config.setConfig(all);
	}
	
	public static void manageElements() throws IOException {
		for (String key : reviews.keySet()) {
			ArrayList<Element> r = new ArrayList<>();
			for (String url : reviews.get(key)) {
				String[] parts = url.split("/");
				String id = parts[8];
				JSONArray children = all.getJSONObject(1).getJSONObject("data").getJSONArray("children");
				JSONObject data = null;
				int i = 0;
				for (; i < children.length(); i++) {
					data = children.getJSONObject(i).getJSONObject("data");
					if (data.getString("id").equals(id)) {
						break;
					}
				}
				Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml4(data.getString("body_html")));
				Element body = doc.select(".md").first();
				r.add(body);
			}
			reviewElements.put(key, r);
		}
		lgr.log(INFO, "Completed!", reviewElements);
		manage = true;
	}
	
	public static String getReview(String character) {
		try {
			while (!manage) {
				Thread.sleep(1000);
			}
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return reviewElements.get(character).get(0).html();
	}
	
	public static JSONArray connect(String urlString) throws IOException {
		try {
			URL url = new URL(urlString);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\n");
			}
			br.close();
			
			return new JSONArray(sb.toString());
		} catch(IOException e) {
			return new JSONArray(getJSON());
		}
	}
	
	public static String getJSON() {
		JSONArray z = config.readConfig();
		return z.toString(4);
	}
	
}
