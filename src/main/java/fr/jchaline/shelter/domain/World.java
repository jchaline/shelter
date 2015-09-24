package fr.jchaline.shelter.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class World  extends AbstractEntity {

	//first layout : x, second : y
	@OneToMany(cascade = CascadeType.ALL)
	private Set<City> cities = new HashSet<City>();
	
	public World() {
		
	}

	public Set<City> getCities() {
		return cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}
	
}
