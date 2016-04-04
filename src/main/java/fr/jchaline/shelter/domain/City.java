package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
public class City extends MapCell {

	
	//TODO : delete this, put spot (market, school, hospital, ... but not street with number etc ...)
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "number")
	private Map<Integer, Street> streets = new HashMap<Integer,Street>();
	
	public City(String name, int xAxis, int yAxis) {
		super(name, xAxis, yAxis);
	}
	
	public City() {
		
	}
	
	public void add(Spot spot, int street, int number) {
		//TODO : assert arguments
		if (!getStreets().containsKey(street)) {
			getStreets().put(street, new Street(street));
		}
		getStreets().get(street).add(number, spot);
	}
	
	public Spot get(int street, int number) {
		//TODO : assert arguments
		return getStreets().get(street).get(number);
	}

	public Map<Integer,Street> getStreets() {
		return streets;
	}

	public void setStreets(Map<Integer,Street> streets) {
		this.streets = streets;
	}

}
