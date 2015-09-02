package fr.jchaline.shelter.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class Dweller extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String nickname;
	
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
	
	public Dweller(){
		
	}

	public Dweller(String nickname){
		this.nickname = nickname;
		this.level = 1;
		this.experience = 0;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

}
