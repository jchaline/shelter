package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
public class Suit extends Item {
	
	@Column(nullable = false)
	private int armor;

	public Suit(String name, int armor, int level, int requiredLevel) {
		this.setName(name);
		this.setArmor(armor);
		this.setLevel(level);
		this.setRequiredLevel(requiredLevel);
	}
}
