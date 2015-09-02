package fr.jchaline.shelter.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Contains 
 * @author jChaline
 *
 */
@Entity
@Table
public class Floor extends AbstractEntity {
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Room> rooms;
	
	@Column(nullable=false)
	private int number;
	
	public Floor(){
		
	}
	
	public Floor(int number){
		this.setNumber(number);
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
