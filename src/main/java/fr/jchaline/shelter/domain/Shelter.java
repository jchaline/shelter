package fr.jchaline.shelter.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Dweller> dwellers = new ArrayList<Dweller>();
	
	@Column(nullable = false)
	@Min(0)
	private long money;
	
	@Column(nullable = false)
	@Min(0)
	private long water;

	@Column(nullable = false)
	@Min(0)
	private long food;
	
	@Column(nullable = false)
	private LocalDateTime lastCompute = LocalDateTime.now();

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

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getWater() {
		return water;
	}

	public void setWater(long water) {
		this.water = water;
	}

	public long getFood() {
		return food;
	}

	public void setFood(long food) {
		this.food = food;
	}

	public LocalDateTime getLastCompute() {
		return lastCompute;
	}

	public void setLastCompute(LocalDateTime lastCompute) {
		this.lastCompute = lastCompute;
	}
}
