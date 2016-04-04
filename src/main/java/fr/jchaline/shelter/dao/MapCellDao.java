package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.MapCell;

public interface MapCellDao extends JpaRepository<MapCell, Long> {
	
}
