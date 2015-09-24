package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Spot;

public interface BuildingDao extends JpaRepository<Spot, Long> {
	
}
