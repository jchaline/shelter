package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import fr.jchaline.shelter.utils.SpecialEnum;

@Entity
@Table
public class RoomType extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@Min(1)
	private int size;
	
	@Column(nullable = false)
	private SpecialEnum special;
	
	public RoomType(){
		
	}
	
	public RoomType(String name, int size, SpecialEnum special){
		this.setName(name);
		this.setSize(size);
		this.setSpecial(special);
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

	public SpecialEnum getSpecial() {
		return special;
	}

	public void setSpecial(SpecialEnum special) {
		this.special = special;
	}
}
