package URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONGetter {
	 public static Optional<JSONObject> readJsonFromUrl(String url) throws IOException {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      JSONParser parser = new JSONParser();
		      JSONObject json = (JSONObject) parser.parse(rd);
		      return Optional.of(json);
		    } catch (ParseException e) {
		    	e.printStackTrace();
				return Optional.empty();
			} finally {
		      is.close();
		    }
		  }
}
