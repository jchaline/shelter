package fr.jchaline.shelter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.jchaline.shelter.domain.Room;

public interface RoomDao extends JpaRepository<Room, Long> {
	
	@Query("SELECT r FROM Floor f JOIN f.rooms r JOIN r.cells c WHERE f.id = :idFloor AND c = :cell ")
	List<Room> findByFloorAndCell(@Param(value = "idFloor") long idFloor, @Param(value = "cell") int cell);

	@Query("SELECT r FROM Floor f JOIN f.rooms r JOIN r.cells c WHERE f.id = :idFloor AND c = :cell ")
	Room findOneByFloorAndCell(@Param(value = "idFloor") long idFloor, @Param(value = "cell") int cell);
	
	@Query("SELECT r FROM Floor f JOIN f.rooms r JOIN r.cells c WHERE f.id = (SELECT f.id FROM Floor f JOIN f.rooms r WHERE r.id = :idRoom) AND (c = :cellLeft OR c = :cellRight) ")
	List<Room> findNeighbors(@Param(value = "idRoom") long idRoom, @Param(value = "cellLeft") int cellLeft, @Param(value = "cellRight") int cellRight);

}
