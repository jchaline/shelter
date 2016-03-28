package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
public class Weapon extends Item {

	@Column(nullable = false)
	private int damage;
	
	@Column(nullable = false)
	@Min(0)
	private int scope;
	
	public Weapon() {
		
	}
	
	public Weapon(String name, int damage, int scope) {
		this.setName(name);
		this.setDamage(damage);
		this.setScope(scope);
		this.setLevel(damage * 10 + scope);
		this.setLevel(this.getLevel() *  this.getLevel());
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}
}
