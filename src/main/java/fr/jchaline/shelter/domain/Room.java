package fr.jchaline.shelter.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Room extends AbstractEntity {
	
	@ManyToOne(fetch = FetchType.EAGER)
	private RoomType roomType;
	
	@Column(nullable=false)
	private int size;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Integer> cells = new HashSet<Integer>();
	
	public Room(){
		
	}
	
	public Room(RoomType type, Set<Integer> cells) {
		this.setRoomType(type);
		this.setSize(type.getSize());
		this.setCells(cells);
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

}
