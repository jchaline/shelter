package fr.jchaline.shelter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jchaline.shelter.domain.Dweller;
import fr.jchaline.shelter.service.DwellerService;

@RestController
@RequestMapping(value = "/dweller", method = RequestMethod.GET)
public class DwellerController extends AbstractShelterController {
	
	@Autowired
	private DwellerService service;
	
	@RequestMapping("/get")
	public Dweller get(@RequestParam long dwellerId) {
		return service.get(dwellerId);
	}
	
	@RequestMapping("/list")
	public List<Dweller> list() {
		return service.list();
	}
	
	@RequestMapping("/paginate/{pageNumber}/{offset}")
	public Page<Dweller> paginate(@PathVariable int pageNumber, @PathVariable int offset, @RequestParam String order, @RequestParam Direction direction) {
		return service.paginate(pageNumber, offset, order, direction);
	}

}
