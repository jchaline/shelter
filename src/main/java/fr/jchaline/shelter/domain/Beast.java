package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Beast extends AbstractEntity {
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String name;
	
	@Column(nullable = false)
	@Min(0)
	private int attack;
	
	@Column(nullable = false)
	@Min(0)
	private int speed;
	
	@Column(nullable = false)
	@Min(0)
	private int life;

	@Column(nullable = false)
	@Min(1)
	private int level;
	
	public Beast(String name, int attack, int speed, int life, int level) {
		this.setName(name);
		this.setAttack(attack);
		this.setSpeed(speed);
		this.setLife(life);
		this.setLevel(level);
	}
}
