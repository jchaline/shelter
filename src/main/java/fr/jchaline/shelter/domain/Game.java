package fr.jchaline.shelter.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Game extends AbstractEntity {
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Shelter shelter;
	
	public Game(){
		
	}

	public Game(String name){
		this.setName("generated");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Shelter getShelter() {
		return shelter;
	}

	public void setShelter(Shelter shelter) {
		this.shelter = shelter;
	}

}
