package fr.jchaline.shelter.service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jchaline.shelter.config.ShelterConstants;
import fr.jchaline.shelter.dao.ItemDao;
import fr.jchaline.shelter.dao.SuitDao;
import fr.jchaline.shelter.dao.WeaponDao;
import fr.jchaline.shelter.domain.Item;
import fr.jchaline.shelter.domain.Suit;
import fr.jchaline.shelter.domain.Weapon;
import fr.jchaline.shelter.utils.AlgoUtils;

@Transactional(readOnly = true)
@Service
public class ItemService {

	public static final List<Triplet<String, Integer, Integer>> WEAPONS_LIST = Arrays.asList(
			Triplet.with("knife", 4, 0), Triplet.with("sword", 7, 0), Triplet.with("katana", 10, 0),
			Triplet.with("gun", 3, 3), Triplet.with("riffle", 7, 6), Triplet.with("sniper", 10, 10),
			Triplet.with("rocket_launcher", 15, 6));

	public static final List<Pair<String, Integer>> SUITS_LIST = Arrays.asList(Pair.with("suit", 1),
			Pair.with("leather", 5), Pair.with("kelvar", 10), Pair.with("cortosis", 15));

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

	public Weapon randWeapon(int requiredLevel) {
		int iLevel = randILevel(requiredLevel);
		// 2) rand object
		Triplet<String, Integer, Integer> w = AlgoUtils.rand(WEAPONS_LIST);
		// 3) make object with ilvl
		return new Weapon(w.getValue0(), w.getValue1() * iLevel, w.getValue2() * iLevel, iLevel, requiredLevel);
	}

	public Suit randSuit(int requiredLevel) {
		int iLevel = randILevel(requiredLevel);
		// 2) rand object
		Pair<String, Integer> s = AlgoUtils.rand(SUITS_LIST);
		// 3) make object with ilvl
		return new Suit(s.getValue0(), s.getValue1() * iLevel, iLevel, requiredLevel);
	}

	private int randILevel(int requiredLevel) {
		// 1) rand ilvl with requiredLevel
		int iLevelBonus = new Random().nextInt(ShelterConstants.I_LEVEL_PER_LEVEL * ShelterConstants.NB_LEVEL_RANGE);
		int iLevelMin = (requiredLevel - ShelterConstants.NB_LEVEL_RANGE) * ShelterConstants.I_LEVEL_PER_LEVEL;
		int iLevel = iLevelBonus + iLevelMin;
		return iLevel;
	}
}
