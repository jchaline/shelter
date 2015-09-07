package fr.jchaline.shelter.domain;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;

import fr.jchaline.shelter.utils.SpecialEnum;

@Entity
@Table
public class Special extends AbstractEntity {

	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "special")
	@MapKeyEnumerated(EnumType.STRING)
	@Column(name = "value")
	private Map<SpecialEnum, Integer> values;

	public Special(List<Integer> special) {
		int i = 0;
		values = new HashMap<SpecialEnum, Integer>();
		values.put(SpecialEnum.S, special.get(i++));
		values.put(SpecialEnum.P, special.get(i++));
		values.put(SpecialEnum.E, special.get(i++));
		values.put(SpecialEnum.C, special.get(i++));
		values.put(SpecialEnum.I, special.get(i++));
		values.put(SpecialEnum.A, special.get(i++));
		values.put(SpecialEnum.L, special.get(i++));
	}

	public Special() {

	}
	
	public String toString() {
		return MessageFormat.format("S({0})P({1})E({2})C({3})I({4})A({5})L({6})", values.get(SpecialEnum.S),values.get(SpecialEnum.P),values.get(SpecialEnum.E),
				values.get(SpecialEnum.C),values.get(SpecialEnum.I),values.get(SpecialEnum.A), values.get(SpecialEnum.L));
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
