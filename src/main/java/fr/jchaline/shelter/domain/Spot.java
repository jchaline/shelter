package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Spot extends AbstractEntity {
	
	@Column(nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	private int number;
	
	public Spot(String name) {
		this.setName(name);
	}
}
