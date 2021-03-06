package com.br.exerc.poli.entities;

/**
 * @author christoffer
 * Essa classe representa uma local no labirinto que é possível transitar livremente
 * Esse local pode ventar, o que representa que esta proximo ao um buraco, ou pode
 * cheirar mal, o que representa que 
 * 
 * */

public class EmptyLocal extends Entity {
	
	private boolean windy, badSmell;
	
	public EmptyLocal(boolean windy, boolean badSmell) {
		this.windy = windy;
		this.badSmell = badSmell;
	}
	
	public EmptyLocal() {
		
	}

	public boolean isWindy() {
		return windy;
	}

	public boolean isBadSmell() {
		return badSmell;
	}
	
	public void setBadSmell(boolean badSmell) {
		this.badSmell = badSmell;
	}
	
	public void setWindy(boolean windy) {
		this.windy = windy;
	}

	@Override
	public String returnID() {
		if(windy)
			return "EH   ";
		else if(badSmell)
			return "EM   ";
		else	
			return "EE   ";
	}
	
	
	
}
