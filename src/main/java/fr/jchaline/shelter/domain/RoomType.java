package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import fr.jchaline.shelter.config.ShelterConstants;
import fr.jchaline.shelter.enums.ResourceEnum;
import fr.jchaline.shelter.enums.SpecialEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class RoomType extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@Min(1)
	private int size;
	
	@Column(nullable = false)
	private int cost;
	
	@Column
	private SpecialEnum special;
	
	@Column(nullable = false)
	@Min(1)
	@Max(ShelterConstants.ROOM_MAX_SIZE)
	private int maxSize;
	
	@Column
	private ResourceEnum resource;
	
	public RoomType(String name, ResourceEnum resource, int size, SpecialEnum special, int cost, int maxSize) {
		this.setName(name);
		this.setSize(size);
		this.setSpecial(special);
		this.setCost(cost);
		this.setMaxSize(maxSize);
		this.setResource(resource);
	}
}
