package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class Beast extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@NotBlank
	private int attack;
	
	@Column(nullable = false)
	@NotBlank
	private int speed;
	
	@Column(nullable = false)
	@NotBlank
	private int life;
	
	public Beast(String name, int attack, int speed, int life) {
		this.setName(name);
		this.setAttack(attack);
		this.setSpeed(speed);
		this.setLife(life);
	}
	
	public Beast() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

}
