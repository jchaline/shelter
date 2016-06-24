package fr.jchaline.shelter.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The player's shelter
 * @author jChaline
 *
 */
@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Shelter extends AbstractEntity {
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "number")
	private Map<Integer, Floor> floors = new HashMap<Integer, Floor>();
	
	@Column(nullable = false)
	@Min(0)
	private long water = 200;

	@Column(nullable = false)
	@Min(0)
	private long food = 200;
	
	@Column(nullable = false)
	private LocalDateTime lastCompute = LocalDateTime.now();
}
