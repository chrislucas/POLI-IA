package com.br.exerc.poli.worlds;

import java.util.ArrayList;
import java.util.Random;
import com.br.exerc.poli.entities.Actor;
import com.br.exerc.poli.entities.EmptyLocal;
import com.br.exerc.poli.entities.Entity;
import com.br.exerc.poli.entities.Goal;
import com.br.exerc.poli.entities.Hole;
import com.br.exerc.poli.entities.Monster;

/**
 * 
 * @author christoffer
 * representacao do labirinto atraves de uma matriz de adjacencia
 * */

public class Matrix {
	
	public static class Node  {
		private Entity entity;		// Entidade que esse no possui
		private int x, y;			// posicao na matrix
		
		public Node(int x, int y, Entity entity) {
			this.x = x;
			this.y = y;
			this.entity = entity;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public Entity getEntity() {
			return entity;
		}
	}
	
	
	private Node [][] world;				// matriz que representa o labirinto
	private Node source, destiny;			// No origem e no destino
	private boolean [][] visited;			// matriz para marcar nos visitados
	private int dimensionX, dimensionY;		// limites do mundo
	
	Entity actor, goal;
	
	public Node getSource() {
		return source;
	}
	
	public Node getDestiny() {
		return destiny;
	}
	
	public int getDimensionX() {
		return dimensionX;
	}
	
	public int getDimensionY() {
		return dimensionY;
	}
	
	public Node[][] getWorld() {
		return world;
	}
	
	
	/**
	 * Esse metodo sera util caso o personagem atire num monstro
	 * dessa forma podemos mudar o cenario, apagando o monstro e
	 * as consequencias que ele causa ao redor dele (mal cheiro)
	 * 
	 * */
	public void setWorld(int x, int y, Node node) {
		this.world[x][y] = node;
	}
	
	/**
	 *  Metodo que facilita o trabalho de reposicionar o personagem
	 *  ou ate mesmos os monstros caso queira implementar uma versao
	 *  onde os monstrons andem
	 * */
	public void setWorld(int x, int y, Entity entity) {
		this.world[x][y].entity = entity;
	}
	
	public Entity getActor() {
		return actor;
	}
	
	public Matrix(int dx, int dy) {
		world 	= new Node[dx][dy];
		visited = new boolean[dx][dy];
		this.dimensionX = dx;
		this.dimensionY = dy;
		populateWorld();
		statusMaze();
	}
	
	public boolean isVisited(int dx, int dy){
		return visited[dx][dy];
	}
	
	public void setVisited(int x, int y, boolean value) {
		this.visited[x][y] = value;
	}
	
	public void setVisited(boolean [][] visited) {
		this.visited = visited;
	}
	
	private void populateWorld() {
		Random random  = new Random();
		int maxQM = (int) (dimensionX * dimensionY * .05F), maxQH = (int)(dimensionY * dimensionY * .1F), countQM = 0, countQH = 0;
		int dx = 0, dy = 0, sx = 0, sy = 0;
		do {
			//int n = (int)System.nanoTime() % this.dimensionX;
			dx = random.nextInt(this.dimensionX);
			//n = (int)System.nanoTime() % this.dimensionY;
			dy = random.nextInt(this.dimensionY);
			//n = (int)System.nanoTime() % this.dimensionX;
			sx = random.nextInt(this.dimensionX);
			//n = (int)System.nanoTime() % this.dimensionY;
			sy = random.nextInt(this.dimensionY);
		} while(dx == sx || dy == sy);
		
		actor = new Actor();
		goal = new Goal();
		
		source  = new Node(sx, sy, actor);
		destiny = new Node(dx, dy, goal);
		world[sx][sy] = source;
		world[dx][dy] = destiny;
		
		for(int i=0; i<this.dimensionX; i++) {
			for(int j=0; j<this.dimensionY; j++) {
				// se for a posicao da fonte ou do destino
				// prossiga
				if(i == dx && j == dy|| i == sx && j == sy)
					continue;
				
				// gerar um numero randomico entre 1 e 3, para
				// posicionar os Elementos no labiriton
				// ELemento 1 - Monstros
				// Elemento 2 - Buraco
				// Elemento 3 - Uma sala vazia, porem
				// essa sala vazia pode ter um monstro ou um buraco a sua volta
				// salar com Elementos 1 ou 2 a sua volta possuem uma indicação
				// ou essa sala venta ou ela possui odor ruim
				// A classe EmptyLocal possui 2 atributos booleanos que indicam
				// se a sala venta ou possui odor ruim.
				
				int rx = random.nextInt(3 - 1) + 1;
				Node e = null;
				if(rx == 1 && countQM  < maxQM) {
					e = new Node(i, j, new Monster());
					countQM++;
				} else if(rx == 2 && countQH < maxQH) {
					e = new Node(i, j, new Hole());
					countQH++;
				} else {
					// verificar se esse Node nao tem na sua proximidade
					// um buraco
					// se tiver, devemos atribuir VERDADE para o atributo
					// ventas da classe EmptyLocal
					
					
					// verificar se esse Node nao tem na sua proximidade
					// um monstro
					// se tiver, devemos atribuir VERDADE para o atributo
					// 'mal cheiro'
					
					// se eh uma posicao no centro, olhar para os quatro lados
					ArrayList<Node> neighboors = validateStep(i, j);
					
					for(Node node : neighboors) {
						if(node != null) {
							Entity entity = node.getEntity();
							if(entity instanceof Monster) {
								e = new Node(i, j, new EmptyLocal(false, true));
								break;
							} else if(entity instanceof Hole) {
								e = new Node(i, j, new EmptyLocal(true, false));
								break;
							} 
						}
					}
					
					if(e == null)
						// do contrario, esse lugar é um local vazio, que não da para inferir nada
						e = new Node(i, j, new EmptyLocal(false, false));	

				}
				world[i][j] = e == null ? new Node(i, j, new EmptyLocal(false, false)) : e; 
			}
		}
	}
		
	public ArrayList<Node> validateStep(int x, int y) {
		ArrayList<Node> neighboors = new ArrayList<>(); 
		if(x < this.dimensionX - 1) {
			neighboors.add(world[x+1][y]);	// cima
		}
		
		if(x > 0) {
			neighboors.add(world[x-1][y]); // baixo
		}
		
		if(y < this.dimensionY - 1) {
			neighboors.add(world[x][y+1]); // direita
		}
		
		if(y > 0) {
			neighboors.add(world[x][y-1]);	// esquerda
		}
		
		return neighboors;
	}
	
	public void statusMaze() {
		for (int i = 0; i <dimensionX; i++) {
			for (int j = 0; j < dimensionY; j++) {
				System.out.printf("%s ", world[i][j].getEntity().returnID());
			}
			System.out.println("");
		}
	}


/*	
	public static void main(String[] args) {
		Matrix matrix = new Matrix(8, 10);
		int sx = matrix.source.getX();
		int sy = matrix.source.getY();
		int dx = matrix.destiny.getX();
		int dy = matrix.destiny.getY();
		matrix.search(sx, sy, dx, dy);
	}
*/	
	
}
