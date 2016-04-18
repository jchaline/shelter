package fr.jchaline.shelter.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import fr.jchaline.shelter.enums.CellEnum;

/**
 * @author JCHALINE
 *
 */
@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
public class City extends CellOccupant {

	//TODO : delete this, put spot (market, school, hospital, ... but not street with number etc ...)
	@OneToMany(cascade = CascadeType.ALL)
	private List<Spot> spots = new ArrayList<Spot>();
	
	public City(String name) {
		super(name, CellEnum.CITY);
	}
	
	public City() {
		
	}

	public List<Spot> getSpots() {
		return spots;
	}

	public void setSpots(List<Spot> spots) {
		this.spots = spots;
	}

	public void addSpot(Spot spot) {
		spots.add(spot);
	}

}
