package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import fr.jchaline.shelter.enums.CellEnum;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class CellOccupant extends AbstractEntity {
	
	@Column(nullable = false)
	private CellEnum type;
	
	@Column(nullable = false)
	@NotBlank
	private String name;
	
	public CellOccupant(String name, CellEnum type) {
		this.setName(name);
		this.setType(type);
	}

	public CellOccupant() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CellEnum getType() {
		return type;
	}

	public void setType(CellEnum type) {
		this.type = type;
	}

}
