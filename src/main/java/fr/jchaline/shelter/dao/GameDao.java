package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Game;

public interface GameDao extends JpaRepository<Game, Long> {

}
