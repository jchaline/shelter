package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Building;

public interface BuildingDao extends JpaRepository<Building, Long> {
	
}
