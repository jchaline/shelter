package fr.jchaline.shelter.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class World extends AbstractEntity {

	@OneToMany(cascade = CascadeType.ALL)
	private Set<City> cities = new HashSet<City>();
	
	/**
	 * Map : cells are nodes, and map is represented with a directed/weighted graph
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private Map<String, MapCell> map = new HashMap<>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<MapEdge> edges = new ArrayList<>();
	
	@Column
	private int width;
	
	@Column
	private int height;
	
	public World(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public World() {
		
	}
	
	public void setCell(int x, int y, MapCell cell) {
		map.put(x + "_" + y, cell);
	}

	public MapCell getCell(int x, int y) {
		return map.get(x + "_" + y);
	}

	public Set<City> getCities() {
		return cities;
	}

	public void setCities(Set<City> cities) {
		this.cities = cities;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Map<String, MapCell> getMap() {
		return map;
	}

	public void setMap(Map<String, MapCell> map) {
		this.map = map;
	}

	public List<MapEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<MapEdge> edges) {
		this.edges = edges;
	}

}
