package fr.jchaline.shelter.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table
public class Room extends AbstractEntity {
	
	@ManyToOne(fetch = FetchType.EAGER)
	private RoomType roomType;
	
	@Column(nullable=false)
	private int size;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	private Set<Integer> cells = new HashSet<Integer>();
	
	@Column(nullable=false)
	@Min(1)
	private int level;
	
	@JsonBackReference
	@ManyToOne
	private Floor floor;
	
	@JsonBackReference
	@OneToMany(mappedBy="room")
	private List<Dweller> dwellers = new ArrayList<Dweller>();
	
	public Room() {
		
	}
	
	public Room(RoomType type, Set<Integer> cells) {
		this.setRoomType(type);
		this.setSize(cells.size());
		this.setCells(cells);
		this.setLevel(1);
	}


	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Set<Integer> getCells() {
		return cells;
	}

	public void setCells(Set<Integer> cells) {
		this.cells = cells;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public List<Dweller> getDwellers() {
		return dwellers;
	}

	public void setDwellers(List<Dweller> dwellers) {
		this.dwellers = dwellers;
	}

}
