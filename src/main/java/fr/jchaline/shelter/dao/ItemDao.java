package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Item;

public interface ItemDao extends JpaRepository<Item, Long> {

}
