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
	
	public static final int DWELLERS_MAX_LEVEL = 50;

	/**
	 * iLevel for object
	 */
	public static final int I_LEVEL_PER_LEVEL = 5;
	//nb level less than required level for compute ilevel
	public static final int NB_LEVEL_RANGE = 5;
	
	
	
	//TODO : move to config file
	/**
	 * Time for schedule turns
	 */
	public static final int SHELTER_COMPUTE_ALL = 10*1000;
	public static final int TEAM_EXPLORE = 10*1000;
	public static final int TEAM_RECRUITMENT = 10*1000;
	public static final int TEAM_FIGHT = 10*1000;
	
	/**
	 * Number of seconds to wait between each move. The lowest it is, the fastest all the teams are
	 */
	public static final int SECOND_BETWEEN_MOVE = 6;
	
}
