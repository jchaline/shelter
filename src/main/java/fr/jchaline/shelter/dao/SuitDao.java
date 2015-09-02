package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Suit;

public interface SuitDao extends JpaRepository<Suit, Long> {

}
