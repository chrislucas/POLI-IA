package com.br.exerc.poli.actions;

import java.util.LinkedList;
import java.util.Queue;

import com.br.exerc.poli.entities.EmptyLocal;
import com.br.exerc.poli.entities.Monster;
import com.br.exerc.poli.worlds.Matrix;
import com.br.exerc.poli.worlds.Matrix.Node;

/**
 * 
 * Essa classe representa um tipo de busca, Breadth First Search (Busca em largura)
 * 
 * classe resposavel por realizar a busca no labirinto usando a estratégia de expandir
 * primeiro os primeiros Nodes encontrados
 * 
 * */

public class BFS implements TypeSearches {

	Matrix matrix;
	
	@Override
	public void search(int sx, int sy, int dx, int dy) {
		// TODO Auto-generated method stub
		Queue<Node> queue = new LinkedList<>();
		queue.add(matrix.getSource());
		// BFS, uma busca em largura afim de procurar a caverna com o pote de ouro
		while(queue.isEmpty()) {
			Node top = queue.poll();
			int x = top.getX(),
				y = top.getY();
			matrix.setWorld(x, y, matrix.getActor());
			
			// se o no expandido for o No destino acabou a busca
			if(top.equals(matrix.getDestiny())) {
				System.out.println("ENCONTREI O OURO");
				break;
			}
			
			for(Node next : matrix.validateStep(top.getX(), top.getY())) {
				
				if(next.getEntity() instanceof Monster) {
					// se for necessario gastar uma flecha com o monstro
					// avaliar as cavernas adjacentes ao monstro e mudar
					// a condicao de mal cheiro delas
					for(Node nb : matrix.validateStep(next.getX(), next.getY())) {
						x = nb.getX();
						y = nb.getY();
						matrix.setWorld(x, y, new Node(x, y, new EmptyLocal(false, false)));
					}
				}
				
				x = next.getX();
				y = next.getY();
				if( ! matrix.isVisited(x, y)) {
					matrix.setVisited(x, y,true);
					queue.add(next);
				}
			}
		}
		
		// uma nova busca em largura a fim de buscar a saida
		matrix.setVisited(new boolean[matrix.getDimensionX()][matrix.getDimensionY()]);
		queue = new LinkedList<>();
		queue.add(matrix.getDestiny());
		while(queue.isEmpty()) {
			Node top = queue.poll();
			if(top.equals(matrix.getSource())) {
				System.out.println("ENCONTREI A SAIDA");
				break;
			}
			
			for(Node next : matrix.validateStep(top.getX(), top.getY())) {
				int x = next.getX(), y = next.getY();
				if( ! matrix.isVisited(x, y)) {
					
					matrix.setVisited(x, y,true);
					queue.add(next);
				}
			}
		}
	}
	
	
	public void run() {
		matrix = new Matrix(8, 10);
		int sx = matrix.getSource().getX();
		int sy = matrix.getSource().getY();
		int dx = matrix.getDestiny().getX();
		int dy = matrix.getDestiny().getY();
		search(sx, sy, dx, dy);
	}

	public static void main(String[] args) {
		new BFS().run();
	}

}
