package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Street extends AbstractEntity {
	
	@Column(nullable = false)
	private int number;
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey
	private Map<Integer, Spot> spots = new HashMap<Integer,Spot>();
	
	public Street(int number) {
		this();
		setNumber(number);
	}

	public Street() {
		
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Map<Integer,Spot> getSpots() {
		return spots;
	}

	public void setSpots(Map<Integer,Spot> spots) {
		this.spots = spots;
	}

	public void add(int number, Spot spot) {
		spot.setNumber(number);
		getSpots().put(number, spot);
	}

	public Spot get(int number) {
		return getSpots().get(number);
	}

}
