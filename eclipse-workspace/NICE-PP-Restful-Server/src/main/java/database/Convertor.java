package database;

import java.sql.ResultSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Convertor {

	public static JSONArray convertToJSON(ResultSet resultSet) throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (resultSet.next()) {
			int total_rows = resultSet.getMetaData().getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < total_rows; i++) {
				String tableName = resultSet.getMetaData().getTableName(i + 1);
				if (tableName.length() > 0) {
					tableName += ".";
				}
				obj.put(tableName + resultSet.getMetaData().getColumnLabel(i + 1), resultSet.getObject(i + 1));

			}
			jsonArray.add(obj);
		}
		return jsonArray;
	}
	
}
