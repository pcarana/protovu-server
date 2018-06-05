package mx.labs.nic.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.labs.nic.dao.BookingBD;
import mx.labs.nic.model.Book;
import mx.labs.nic.model.BookException;
import mx.labs.nic.model.BookFactory;

@WebServlet(name = "booking", urlPatterns = { "/booking" })
public class BookingServlet extends HttpServlet {

	private static final long serialVersionUID = -558527206468880514L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String parameter = req.getParameter("id");
		JsonObject result = null;
		Integer status = null;
		String errorMsg = null;
		resp.setHeader("Access-Control-Allow-Origin", "*");

		if (parameter != null && !parameter.isEmpty()) {
			try {
				Book book = BookingBD.getById(Long.parseLong(parameter));
				if (book != null)
					result = book.toJsonObject();
			} catch (NumberFormatException e) {
				// XXX: handle exception
				status = HttpServletResponse.SC_BAD_REQUEST;
				errorMsg = "Bad Request";
			}
		} else {
			Collection<Book> loadAll = BookingBD.loadAll();
			if (loadAll != null && !loadAll.isEmpty()) {
				JsonObjectBuilder builder = Json.createObjectBuilder();
				JsonArrayBuilder arrB = Json.createArrayBuilder();
				for (Book b : loadAll) {
					arrB.add(b.toJsonObject());
				}
				builder.add("Books", arrB);
				result = builder.build();
			}
		}

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");

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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Enumeration<String> attributeNames = req.getAttributeNames();

		while (attributeNames.hasMoreElements()) {
			System.out.println("attributes");
			String nextElement = attributeNames.nextElement();
			System.out.println(nextElement + " - " + req.getAttribute(nextElement));
		}

		Map<String, String[]> parameterMap = req.getParameterMap();

		for (String key : parameterMap.keySet()) {
			System.out.println("parameters");
			System.out.println(key + " - " + Arrays.asList(parameterMap.get(key)));
		}

		String body = req.getReader().lines().collect(Collectors.joining());
		System.out.println(body.isEmpty());


		// System.out.println(req.getParameterMap());
		// handlePost(req);
		resp.setHeader("Access-Control-Allow-Origin", "*");

		Book createBook;
		try {
			createBook = BookFactory.createBook(req, body);
			BookingBD.store(createBook);
		} catch (BookException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			JsonObjectBuilder builder = Json.createObjectBuilder();

			builder.add("error", "cannot store the request");

			for (String field : e.getErrors().keySet()) {
				builder.add(field, e.getErrors().get(field));
			}

			JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
			jsonWriter.writeObject(builder.build());

			return;
		}
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doOptions(req, resp);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Allow", "GET, POST");
		resp.setHeader("Access-Control-Allow-Headers", "*");

		// resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

	}

}
