package fr.jchaline.shelter.enums;

public enum JobEnum {
	//nothing!
	NEWBIE(1, 1, 1),
	//speed, life
	EXPLORER(2, 5, 10),
	//attack, life
	WARRIOR(8, 5, 2);
	
	public int speed;
	public int attack;
	public int life;
	
	private JobEnum(int attack, int life, int speed){
		this.speed = speed;
		this.attack = attack;
		this.life = life;
	}
}
