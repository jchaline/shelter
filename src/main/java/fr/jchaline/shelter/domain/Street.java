package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Street extends AbstractEntity {
	
	@Column(nullable = false)
	private int number;
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "number")
	private Map<Integer, Spot> spots = new HashMap<Integer,Spot>();
	
	public Street(int number) {
		setNumber(number);
	}
}
