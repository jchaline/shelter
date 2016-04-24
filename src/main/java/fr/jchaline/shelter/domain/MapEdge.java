package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class MapEdge extends AbstractEntity {
	
	@ManyToOne(optional = false)
	private MapCell source;
	
	@ManyToOne(optional = false)
	private MapCell target;

	@Column(nullable = false)
	private int weight;
	
	public MapEdge(MapCell from, MapCell to) {
		this.setSource(from);
		this.setTarget(to);
		this.setWeight(1);
	}

	public MapEdge(MapCell from, MapCell to, int weight) {
		this.setSource(from);
		this.setTarget(to);
		this.setWeight(weight);
	}

	public String toString() {
		return "(" + source.getXaxis() + "," + source.getYaxis() + ") => (" + target.getXaxis() + "," + target.getYaxis() + ") [" + weight + "]";
	}

}
