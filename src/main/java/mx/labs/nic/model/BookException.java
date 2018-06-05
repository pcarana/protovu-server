package mx.labs.nic.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookException extends Exception {

	private Map<String, String> reqMessages;

	private static final long serialVersionUID = 1744602840150652839L;

	public BookException() {
		super();
		reqMessages = new HashMap<String, String>();
	}

	public Map<String, String> getErrors() {
		return Collections.unmodifiableMap(reqMessages);
	}

	public void setError(String field, String errorMessage) {
		reqMessages.put(field, errorMessage);
	}

}
