package fr.jchaline.shelter.service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.DwellerDao;
import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.utils.AlgoUtils;

@Service
@Transactional(readOnly = true)
public class DwellerService {
	
	private static final List<String> NAMES = Arrays.asList("O'Neill", "Jackson", "Carter", "Hammond", "Quinn", "Mitchell", "Landry", "Doran", "Harriman", "Fraiser", "Siler", "Pierre");
	private static final List<String> MALE_FIRSTNAMES = Arrays.asList("Jack", "Daniel", "George", "Jonas", "Cameron", "Hank", "Walter", "Sylvester", "Jean-Jaques");
	private static final List<String> FEMALE_FIRSTNAMES = Arrays.asList("Samantha", "Janet", "Amanda", "Claudia", "Patricia", "Elyse", "Sonya", "Sharon", "Dawn", "Claire");
	
	@Autowired
	private DwellerDao dao;
	
	@Autowired
	private SpecialService specialService;
	
	public List<Dweller> list() {
		return dao.findAll();
	}
	
	public Dweller get(long dwellerId) {
		return dao.findOne(dwellerId);
	}
	
	public Page<Dweller> paginate(int pageNumber, int offset) {
		PageRequest pageRequest = new PageRequest(
			pageNumber, offset, new Sort(
					new Order(Direction.ASC, "nickname")
			)
		);
		return dao.findAll(pageRequest);
	}
	
	/**
	 * Generate random dweller
	 * @return the dweller
	 * @throws Exception 
	 */
	public Dweller generate() {
		boolean male = new Random().nextBoolean();
		String name = AlgoUtils.rand(NAMES);
		String firstname = AlgoUtils.rand(male ? MALE_FIRSTNAMES : FEMALE_FIRSTNAMES);
		return new Dweller(male, name, firstname, specialService.randForDweller(4));
	}

}
