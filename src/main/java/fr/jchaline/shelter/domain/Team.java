package fr.jchaline.shelter.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table
public class Team extends AbstractEntity {
	
	@JsonManagedReference
	@OneToMany
	private List<Dweller> dwellers;
	
	@Column
	private LocalDateTime lastEvent;
	
	@Column(nullable = false)
	private LocalDateTime begin;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Duty duty;
	
	/**
	 * Cell target for the team send on duty
	 */
	@ManyToOne
	private MapCell target;

	/**
	 * Current Cell of the team
	 */
	@ManyToOne(optional = false)
	private MapCell current;
	
	public Team() {
		begin = LocalDateTime.now();
		lastEvent = begin;
	}

	public List<Dweller> getDwellers() {
		return dwellers;
	}

	public void setDwellers(List<Dweller> dwellers) {
		this.dwellers = dwellers;
	}

	public LocalDateTime getLastEvent() {
		return lastEvent;
	}

	public void setLastEvent(LocalDateTime lastEvent) {
		this.lastEvent = lastEvent;
	}

	public LocalDateTime getBegin() {
		return begin;
	}

	public void setBegin(LocalDateTime begin) {
		this.begin = begin;
	}

	public Duty getDuty() {
		return duty;
	}

	public void setDuty(Duty duty) {
		this.duty = duty;
	}

	public MapCell getTarget() {
		return target;
	}

	public void setTarget(MapCell target) {
		this.target = target;
	}

	public MapCell getCurrent() {
		return current;
	}

	public void setCurrent(MapCell current) {
		this.current = current;
	}

}
