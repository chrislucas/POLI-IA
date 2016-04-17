package com.br.exerc.poli.actions;

/**
 * Interface que representa uma acao de uma Entidade
 * Por exemplo, na classe Actor(personagem que vaga pelo labirinto)
 * a acao e atirar a flecha.
 * na classe Monster pode ser atacar o Actor
 * 
 * */

public interface Action {
	public void action();
}
