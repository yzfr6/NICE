package JSON;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONGetter {

	public static JSONObject loadJSONObject(File f) throws IOException, ParseException {

		JSONParser jsonParser = new JSONParser();

		FileReader reader = new FileReader(f);

		Object obj = jsonParser.parse(reader);

		JSONObject json = (JSONObject) obj;

		return json;

	}

}
