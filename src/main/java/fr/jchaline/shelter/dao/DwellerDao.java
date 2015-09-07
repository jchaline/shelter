package fr.jchaline.shelter.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.domain.Room;

public interface DwellerDao extends JpaRepository<Dweller, Long> {
	
	Page<Dweller> findAll(Pageable pageable);
	
	List<Dweller> findAllByRoom(Room room);
	
}
