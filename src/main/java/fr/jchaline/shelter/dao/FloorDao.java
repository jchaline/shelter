package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Floor;

public interface FloorDao extends JpaRepository<Floor, Long> {
	
	Floor findByNumber(int number);

}
