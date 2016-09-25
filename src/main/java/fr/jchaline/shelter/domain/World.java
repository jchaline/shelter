package fr.jchaline.shelter.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class World extends AbstractEntity {

	@OneToMany(cascade = CascadeType.ALL)
	private Set<City> cities = new HashSet<City>();
	
	@Column
	private String name;
	
	/**
	 * Map : cells are nodes, and map is represented with a directed/weighted graph
	 */
	@OneToMany(cascade = CascadeType.ALL)
	
	private Map<String, MapCell> map = new HashMap<>();
	/**
	 * Path between cells
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<MapEdge> edges = new ArrayList<>();
	
	@Column
	private int width;
	
	@Column
	private int height;
	
	public World(String name, int width, int height) {
		this.setName(name);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void setCell(int x, int y, MapCell cell) {
		map.put(x + "_" + y, cell);
	}

	public MapCell getCell(Integer x, Integer y) {
		return map.get(x + "_" + y);
	}
	
	/**
	 * Draw String version of the map, for debug
	 */
	public String draw() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("  ");
		
		Stream.iterate(0, n  ->  n  + 1).limit(10).forEach(x -> {
			sb.append(x + ";");
		});
		
		sb.append("\n\r");
		
		sb.append("");
		for (int y = 0; y<height; y++) {
			sb.append(y + ";");
			for (int x = 0; x<width; x++) {
				char type = getCell(x, y).getOccupant().getType().toString().charAt(0);
				sb.append(type).append(";");
			}
			sb.append("\n\r");
		}
		
		return sb.toString();
	}
}
