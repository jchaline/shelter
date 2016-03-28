package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.jchaline.shelter.domain.Item;

public interface ItemDao extends JpaRepository<Item, Long> {
	
	@Query("SELECT MAX(level) FROM Item i ")
	Integer findMaxLevel();
	
	@Query("SELECT MIN(level) FROM Item i ")
	Integer findMinLevel();

}
