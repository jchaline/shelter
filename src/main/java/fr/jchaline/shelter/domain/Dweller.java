package fr.jchaline.shelter.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import fr.jchaline.shelter.enums.JobEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false, exclude={"team"})
@NoArgsConstructor
public class Dweller extends AbstractEntity {
	
	@Column(nullable = false)
	private Boolean male;

	@Column(nullable = false)
	@NotBlank
	private String name;

	@Column(nullable = false)
	@NotBlank
	private String firstname;
	
	@Column(nullable = false)
	@Min(1)
	private int level;
	
	@Column(nullable = false)
	@Min(0)
	private int experience;
	
	@OneToMany
	private List<Item> items;
	
	@OneToOne
	private Weapon weapon;
	
	@JsonManagedReference
	@ManyToOne
	private Room room;

	@JsonBackReference
	@ManyToOne
	private Team team;
	
	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Special special;
	
	@Column(nullable = false)
	private JobEnum job = JobEnum.NEWBIE;
	
	@ManyToOne(optional = false)
	private MapCell mapCell;
	
	public Dweller(boolean male, String name, String firstname, Special special){
		this.setMale(male);
		this.setName(name);
		this.setFirstname(firstname);
		this.setLevel(1);
		this.setExperience(0);
		this.setSpecial(special);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.getId());
		sb.append(") ");
		sb.append(this.getFirstname());
		sb.append(" ");
		sb.append(this.getName());
		sb.append(", level : ");
		sb.append(this.getLevel());
		return sb.toString();
	}
}
