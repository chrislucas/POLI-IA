package com.br.exerc.poli.actions;


/**
 * @author christoffer
 * 
 * interface para criar um contrato entre o tipo de representacao
 * do mundo Wumpus (Matrix de adjacencia ou Lista de adjacencia)
 * e a forma que se faz a busca nesses mundos (BFS ou DFS
 * que sao implementadas de formas diferentes por serem representações diferentes de um grafo)
 * 
 * */

public interface TypeSearches {
	
	// metodo de busca
	public boolean search(int sx, int sy, int dx, int dy);
	public boolean run();
}
