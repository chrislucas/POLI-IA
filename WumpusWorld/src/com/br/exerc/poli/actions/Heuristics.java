package com.br.exerc.poli.actions;

public interface Heuristics {
	// metodo de heuristica que se aplica a uma matrix
	// cuja origem esta em MATRIX[sx][sy] e o destino em
	// MATRIX[dx][dy]
	public double heuristic(int sx, int sy, int dx, int dy);
	
}
