package fr.jchaline.shelter.domain;

public interface Fighter {
	
	/**
	 * Level of the entity
	 * @return the level
	 */
	int getLevel();
	
	/**
	 * Number of action per turn
	 * @return the number of action per turn
	 */
	int attackPerTurn();
	
	/**
	 * The number of damages
	 * @param target 
	 * @return the damages
	 */
	int computeDamage(Fighter target);
	
	int getLife();
	
	void takeDamage(int damage);
	
	int getSpeed();
	
	/**
	 * Compute the accuracy between two entity : [10, 95] %
	 * 85% if same level, +/- 3 % per level
	 * @param other the entity attacked
	 * @return the accuracy, between 0 and 1
	 */
	public default double computeAccuracy(Fighter other) {
		return Math.min(0.95, Math.max(20.0, 85 - (other.getLevel() - this.getLevel()) * 3));
	}
	
	/**
	 * TODO : improve this
	 * Compute the experience 
	 * @return
	 */
	public default int computeExperience() {
		return 5 * getLevel();
	}

}
