package fr.jchaline.shelter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.BeastDao;
import fr.jchaline.shelter.domain.Beast;
import fr.jchaline.shelter.utils.AlgoUtils;

@Transactional(readOnly = true)
@Service
public class BeastService {
	
	@Autowired
	private BeastDao beastDao;
	
	/**
	 * TODO : rules for making beasts group....
	 * @return
	 */
	public List<Beast> makeGroup(int size, double avgLevel) {
		//1 : compute level
		
		//2 : compute group size
		
		//3 : find beast
		
		List<Beast> range = beastDao.findByLevelBetween(1, (int) avgLevel + 10);
		
		List<Beast> asList = new ArrayList<>();
		for (int i = 0; i < size + new Random().nextInt(size); i++) {
			Beast clone = new Beast();
			BeanUtils.copyProperties(AlgoUtils.rand(range), clone);
			asList.add(clone);
			//TODO : assert modify clone doesn't modify origin
		}
		
		return asList;
	}

}
