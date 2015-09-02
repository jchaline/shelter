package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Room;

public interface RoomDao extends JpaRepository<Room, Long> {

}
