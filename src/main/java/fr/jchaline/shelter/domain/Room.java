package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Room extends AbstractEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	private RoomType roomType;
	
	@Column(nullable=false)
	private int size;
	
	public Room(){
		
	}
	
	public Room(RoomType type){
		this.setRoomType(type);;
	}


	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

}
