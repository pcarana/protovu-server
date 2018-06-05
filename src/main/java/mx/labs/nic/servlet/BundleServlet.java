package mx.labs.nic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "bundle", urlPatterns = { "/bundle" })
public class BundleServlet extends HttpServlet {

	private static final long serialVersionUID = -527206455880514688L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Locale locale = req.getLocale();
		System.out.println("Request for '" + locale + "' bundles...");
		ResourceBundle catalogsBundle = ResourceBundle.getBundle("bundle.catalogs.labels", locale);
		ResourceBundle globalBundle = ResourceBundle.getBundle("bundle.modules.global.labels", locale);
		ResourceBundle registerBundle = ResourceBundle.getBundle("bundle.modules.register.labels", locale);
		ResourceBundle statsBundle = ResourceBundle.getBundle("bundle.modules.stats.labels", locale);
		List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
		bundles.add(catalogsBundle);
		bundles.add(globalBundle);
		bundles.add(registerBundle);
		bundles.add(statsBundle);
		
		// String parameter = req.getParameter("id");
		JsonObject result = null;
		Integer status = null;
		String errorMsg = null;

		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		Map<String, Object> loadedBundles = new HashMap<String, Object>();
		for (ResourceBundle rb : bundles) {
			for (String key : rb.keySet()) {
				addPropertyToMap(loadedBundles, key, rb.getString(key));
			}
		}
		JsonObjectBuilder langBuilder = Json.createObjectBuilder();
		langBuilder.add(locale.getLanguage().toLowerCase(), getJsonFromMap(loadedBundles));
		result = langBuilder.build();

		if (result == null) {
			if (status == null) {
				status = HttpServletResponse.SC_NOT_FOUND;
				errorMsg = "Not Found";
			}
			resp.setStatus(status);
			PrintWriter writer = resp.getWriter();
			writer.write("{" + status + ":\"" + errorMsg + "\"}");
			writer.flush();

			return;
		}

		JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
		jsonWriter.writeObject(result);

		return;
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doOptions(req, resp);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Allow", "GET, POST");
		resp.setHeader("Access-Control-Allow-Headers", "*");
	}
	
	@SuppressWarnings("unchecked")
	private static JsonObjectBuilder getJsonFromMap(Map<String, Object> map) {
		JsonObjectBuilder valueBuilder = Json.createObjectBuilder();
		JsonObjectBuilder currBuilder = valueBuilder;
		for (String key : map.keySet()) {
			Object currObj = map.get(key);
			if (currObj instanceof Map) {
				currBuilder.add(key, getJsonFromMap((Map<String, Object>) currObj));
			} else if (currObj instanceof String) {
				currBuilder.add(key, (String)map.get(key));
			}
		}
		return valueBuilder;
	}
	
	@SuppressWarnings("unchecked")
	private static void addPropertyToMap(Map<String, Object> map, String key, String value) {
		String[] keys = key.split("\\.");
		Map<String, Object> currMap = map;
		for (int i = 0; i < keys.length - 1; i++) {
			if (currMap.containsKey(keys[i])) {
				Object currObj = currMap.get(keys[i]);
				if (currObj instanceof Map) {
					currMap = (Map<String, Object>) currObj;
				} else {
					throw new RuntimeException("An object property was expected, but got another thing at " + key);
				}
			} else {
				Map<String, Object> objMap = new HashMap<String, Object>();
				currMap.put(keys[i], objMap);
				currMap = objMap;
			}
		}
		currMap.put(keys[keys.length - 1], value);
	}

}
