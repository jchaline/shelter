package fr.jchaline.shelter.service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.domain.Special;
import fr.jchaline.shelter.utils.AlgoUtils;

@Transactional(readOnly = true)
@Service
public class SpecialService {

	/**
	 * Generate Special object 
	 * @param bonus The value distributed to the total
	 * @return the Special 
	 */
	public Special randForDweller(int bonus) {
		Random r = new Random();
		List<Integer> nseat = AlgoUtils.nseat(bonus, Arrays.asList(r.nextInt(1000), r.nextInt(1000), r.nextInt(1000), r.nextInt(1000), r.nextInt(1000), r.nextInt(1000), r.nextInt(1000)));
		List<Integer> special = nseat.stream().map(x -> x+1).collect(Collectors.toList());
		return new Special(special);
	}
}
