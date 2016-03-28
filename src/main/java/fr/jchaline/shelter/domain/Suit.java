package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
public class Suit extends Item {
	
	@Column(nullable = false)
	private int armor;
	
	public Suit() {
		
	}

	public Suit(String name, int armor) {
		this.setName(name);
		this.setArmor(armor);
		this.setLevel(armor * 10);
		this.setLevel(this.getLevel() *  this.getLevel());
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}
}
