package fr.jchaline.shelter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.dao.ItemDao;
import fr.jchaline.shelter.dao.SuitDao;
import fr.jchaline.shelter.dao.WeaponDao;
import fr.jchaline.shelter.domain.Item;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Weapon;

@Transactional(readOnly = true)
@Service
public class ItemService {

	@Autowired
	private ItemDao dao;

	@Autowired
	private WeaponDao weaponDao;
	
	@Autowired
	private SuitDao suitDao;
	
	public List<Item> list() {
		return dao.findAll();
	}

	public List<Weapon> listWeapons() {
		return weaponDao.findAll();
	}

	public List<Suit> listSuits() {
		return suitDao.findAll();
	}
}
