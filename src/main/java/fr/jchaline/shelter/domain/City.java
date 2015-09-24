package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class City extends AbstractEntity {

	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	public City(String name) {
		this.setName(name);
	}
	
	public City() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
