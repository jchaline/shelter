package fr.jchaline.shelter.domain;

import java.util.List;
import java.util.Random;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import fr.jchaline.shelter.enums.JobEnum;
import fr.jchaline.shelter.enums.SpecialEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false, exclude={"team"})
@NoArgsConstructor
public class Dweller extends AbstractEntity implements Fighter {
	
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
	
	@Column(nullable = false)
	@Min(0)
	private int life;

	@Column(nullable = false)
	@Min(0)
	private int maxLife;
	
	@OneToMany
	private List<Item> items;
	
	@OneToOne
	private Weapon weapon;
	
	@JsonManagedReference
	@ManyToOne
	private Room room;

	@JsonBackReference
	@ManyToOne
	private Team team;
	
	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Special special;
	
	@Column(nullable = false)
	private JobEnum job = JobEnum.NEWBIE;
	
	@ManyToOne(optional = false)
	private MapCell mapCell;
	
	@ManyToOne(optional = false)
	private Player player;
	
	public Dweller(boolean male, String name, String firstname, Special special){
		this.setMale(male);
		this.setName(name);
		this.setFirstname(firstname);
		this.setLevel(1);
		this.setExperience(0);
		this.setSpecial(special);
		this.updateMaxLife();
		this.setLife(this.getMaxLife());
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

	@Override
	public int attackPerTurn() {
		return (int) Math.ceil(special.getValue(SpecialEnum.A) / 3) + 1;
	}

	@Override
	public int computeDamage(Fighter target) {
		int result = special.getValue(SpecialEnum.S) * (15 + level);
		if (weapon != null) {
			result += weapon.getDamage();
		}
		//rand coeff 0.8 to 1.2
		double randomBonusMalus = new Random().nextDouble() * 0.4 - 0.2;
		result += (int) Math.ceil(result * randomBonusMalus);
		
		//can critic with perception : 100% with 20 perception, 50% with 10
		int criticRand = new Random().nextInt(20) + 1;
		if (criticRand - special.getValue(SpecialEnum.P) < 0) {
			result *= 2;
		}
		
		return result;
	}
	
	public int getSpeed() {
		return special.getValue(SpecialEnum.A);
	}

	@Override
	public void takeDamage(int damage) {
		life = Math.max(0, life - damage);
	}
	
	private void updateMaxLife() {
		maxLife = level * 30;
	}
	
	public void addExperience(int addExperience) {
		//TODO : improve increase level formula
		experience += addExperience;
		int maxExpBeforeLevelUp = getMaxExperience();
		while (experience >= maxExpBeforeLevelUp) {
			experience -= maxExpBeforeLevelUp;
			level++;
			maxExpBeforeLevelUp = getMaxExperience();
			updateMaxLife();
		}
	}

	public int getMaxExperience() {
		return (int) (level * 5 + Math.pow(level, 2));
	}
}
