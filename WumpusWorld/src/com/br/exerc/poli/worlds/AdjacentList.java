package com.br.exerc.poli.worlds;

import java.util.ArrayList;

import com.br.exerc.poli.actions.TypeSearches;
import com.br.exerc.poli.entities.Entity;

public class AdjacentList implements TypeSearches {
	
	public static class Node  {
		private Entity entity;		// Entidade que esse no possui
		private int x, y;			// posicao na matrix
		
		public Node(int x, int y, Entity entity) {
			this.x = x;
			this.y = y;
			this.entity = entity;
		}
	}
	
	
	public ArrayList<Node> world;
	public boolean [] visited;
	public int vertices;
	
	public AdjacentList(int vertices) {
		this.world 		= new ArrayList<>();
		this.visited 	= new boolean[vertices];
		this.vertices	= vertices;
	}
	
	@Override
	public void search(int sx, int sy, int dx, int dy) {
		

	}

}
