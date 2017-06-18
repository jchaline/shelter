package fr.jchaline.shelter.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
public class Message extends AbstractEntity {
	
	@Column
	private String content;
	
	public Message(String content) {
		this.content = content;
	}
	
	public Message() {
		this.content = StringUtils.EMPTY;
	}
}
