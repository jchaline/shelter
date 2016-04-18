package fr.jchaline.shelter.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class MapCell extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column
	private int xaxis;
	
	@Column
	private int yaxis;
	
	@OneToOne(cascade = CascadeType.ALL)
	private CellOccupant occupant; 
	
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

	public CellOccupant getOccupant() {
		return occupant;
	}

	public void setOccupant(CellOccupant occupant) {
		this.occupant = occupant;
	}
	
	public boolean equals(MapCell other) {
		return other != null && xaxis == other.xaxis && yaxis == other.yaxis;
	}
	
	@Override
	public int hashCode() {
		return 1024 * xaxis + yaxis;
	}
	
	public String toString() {
		return name + " (" + xaxis + "," + yaxis + ")";
	}
}
