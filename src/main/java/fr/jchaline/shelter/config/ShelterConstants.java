package fr.jchaline.shelter.config;

public class ShelterConstants {
	
	/**
	 * Space for rooms per floor
	 */
	public static final int FLOOR_SIZE = 18;

	/**
	 * Max size for room (when merged)
	 */
	public static final int ROOM_MAX_SIZE = 6;
	
	public static final String POWER = "power";
	public static final String WATER = "water";
	public static final String FOOD = "food";
	public static final String ELEVATOR = "elevator";
	
	public static final int SPECIAL_MAX = 10;
	
	public static final int STREET_PER_CITY = 10;
	public static final int BUILDING_PER_STREET = 10;
	
	
	//TODO : move to config file
	public static final int SHELTER_COMPUTE_ALL = 100*1000;
	public static final int TEAM_EXPLORE = 100*1000;
	public static final int TEAM_RECRUITMENT = 100*1000;
	public static final int TEAM_FIGHT = 100*1000;
	
}
