package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Weapon extends Item {

	@Column(nullable = false)
	private int damage;
	
	@Column(nullable = false)
	@Min(0)
	private int scope;
	
	public Weapon(String name, int damage, int scope, int level, int requiredLevel) {
		this.setName(name);
		this.setDamage(damage);
		this.setScope(scope);
		this.setLevel(level);
		this.setRequiredLevel(requiredLevel);
	}
}
