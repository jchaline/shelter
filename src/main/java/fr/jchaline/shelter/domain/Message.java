package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Message extends AbstractEntity {
	
	@Column
	private String content;
	
	public Message(String content) {
		this.content = content;
	}
	
	public Message() {
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
