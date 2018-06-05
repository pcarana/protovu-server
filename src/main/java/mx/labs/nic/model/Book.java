package mx.labs.nic.model;

import java.time.LocalDate;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Book {

	private String name;
	private String lastName;
	private String flighType;
	private LocalDate departure;
	private LocalDate arrival;
	private Integer familySize;
	private String passengerType;
	private List<String> extraServices;

	public Book(String name, String lastName, String flighType, LocalDate departure, LocalDate arrival,
			Integer familySize, String passengerType, List<String> extraServices) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.flighType = flighType;
		this.departure = departure;
		this.arrival = arrival;
		this.familySize = familySize;
		this.passengerType = passengerType;
		this.extraServices = extraServices;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFlighType() {
		return flighType;
	}

	public void setFlighType(String flighType) {
		this.flighType = flighType;
	}

	public LocalDate getDeparture() {
		return departure;
	}

	public void setDeparture(LocalDate departure) {
		this.departure = departure;
	}

	public LocalDate getArrival() {
		return arrival;
	}

	public void setArrival(LocalDate arrival) {
		this.arrival = arrival;
	}

	public Integer getFamilySize() {
		return familySize;
	}

	public void setFamilySize(Integer familySize) {
		this.familySize = familySize;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	public List<String> getExtraServices() {
		return extraServices;
	}

	public void setExtraServices(List<String> extraServices) {
		this.extraServices = extraServices;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flighType == null) ? 0 : flighType.hashCode());
		result = prime * result + ((arrival == null) ? 0 : arrival.hashCode());
		result = prime * result + ((departure == null) ? 0 : departure.hashCode());
		result = prime * result + ((extraServices == null) ? 0 : extraServices.hashCode());
		result = prime * result + ((familySize == null) ? 0 : familySize.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((passengerType == null) ? 0 : passengerType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (flighType == null) {
			if (other.flighType != null)
				return false;
		} else if (!flighType.equals(other.flighType))
			return false;
		if (arrival == null) {
			if (other.arrival != null)
				return false;
		} else if (!arrival.equals(other.arrival))
			return false;
		if (departure == null) {
			if (other.departure != null)
				return false;
		} else if (!departure.equals(other.departure))
			return false;
		if (extraServices == null) {
			if (other.extraServices != null)
				return false;
		} else if (!extraServices.equals(other.extraServices))
			return false;
		if (familySize == null) {
			if (other.familySize != null)
				return false;
		} else if (!familySize.equals(other.familySize))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (passengerType == null) {
			if (other.passengerType != null)
				return false;
		} else if (!passengerType.equals(other.passengerType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Book [name=" + name + ", lastName=" + lastName + ", flighType=" + flighType + ", departure=" + departure
				+ ", arrival=" + arrival + ", familySize=" + familySize + ", passengerType=" + passengerType
				+ ", extraServices=" + extraServices + "]";
	}

	public String toJson() {
		return toJsonObject().toString();
	}

	public JsonObject toJsonObject() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		if (name != null)
			builder.add("name", name);
		if (lastName != null)
			builder.add("lastName", lastName);
		if (flighType != null)
			builder.add("flightType", flighType);
		if (departure != null)
			builder.add("departure", departure.format(BookFactory.TIME_FORMATER));
		if (arrival != null)
			builder.add("arrival", arrival.format(BookFactory.TIME_FORMATER));
		if (familySize != null)
			builder.add("familySize", familySize);
		if (passengerType != null)
			builder.add("passengerType", passengerType);
		if (extraServices != null) {

			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (String s : extraServices) {
				arrayBuilder.add(s);
			}

			builder.add("services", arrayBuilder.build());
		}

		return builder.build();
	}

}
