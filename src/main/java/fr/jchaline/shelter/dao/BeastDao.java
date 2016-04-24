package fr.jchaline.shelter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Beast;

public interface BeastDao  extends JpaRepository<Beast, Long> {
	
	List<Beast> findByLevelBetween(int min, int max);

}
