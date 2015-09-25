package fr.jchaline.shelter.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Player extends AbstractEntity {
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Shelter shelter;
	
	/**
	 * List of all street's id discovered by player
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Long> discoveredStreets = new HashSet<Long>();
	
	public Player(){
		
	}

	public Player(String name){
		this.setName(name);
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

	public Set<Long> getDiscoveredStreets() {
		return discoveredStreets;
	}

	public void setDiscoveredStreets(Set<Long> discoveredStreets) {
		this.discoveredStreets = discoveredStreets;
	}

}
