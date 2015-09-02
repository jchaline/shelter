package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.RoomType;

public interface RoomTypeDao extends JpaRepository<RoomType, Long> {

}
