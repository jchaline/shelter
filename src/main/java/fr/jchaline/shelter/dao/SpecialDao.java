package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Special;

public interface SpecialDao extends JpaRepository<Special, Long> {

}
