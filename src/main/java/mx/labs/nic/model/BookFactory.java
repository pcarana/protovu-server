package mx.labs.nic.model;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.servlet.http.HttpServletRequest;

public class BookFactory {

	public final static DateTimeFormatter TIME_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static Book createBook(HttpServletRequest req, String body) throws BookException {
		String name;
		String lastName;
		String tripType;
		String departureString;
		LocalDate departure = null;
		String arrivalString;
		LocalDate arrival = null;
		String number;
		int companion = 0;
		String passengerType;
		String[] services = null;
		List<String> servicesList = Collections.emptyList();
		if (body.isEmpty()) {
			name = req.getParameter("nameInput");
			lastName = req.getParameter("lastNameInput");
			tripType = req.getParameter("tripTypeInput");
			departureString = req.getParameter("departureInput");
			arrivalString = req.getParameter("arrivalInput");
			number = req.getParameter("numberInput");
			passengerType = req.getParameter("passengerTypeInput");
			services = req.getParameterValues("serviceInput");
		} else {

			JsonReader reader = Json.createReader(new StringReader(body));
			JsonObject json = reader.readObject();
			reader.close();

			try {
				name = json.getString("name");
			} catch (NullPointerException e) {
				name = null;
			}

			try {
				lastName = json.getString("lastName");
			} catch (NullPointerException e) {
				lastName = null;
			}

			try {
				tripType = json.getString("tripType");
			} catch (NullPointerException e) {
				tripType = null;
			}

			try {
				departureString = json.getString("departure");
			} catch (NullPointerException e) {
				departureString = null;
			}

			
			try {
				JsonValue jValue = json.get("arrival");
				if (jValue.getValueType().equals(ValueType.NULL)) {
					arrivalString = null;
				} else {
					arrivalString = json.getString("arrival");
				}
			} catch (NullPointerException e) {
				arrivalString = null;
			}
			try {
				JsonValue jValue = json.get("familySize");
				if (jValue.getValueType().equals(ValueType.NULL)) {
					number = null;
				} else if (jValue.getValueType().equals(ValueType.NUMBER)) {
					number = json.getInt("familySize") + "";
				}else {
					number = json.getString("familySize");
				}

			} catch (NullPointerException e) {
				number = null;
			}
			try {
				passengerType = json.getString("passengerType");

				JsonValue jValue = json.get("passengerType");
				if (jValue.getValueType().equals(ValueType.NULL)) {
					passengerType = null;
				} else {
					passengerType = json.getString("passengerType");
				}
			} catch (NullPointerException e) {
				passengerType = null;
			}

			try {
				JsonArray jsonArray = json.getJsonArray("services");
				if (jsonArray.size() > 0) {
					services = new String[jsonArray.size()];
					for (int i = 0; i < jsonArray.size(); i++) {
						services[i] = jsonArray.getString(i);
					}
				}
			} catch (NullPointerException e) {
				services = null;
			}

		}

		boolean requestHasErrors = false;
		BookException exception = new BookException();

		// validate name
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.error_book", req.getLocale());

		if (name == null || name.isEmpty() || name.length() < 2 || name.length() > 50) {
			requestHasErrors = true;
			exception.setError("name", bundle.getString("name"));
		}

		if (lastName != null && !lastName.isEmpty() && (lastName.length() < 2 || lastName.length() > 50)) {
			exception.setError("lastName", bundle.getString("lastName"));
		}

		if (tripType == null) {
			requestHasErrors = true;
			exception.setError("tripType", bundle.getString("tripType"));
		}

		if (departureString != null) {
			try {
				departure = LocalDate.parse(departureString, TIME_FORMATER);

			} catch (DateTimeParseException e) {
				requestHasErrors = true;
				exception.setError("departure", bundle.getString("departure"));
			}
		}

		if (arrivalString != null) {
			try {
				arrival = LocalDate.parse(arrivalString, TIME_FORMATER);
				if (arrival.isBefore(departure)) {
					requestHasErrors = true;
					exception.setError("arrival", bundle.getString("arrival"));
				}

			} catch (DateTimeParseException e) {
				requestHasErrors = true;
				exception.setError("arrival", bundle.getString("arrival"));
			}
		} else if (tripType.equalsIgnoreCase("round")) {
			requestHasErrors = true;
			exception.setError("arrival", bundle.getString("arrivalRound"));
		}

		if (number != null && !number.isEmpty()) {
			try {
				companion = Integer.parseInt(number);
				if (companion < 0 || companion > 30) {
					requestHasErrors = true;
					exception.setError("familySize", bundle.getString("familySize"));
				}
			} catch (NumberFormatException e) {
				requestHasErrors = true;
				exception.setError("familySize", bundle.getString("familySize"));
			}
		}

		if (passengerType == null || passengerType.isEmpty()) {
			requestHasErrors = true;
			exception.setError("passengerType", bundle.getString("passengerType"));
		}

		if (services != null && services.length >= 0) {
			servicesList = Arrays.asList(services);
		}

		if (requestHasErrors)
			throw exception;

		return new Book(name, lastName, tripType, departure, arrival, companion, passengerType, servicesList);
	}

}
