package fr.jchaline.shelter.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MapCell extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column
	private int xaxis;
	
	@Column
	private int yaxis;
	
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private CellOccupant occupant; 
	
	public MapCell(String name, int xaxis, int yaxis) {
		this.setName(name);
		this.setXaxis(xaxis);
		this.setYaxis(yaxis);
	}

	public String toString() {
		return name + " (" + xaxis + "," + yaxis + ")";
	}
}
