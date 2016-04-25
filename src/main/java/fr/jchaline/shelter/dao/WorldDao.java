package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.World;

public interface WorldDao  extends JpaRepository<World, Long> {
	
	World findByName(String name);

}
