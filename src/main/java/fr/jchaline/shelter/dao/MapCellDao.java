package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.MapCell;

public interface MapCellDao extends JpaRepository<MapCell, Long> {
	
	/**
	 * Find map cell by cell name
	 * @param name The cell name to find
	 * @return the MapCell
	 */
	MapCell findByName(String name);
	
}
