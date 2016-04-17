package com.br.exerc.poli.entities;

public class Monster extends Entity {

	private String ID;
	
	public Monster() {
		this.ID = "MO   ";
	}
	
	@Override
	public String returnID() {
		return this.ID;
	}

}
