package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Shelter extends AbstractEntity {
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "number")
	private Map<Integer, Floor> floors = new HashMap<Integer, Floor>();
	
	@OneToMany
	private List<Dweller> dwellers;

	public Shelter(){
		
	}

	public Map<Integer, Floor> getFloors() {
		return floors;
	}

	public void setFloors(Map<Integer, Floor> floors) {
		this.floors = floors;
	}

	public List<Dweller> getDwellers() {
		return dwellers;
	}

	public void setDwellers(List<Dweller> dwellers) {
		this.dwellers = dwellers;
	}
}
