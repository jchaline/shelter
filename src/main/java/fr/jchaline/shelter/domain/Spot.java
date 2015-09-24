package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class Spot extends AbstractEntity {
	
	@Column(nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	private int number;
	
	public Spot(String name) {
		this();
		this.setName(name);
	}

	public Spot() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
