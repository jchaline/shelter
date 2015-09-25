package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Player;

public interface PlayerDao extends JpaRepository<Player, Long> {

}
