package fr.jchaline.shelter.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table
public class Special extends AbstractEntity {

	@Column(nullable = false)
	@Min(1)
	private int s;

	@Column(nullable = false)
	@Min(1)
	private int p;
	
	@Column(nullable = false)
	@Min(1)
	private int e;
	
	@Column(nullable = false)
	@Min(1)
	private int c;
	
	@Column(nullable = false)
	@Min(1)
	private int i;
	
	@Column(nullable = false)
	@Min(1)
	private int a;
	
	@Column(nullable = false)
	@Min(1)
	private int l;

	public Special(int s, int p, int e, int c, int i, int a, int l) {
		this.setS(s);
		this.setP(p);
		this.setE(e);
		this.setC(c);
		this.setI(i);
		this.setA(a);
		this.setL(l);
	}
	
	public Special(List<Integer> special) {
		int i = 0;
		this.setS(special.get(i++));
		this.setP(special.get(i++));
		this.setE(special.get(i++));
		this.setC(special.get(i++));
		this.setI(special.get(i++));
		this.setA(special.get(i++));
		this.setL(special.get(i++));
	}

	public Special() {
		
	}
	
	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getE() {
		return e;
	}

	public void setE(int e) {
		this.e = e;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getL() {
		return l;
	}

	public void setL(int l) {
		this.l = l;
	}
	
	
}
