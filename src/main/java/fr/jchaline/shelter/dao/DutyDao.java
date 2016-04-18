package fr.jchaline.shelter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Duty;

public interface DutyDao  extends JpaRepository<Duty, Long> {
	
	Duty findByName(String name);
	
	List<Duty> findByAction(boolean action);

}
