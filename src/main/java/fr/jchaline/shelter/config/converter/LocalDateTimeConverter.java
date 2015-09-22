package fr.jchaline.shelter.config.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter  implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDateTime date) {
		return toDate(date);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		return toLocalDateTime(dbData);
	}
	
	public static Date toDate(LocalDateTime ldt) {
		return ldt != null ? Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()) : null;
	}
	
	public static LocalDateTime toLocalDateTime(Date date) {
		return date != null ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
	}

}
