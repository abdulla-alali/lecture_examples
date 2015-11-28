package course.examples.maps.earthquakemap;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONResponseHandler {


	public static List<EarthQuakeRec> getJSON(String data) {
		List<EarthQuakeRec> result = new ArrayList<EarthQuakeRec>();
		try {
			JSONObject object = (JSONObject) new JSONTokener(data)
					.nextValue();
			JSONArray earthquakes = object.getJSONArray("earthquakes");
			for (int i = 0; i < earthquakes.length(); i++) {
				JSONObject tmp = (JSONObject) earthquakes.get(i);
				result.add(new EarthQuakeRec(
						tmp.getDouble("lat"),
						tmp.getDouble("lng"),
						tmp.getDouble("magnitude")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
