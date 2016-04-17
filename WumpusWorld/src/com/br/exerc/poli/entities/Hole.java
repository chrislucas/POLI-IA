package com.br.exerc.poli.entities;

public class Hole extends Entity {
	private String ID;
	
	public Hole() {
		// TODO Auto-generated constructor stub
		this.ID = "HO   ";
	}
	
	@Override
	public String returnID() {
		return this.ID;
	}

}
