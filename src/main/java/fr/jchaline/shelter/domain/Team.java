package fr.jchaline.shelter.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Team extends AbstractEntity {
	
	@JsonManagedReference
	@OneToMany
	private List<Dweller> dwellers;
	
	@Column(nullable = false)
	private LocalDateTime lastEvent = LocalDateTime.now();

	@Column
	private LocalDateTime lastMove;
	
	@Column(nullable = false)
	private LocalDateTime begin = LocalDateTime.now();
	
	@ManyToOne
	private Duty duty;
	
	/**
	 * Cell target for the team send on duty
	 */
	@ManyToOne
	private MapCell target;

	/**
	 * Cell origin of the team before sent on duty
	 */
	@ManyToOne
	private MapCell origin;

	/**
	 * Current Cell of the team
	 */
	@ManyToOne(optional = false)
	private MapCell current;
	
	@ManyToOne(optional = false)
	private Player player;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(this.getId());
		sb.append(") ");
		sb.append(" origin : ");
		sb.append(this.getOrigin());
		sb.append(", target : ");
		sb.append(this.getTarget());
		sb.append(", current : ");
		sb.append(this.getCurrent());
		return sb.toString();
	}
}
