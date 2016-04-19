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
	public static final String RETURN = "return";
	
	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String label;
	
	/**
	 * True for player's actions, false for other purpose
	 */
	@Column(nullable = false)
	private boolean action = false;
	
	public Duty() {
		
	}

	public Duty(String name, String label, boolean action) {
		this.setName(name);
		this.setLabel(label);
		this.setAction(action);
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

	public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

}