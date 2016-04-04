package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Team task !
 */
@Entity
@Table
public class Duty extends AbstractEntity {
	
	public static final String EXPLORE = "explore";
	public static final String FIGHT = "fight";
	public static final String RECRUITMENT = "recruitment";
	
	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String label;
	
	public Duty() {
		
	}

	public Duty(String name, String label){
		this.setName(name);
		this.setLabel(label);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
