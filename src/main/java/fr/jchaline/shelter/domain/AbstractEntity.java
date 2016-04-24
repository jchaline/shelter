package fr.jchaline.shelter.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractEntity {

	@Id
	@GeneratedValue
	@Column(unique = true, nullable = false)
	private Long id;
	
	@Column
	private LocalDateTime dateCreate = LocalDateTime.now();
}
