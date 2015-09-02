package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
public class Weapon extends Item {

	@Column(nullable = false)
	private int damage;
	
	public Weapon(){
		
	}
	
	public Weapon(String name, int damage){
		this.setName(name);
		this.setDamage(damage);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
