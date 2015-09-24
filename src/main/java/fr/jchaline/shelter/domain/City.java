package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class City extends AbstractEntity {

	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "number")
	private Map<Integer, Street> streets = new HashMap<Integer,Street>();
	
	public City(String name) {
		this();
		this.setName(name);
	}
	
	public City() {
		
	}
	
	public void add(Spot spot, int street, int number) {
		//TODO : assert arguments
		if (!getStreets().containsKey(streets)) {
			getStreets().put(street, new Street(street));
		}
		getStreets().get(street).add(number, spot);
	}
	
	public Spot get(int street, int number) {
		//TODO : assert arguments
		return getStreets().get(street).get(number);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer,Street> getStreets() {
		return streets;
	}

	public void setStreets(Map<Integer,Street> streets) {
		this.streets = streets;
	}

}
