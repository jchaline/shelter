package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Item extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	/**
	 * The iLevel of the object
	 * Represent the quality of the object
	 */
	@Column(nullable = false)
	@Min(1)
	private int level;
	
	/**
	 * Minimum dweller level to use the object
	 */
	@Column(nullable = false)
	@Min(1)
	private int requiredLevel;
	
}
