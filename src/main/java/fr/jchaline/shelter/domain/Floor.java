package fr.jchaline.shelter.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	@Column(nullable=false)
	private int number;
	
	@Column(nullable=false)
	private int size;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Room> rooms = new HashSet<Room>();
	
	public Floor(){
		
	}
	
	public Floor(int number, int size){
		this.setNumber(number);
		this.setSize(size);
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}
	
}
