package fr.jchaline.shelter.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import fr.jchaline.shelter.enums.JobEnum;

@Entity
@Table
public class Dweller extends AbstractEntity {
	
	@Column(nullable = false)
	private Boolean male;

	@Column(nullable = false)
	@NotBlank
	private String name;

	@Column(nullable = false)
	@NotBlank
	private String firstname;
	
	@Column(nullable = false)
	@Min(1)
	private int level;
	
	@Column(nullable = false)
	@Min(0)
	private int experience;
	
	@OneToMany
	private List<Item> items;
	
	@OneToOne
	private Weapon weapon;
	
	@JsonManagedReference
	@OneToOne
	private Room room;

	@JsonManagedReference
	@ManyToOne
	private Team team;
	
	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Special special;
	
	@Column(nullable = false)
	private JobEnum job = JobEnum.NEWBIE;
	
	@OneToOne
	private MapCell mapCell;
	
	public Dweller() {
		
	}

	public Dweller(boolean male, String name, String firstname, Special special){
		this.setMale(male);
		this.setName(name);
		this.setFirstname(firstname);
		this.setLevel(1);
		this.setExperience(0);
		this.setSpecial(special);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.getId());
		sb.append(") ");
		sb.append(this.getFirstname());
		sb.append(" ");
		sb.append(this.getName());
		sb.append(", level : ");
		sb.append(this.getLevel());
		return sb.toString();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Special getSpecial() {
		return special;
	}

	public void setSpecial(Special special) {
		this.special = special;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Boolean getMale() {
		return male;
	}

	public void setMale(Boolean male) {
		this.male = male;
	}

	public JobEnum getJob() {
		return job;
	}

	public void setJob(JobEnum job) {
		this.job = job;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public MapCell getMapCell() {
		return mapCell;
	}

	public void setMapCell(MapCell mapCell) {
		this.mapCell = mapCell;
	}

}
