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
public class Beast extends AbstractEntity implements Fighter{
	
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
	
	public Beast(String name, int attack, int speed, int level) {
		this.setName(name);
		this.setAttack(attack);
		this.setSpeed(speed);
		this.setLife(life);
		this.setLevel(level);
		this.updateMaxLife();
	}

	@Override
	public int attackPerTurn() {
		return (int) Math.ceil(speed / 3) + 1;
	}

	@Override
	public int computeDamage(Fighter target) {
		return attack;
	}

	@Override
	public void takeDamage(int damage) {
		life = Math.max(0, life - damage);
	}
	
	private void updateMaxLife() {
		life = level * 30;
	}
}
