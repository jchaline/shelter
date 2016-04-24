package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import fr.jchaline.shelter.enums.CellEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CellOccupant extends AbstractEntity {
	
	@Column(nullable = false)
	private CellEnum type;
	
	@Column(nullable = false)
	@NotBlank
	private String name;
	
	public CellOccupant(String name, CellEnum type) {
		this.setName(name);
		this.setType(type);
	}
}
