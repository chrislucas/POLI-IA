package com.br.exerc.poli.worlds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
		private Node ancestor;		// node de origem
		private int x, y;			// posicao na matrix
		
		public Node(int x, int y, Entity entity) {
			this.x = x;
			this.y = y;
			this.entity = entity;
		}
		
		public void setAncestor(Node ancestor) {
			this.ancestor = ancestor;
		}
		
		public Node getAncestor() {
			return ancestor;
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
	
	
	private Node [][] world;					// matriz que representa o labirinto
	private Node source, destiny;				// No origem e no destino
	private boolean [][] visited;				// matriz para marcar nos visitados
	private int dimensionX, dimensionY;			// limites do mundo
	
	private Entity actor, goal;
	
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
	
	/**
	 * Metodo util para saber sobre um determinado Node
	 * @param x
	 * @param y
	 * */
	public Node getPlace(int x, int y) {
		return this.world[x][y];
	}
	
	
	public Actor getActor() {
		return (Actor)actor;
	}
	
	public Matrix(int dx, int dy) {
		world 	= new Node[dx][dy];
		visited = new boolean[dx][dy];
		this.dimensionX = dx;
		this.dimensionY = dy;
		populateWorld(generatePositionEntities());
		//populateWorld();
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
	
	/**
	 * Esse metodo me permite posicionar os elementos no Mapa de forma aleatorio
	 * Atraves da classe Random do pacote java.util, gero numeros aleatorios
	 * entre 0 e a ORDEM DA MATRIZ para posicionar as ENtidades num local aleatorio
	 * */

	private Map<Integer, Entity> generatePositionEntities() {
		Set<Integer> set = new HashSet<Integer>();			// conjunto de inteiros entre 0 e DimensaoX * DimensaoY
		Map<Integer, Entity> map = new HashMap<>();			// mapa com a posicao no mundo e a entidade que sera posicionada
		Random entities = new Random();						// gerador randomico de
		Random position = new Random();
		// dimensao do mundo
		int dim = (dimensionX) * (dimensionY);	// dimensao
		// um bom lembrete, parâmetro em nextInt nao pode ser 0
		Integer pos = position.nextInt(dim);
		// posicao do ator
		set.add(pos);
		map.put(pos, new Actor());
		do {
			pos = position.nextInt(dim);
		} while(set.contains(pos));
		// posicao do objetivo
		set.add(pos);
		map.put(pos, new Goal());		
		
		Integer max = 0;
		
		int maxQM = (int) (dim * .05F)		// maxima quantidade de monstros 0.5% do tamanho do mapa
			,maxQH = (int)(dim * .1F)		// maxima quantidade de buracos 10 % do tamanho do mapa
			,countQM = 0										// contador de monstros
			,countQH = 0;										// contador de buracos
		int counter = 0;
		do {
			int i = entities.nextInt(3);
			Entity e = null;
			if(i == 0 && countQM < maxQM) {
				e = new Monster();
				countQM++;
			} else if(i == 1 && countQH  < maxQH) {
				e = new Hole();
				countQH++;
			} else {
				e = new EmptyLocal(false, false);
			}
			pos = position.nextInt(dim);
			if(pos > max)
				max = pos;			
			if(set.contains(pos)) {
				Integer aux = pos;
				do {
					pos = position.nextInt(Math.abs(max - aux) + 1) + aux;
					aux++;
					if(aux >= max) {
						aux = 0;
						counter++;
					} 
					// tenta 2 vezes gerar numeros randomicos
					// se nao conseguir so adiciona o que esta faltando
					else if(counter > 1) {
						for(int d=0; d<dim; d++) {
							if(!set.contains(d)) {
								pos = d;
								counter = 0;
								break;
							}
						}							
					}
				} while (set.contains(pos));
				set.add(pos);
				map.put(pos, e);
			} else {
				set.add(pos);
				map.put(pos, e);
			}
			if(pos > max)
				max = pos;	
		} while(set.size() < dim);
		return map;
	}
	
	public void populateWorld(Map<Integer, Entity> map) {
		for(Map.Entry<Integer, Entity> entry : map.entrySet()) {
			int pos = entry.getKey();
			/*
			 * Calculo para converter uma psicao mat(i,j) para um inteiro
			 * i * (DIMENSAOX) + j se DIMENSAO X == DIMENSAO Y
			 * i * (DIMENSAOX) + (i + j) se as DIMENSAO X != DIMENSAO Y
			 * */
			int x = 0, y = 0;
			if(dimensionX == dimensionY) {
				x = pos / (dimensionX);
				y = pos % (dimensionY);
			}

			Entity entity = entry.getValue();
			Node node = null;
			
			// definindo o ponto de partida e chegada
			if(entity instanceof Actor) {
				actor 	= (Actor) entity;
				source  = new Node(x, y, actor);				
			} else if (entity instanceof Goal) {
				goal 	= (Goal) entity;
				destiny = new Node(x, y, goal);
			}
			
			// se o Node for um espaco vazio devo olhar nos Node adjacentes
			else if(entity instanceof EmptyLocal) {
				// verificar se esse Node nao tem na sua proximidade
				// um buraco
				// se tiver, devemos atribuir VERDADE para o atributo
				// ventas da classe EmptyLocal
				
				
				// verificar se esse Node nao tem na sua proximidade
				// um monstro
				// se tiver, devemos atribuir VERDADE para o atributo
				// 'mal cheiro'
				
				// se eh uma posicao no centro, olhar para os quatro lados
				ArrayList<Node> neighboors = validateStep(x, y);
				for(Node neighboor : neighboors) {
					if(neighboor != null) {
						Entity e = neighboor.getEntity();
						if(e instanceof Monster) {
							node = new Node(x, y, new EmptyLocal(false, true));
							break;
						} else if(e instanceof Hole) {
							node = new Node(x, y, new EmptyLocal(true, false));
							break;
						} 
					}
				}
				
				if(node == null)
					// do contrario, esse lugar é um local vazio, que não da para inferir nada
					node = new Node(x, y, new EmptyLocal(false, false));
			} 
			// senao for um espaco vazio, nao importa o que seja, por polimorfismo, criamos
			// um Node com
			else {
				node = new Node(x, y, entity);
			}
			setWorld(x, y, node);
		}
	}
	
	public void populateWorld() {
		Random random  = new Random();
		int maxQM = (int) (dimensionX * dimensionY * .05F)		// maxima quantidade de monstros 0.5% do tamanho do mapa
			,maxQH = (int)(dimensionY * dimensionY * .1F)		// maxima quantidade de buracos 10 % do tamanho do mapa
			,countQM = 0										// contador de monstros
			,countQH = 0;										// contador de buracos
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
		
		actor 	= new Actor();
		goal 	= new Goal();
		
		source  = new Node(sx, sy, actor);
		destiny = new Node(dx, dy, goal);
		setWorld(sx, sy, source);
		setWorld(dx, dy, destiny);
		
		//world[sx][sy] = source;
		//world[dx][dy] = destiny;
		
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
				// Se Entity e for nulo, colocar como um lugar vazio, senao vale o proprio 'e'
				//e = e == null ? new Node(i, j, new EmptyLocal(false, false)) : e;
				setWorld(i, j, e);
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
		System.out.println("");
		return;
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
