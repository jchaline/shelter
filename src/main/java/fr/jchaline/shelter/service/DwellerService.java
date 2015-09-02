package fr.jchaline.shelter.service;

import java.util.List;

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

@Transactional(readOnly = true)
@Service
public class DwellerService {
	
	@Autowired
	private DwellerDao dao;
	
	public List<Dweller> list() {
		return dao.findAll();
	}
	
	public Page<Dweller> paginate(int pageNumber, int offset) {
		PageRequest pageRequest = new PageRequest(
				  	pageNumber, offset, new Sort(
				    new Order(Direction.ASC, "nickname")
				  )
				);
		return dao.findAll(pageRequest);
	}

}
