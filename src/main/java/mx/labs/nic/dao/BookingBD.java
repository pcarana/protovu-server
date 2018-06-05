package mx.labs.nic.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mx.labs.nic.model.Book;

public class BookingBD {

	// private static List<Book> bookingBD = new ArrayList<Book>();

	private static Map<Long, Book> bookingBD = new HashMap<Long, Book>();

	private static Long idCounter = 0L;

	public static void store(Book book) {

		bookingBD.put(idCounter++, book);
	}

	public static Collection<Book> loadAll() {

		return bookingBD.values();
		// return Collections.unmodifiableList(bookingBD);
	}

	public static Book getById(Long id) {
		return bookingBD.get(id);
	}


}
