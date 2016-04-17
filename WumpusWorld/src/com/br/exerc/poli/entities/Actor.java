package com.br.exerc.poli.entities;

import java.util.Random;

import com.br.exerc.poli.actions.Action;

/**
 * @author christoffer
 * 
 * classe que representa o personagem que está buscando o objetivo no grafo
 * 
 * */

public class Actor extends Entity {

	private int arrows;
	
	public Actor() {
		this.arrows = new Random().nextInt(8) + 1;
	}
	
	public Actor(int arrows) {
		this.arrows = arrows;
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
	
	@Override
	public String returnID() {
		// TODO Auto-generated method stub
		return "AC";
	}

}
