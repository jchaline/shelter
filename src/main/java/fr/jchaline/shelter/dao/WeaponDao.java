package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Weapon;

public interface WeaponDao extends JpaRepository<Weapon, Long> {

}
