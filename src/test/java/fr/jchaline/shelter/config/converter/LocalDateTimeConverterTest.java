package fr.jchaline.shelter.config.converter;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import fr.jchaline.shelter.config.converter.LocalDateTimeConverter;

@RunWith(MockitoJUnitRunner.class)
public class LocalDateTimeConverterTest {
	
	
	@Test
	public void toDate() {
		LocalDateTime ldt = LocalDateTime.now();
	    Date date = LocalDateTimeConverter.toDate(ldt);
	    LocalDateTime ldt2 = LocalDateTimeConverter.toLocalDateTime(date);
	    Assert.isTrue(ldt.isEqual(ldt2));
	}

	@Test
	public void toLocalDateTime() {
	    Date date = new Date();
	    LocalDateTime localDateTime = LocalDateTimeConverter.toLocalDateTime(date);
	    Date date2 = LocalDateTimeConverter.toDate(localDateTime);
	    Assert.isTrue(!date.after(date2));
	    Assert.isTrue(!date2.after(date));
	}
}
