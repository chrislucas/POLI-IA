package com.br.exerc.poli.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author christoffer
 * 
 * classe que representa o personagem que está buscando o objetivo no grafo
 * 
 * */

public class Actor extends Entity {

	private int arrows, points;
	public static final HashMap<Integer, String> map;
	private String orientation;
	public static final int NORTE = 0, SUL = 1, LESTE = 2, OESTE = 3;
	
	static {
		map = new HashMap<>();
		map.put(NORTE, "AC NO");
		map.put(SUL, "AC SU");
		map.put(LESTE, "AC LE");
		map.put(OESTE, "AC OS");
	}
	
	public Actor() {
		this.arrows = new Random().nextInt(1) + 1;
		this.orientation = map.get(2);
		this.points = 100;
	}
	
	public Actor(int arrows, int points) {
		this.arrows = arrows;
		this.orientation = map.get(2);
		this.points = points;
	}
	
	public int getArrows() {
		return arrows;
	}
		

	public void ahoot() {
		if(this.arrows == 0) {
			new UnsupportedOperationException("Arma sem munição");
		}
		this.arrows--;
		return;	
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setOrientation(int n) throws Exception {
		if( ! map.containsKey(n) )
			throw new Exception("Não essa direção");
		
		this.orientation = map.get(n);
	}
	
	public String getOrientation() {
		return orientation;
	}
	
	@Override
	public String returnID() {
		// TODO Auto-generated method stub
		return orientation;
	}

}
