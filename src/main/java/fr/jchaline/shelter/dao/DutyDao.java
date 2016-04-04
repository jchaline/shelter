package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Duty;

public interface DutyDao  extends JpaRepository<Duty, Long> {
	
	Duty findByName(String name);

}
