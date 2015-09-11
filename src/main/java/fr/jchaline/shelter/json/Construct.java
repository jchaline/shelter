package fr.jchaline.shelter.json;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Construct {
	
	@NotNull
	private Integer floor;
	
	@NotNull
	private Integer cell;
	
	@NotBlank
	private String type;
	
	public Construct(){
		
	}
	
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Integer getCell() {
		return cell;
	}
	public void setCell(Integer cell) {
		this.cell = cell;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
