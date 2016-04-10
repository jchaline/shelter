package fr.jchaline.shelter.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jchaline.shelter.domain.Message;

public interface MessageDao extends JpaRepository<Message, Long> {
	
}
