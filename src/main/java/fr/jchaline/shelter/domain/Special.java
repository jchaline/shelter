package fr.jchaline.shelter.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.jchaline.shelter.utils.SpecialEnum;

@Entity
@Table
public class Special extends AbstractEntity {
	
	@OneToMany(cascade = CascadeType.ALL)
	@MapKey
	private Map<SpecialEnum, Integer> values;

	public Special(List<Integer> special) {
		int i = 0;
		values = new HashMap<SpecialEnum, Integer>();
		values.put(SpecialEnum.S, i++);
		values.put(SpecialEnum.P, i++);
		values.put(SpecialEnum.E, i++);
		values.put(SpecialEnum.C, i++);
		values.put(SpecialEnum.I, i++);
		values.put(SpecialEnum.A, i++);
		values.put(SpecialEnum.L, i++);
	}

	public Special() {
		
	}
	
	public int getValue(SpecialEnum special) {
		return values.get(special);
	}
	
	public void setValue(SpecialEnum special, int value) {
		this.getValues().put(special, value);
	}

	public Map<SpecialEnum, Integer> getValues() {
		return values;
	}

	public void setValues(Map<SpecialEnum, Integer> values) {
		this.values = values;
	}
	
}
