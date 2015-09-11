package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * The player's shelter
 * @author jChaline
 *
 */
@Entity
@Table
public class Shelter extends AbstractEntity {
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "number")
	private Map<Integer, Floor> floors = new HashMap<Integer, Floor>();
	
	@OneToMany
	private List<Dweller> dwellers;
	
	@Column(nullable = false)
	@Min(0)
	private int money;
	
	@Column(nullable = false)
	@Min(0)
	private int water;

	@Column(nullable = false)
	@Min(0)
	private int food;

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

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}
}
