package fr.jchaline.shelter.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class AlgoUtilsTest {
	
	@Test
	public void nseat() throws Exception {
		List<Integer> input = Arrays.asList(100, 50, 100, 50, 100, 50, 100);
		List<Integer> expected = Arrays.asList(2, 1, 2, 1, 2, 1, 2);
		
		List<Integer> output = AlgoUtils.nseat(11, input);
		
		assertEquals(expected, output);
	}

}
