package fr.jchaline.shelter.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table
public class Team extends AbstractEntity {
	
	@JsonBackReference
	@OneToMany
	private List<Dweller> dwellers = new ArrayList<Dweller>();
	
	@Column
	private LocalDateTime lastEvent;
	
	@Column(nullable = false)
	private LocalDateTime begin;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Duty duty;
	
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

}
