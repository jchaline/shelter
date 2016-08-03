package fr.jchaline.shelter.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import fr.jchaline.shelter.enums.CellEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A city can grow up, not 	shrink
 * @author JCHALINE
 *
 */
@Entity
@Table
@Polymorphism(type= PolymorphismType.EXPLICIT)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends CellOccupant {

	//TODO : delete this, put spot (market, school, hospital, ... but not street with number etc ...)
	@OneToMany(cascade = CascadeType.ALL)
	private List<Spot> spots = new ArrayList<Spot>();
	
	@Column
	@Min(10)
	private int width;
	
	@Column
	@Min(10)
	private int height;
	
	public City(String name) {
		super(name, CellEnum.CITY);
		this.setWidth(10);
		this.setHeight(10);
	}

	public void addSpot(Spot spot) {
		spots.add(spot);
	}

}
