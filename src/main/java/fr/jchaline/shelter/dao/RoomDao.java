package fr.jchaline.shelter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.jchaline.shelter.domain.Room;

public interface RoomDao extends JpaRepository<Room, Long> {
	
	@Query("SELECT r FROM Floor f JOIN f.rooms r JOIN r.cells c WHERE f.id = :idFloor AND :cell = c")
	List<Room> findByFloorAndCell(@Param(value = "idFloor") long idFloor, @Param(value = "cell") int cell);

	@Query("SELECT r FROM Floor f JOIN f.rooms r JOIN r.cells c WHERE f.id = :idFloor AND :cell = c")
	Room findOneByFloorAndCell(@Param(value = "idFloor") long idFloor, @Param(value = "cell") int cell);

}
