package fr.jchaline.shelter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Duty;
import fr.jchaline.shelter.domain.Team;

public interface TeamDao  extends JpaRepository<Team, Long> {
	
	/**
	 * Search Team with duty
	 * @param duty The duty
	 * @return The team with this duty
	 */
	List<Team> findByDuty(Duty duty);

}
