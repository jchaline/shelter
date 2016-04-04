package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class MapCell extends AbstractEntity {
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column
	private int xaxis;
	
	@Column
	private int yaxis;
	
	public MapCell(String name, int xaxis, int yaxis) {
		this.setName(name);
		this.setXaxis(xaxis);
		this.setYaxis(yaxis);
	}

	public MapCell() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getXaxis() {
		return xaxis;
	}

	public void setXaxis(int xaxis) {
		this.xaxis = xaxis;
	}

	public int getYaxis() {
		return yaxis;
	}

	public void setYaxis(int yaxis) {
		this.yaxis = yaxis;
	}
}
