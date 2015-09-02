package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table
public class RoomType extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@Min(1)
	private int size;
	
	public RoomType(){
		
	}
	
	public RoomType(String name, int size){
		this.setName(name);
		this.setSize(size);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
