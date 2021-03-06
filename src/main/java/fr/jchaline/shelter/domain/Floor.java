package fr.jchaline.shelter.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Contains 
 * @author jChaline
 *
 */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false, exclude={"rooms"})
@NoArgsConstructor
public class Floor extends AbstractEntity {
	
	@Column(nullable=false)
	private int number;
	
	@Column(nullable=false)
	private int size;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="floor")
	private Set<Room> rooms = new HashSet<Room>();
	
	public Floor(int number, int size){
		this.setNumber(number);
		this.setSize(size);
	}
}
