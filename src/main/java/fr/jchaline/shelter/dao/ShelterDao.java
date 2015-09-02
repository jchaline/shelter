package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Shelter;

public interface ShelterDao extends JpaRepository<Shelter, Long> {
	
}
