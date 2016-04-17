package com.br.exerc.poli.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import com.br.exerc.poli.entities.Actor;
import com.br.exerc.poli.entities.EmptyLocal;
import com.br.exerc.poli.entities.Entity;
import com.br.exerc.poli.entities.Hole;
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

public class BFSMatrix implements TypeSearches {

	Matrix matrix;
	
	
	/**
	 * 
	 * Metodo que percorre o labirinto a partir de uma posicao Origem(sx, sy)
	 * para chegar ao Destino(dx, dy)
	 * 
	 * */
	
	@Override
	public boolean search(int sx, int sy, int dx, int dy) {
		// TODO Auto-generated method stub
		Queue<Node> queue = new LinkedList<>();
		queue.add(matrix.getSource());
		matrix.setVisited(matrix.getSource().getX(), matrix.getSource().getY(), true);
		// processar a fila na busca pelo objetivo
		if( ! processQueue(queue, "Encontrei o OURO !!!")) {
			return false;
		}

		// uma nova busca em largura afim de buscar a saida
		matrix.setVisited(new boolean[matrix.getDimensionX()][matrix.getDimensionY()]);
		queue = new LinkedList<>();
		queue.add(matrix.getDestiny());
		matrix.setVisited(matrix.getDestiny().getX(), matrix.getDestiny().getY(), true);
		
		// processar a fila agora para sair da cavernda
		return processQueue(queue, "Encontrei a saida !!!");	
	}
	
	public boolean processQueue(Queue<Node> queue, String successMessage) {
		// BFS, uma busca em largura afim de procurar a caverna com o pote de ouro
		while( ! queue.isEmpty() ) {
			Matrix.Node top = queue.poll();
			Matrix.Node anc = top.getAncestor();
			
			if( anc != null ) {
				// se valor de X do novo no for maior que do antecessor
				// o robo esta virado para LESTE
				if(top.getX() > anc.getX() ) {
					try {
						matrix.getActor().setOrientation(Actor.LESTE);
					} catch (Exception e) {};
				}
				// se valor de X do novo no for menor que do antecessor
				// o robo esta virado para OESTE
				else if (top.getX() < anc.getX() ){
					try {
						matrix.getActor().setOrientation(Actor.OESTE);
					} catch (Exception e) {};
				}
				// se valor de Y do novo no for maior que do antecessor
				// o robo esta virado para NORTE
				else if(top.getY() > anc.getY()) {
					try {
						matrix.getActor().setOrientation(Actor.NORTE);
					} catch (Exception e) {};
				}
				else {
					try {
						matrix.getActor().setOrientation(Actor.SUL);
					} catch (Exception e) {};
				}
			} // fim f ancestor
			
			
			int x = top.getX(),
				y = top.getY();
			int points = matrix.getActor().getPoints();
			
			// se o no expandido for o No destino acabou a busca
			if(top.equals(matrix.getDestiny())) {
				System.out.println(successMessage);
				matrix.getActor().setPoints(points + 1000);
				System.out.printf("%d pontos para o Robo\n", matrix.getActor().getPoints());
				return true;
			}
			points -= 10;
			
			matrix.getActor().setPoints(points);
			
			// reposiciona o Ator no labiritno
			matrix.setWorld(x, y, matrix.getActor());
			// Imprime o labirinto
			matrix.statusMaze();
			
			// a partir da posicao atual, buscar as cavernas adjacentes
			for(Node next : matrix.validateStep(top.getX(), top.getY())) {
				/*
				 * Ao entrar num Node o agente deve avaliar o local
				 * */
				Entity local = next.getEntity();
				// O local e um local vazio ?
				if(local instanceof EmptyLocal) {
					// se sim, eh um local seguro, entao verificamos
					// se é posśível identificar o que ocorre nos locais
					// adjacentes e inferir alguma coisa
					
					// esse local cheira mal
					if( ((EmptyLocal) local).isBadSmell() ) {
						// se sim, estamos proximos do monstro
						
						// se for necessario gastar uma flecha com o monstro
						// avaliar as cavernas adjacentes ao monstro e mudar
						// a condicao de mal cheiro delas
						if(matrix.getActor().getArrows() > 0)
							encontrarWumpus(x, y, matrix.getActor().getDirection());
					}
					
					// sentimos alguma brisa nesse local
					else if( ((EmptyLocal) local).isWindy() ) {
						// se sim estamos proximos de um buraco
					}
				}
				
				else if(local instanceof Monster) {
					System.out.println("Robo foi destruido pelo monstro");
					matrix.getActor().setPoints(points - 1000);
					return false;
				}
				
				// local possui um buraco
				else if( local instanceof Hole ) {
					// se sim, o robo caiu
					System.out.println("Robo caiu no buraco");
					matrix.getActor().setPoints(points - 1000);
					return false;
				}
				
				x = next.getX();
				y = next.getY();
				if( ! matrix.isVisited(x, y)) {
					matrix.setVisited(x, y,true);
					next.setAncestor(top);
					queue.add(next);
				}
			}
		}
		return false;
	}
	
	
	// ao atirar a flecha, ela voua em linha reta
	// atravessando as salas, até pegar no Wumpus ou numa parede
	/**
	 * @param
	 * int x, y posicao do
	 * 
	 * */
	public boolean encontrarWumpus(int actorPx, int actorPy, int direction) {
		// sea flecha encontrar o monstro retorna TRUE e modifique
		// as salas adjacentes
		boolean ans = false;
		switch (direction) {
			case Actor.LESTE:
				// a flecha navega para leste -> direita
				for(int y = actorPy; y < matrix.getDimensionX(); y++) {
					// verificar o No a direita, para onde a flecha prossegue
					Node place = matrix.getPlace(actorPx, y);
					// se o local que a flecha passou tem um monstro
					if(place.getEntity() instanceof Monster) {
						// limpa o lugar que tem o monstro
						matrix.setWorld(actorPx, y, new EmptyLocal(false, false));
						// remove o fedor das salas adijacentes;
						for(Node neighboor : matrix.validateStep(actorPx, y)) {
							// se for um buraco proximo ao monstro, no faça nada
							if(neighboor.getEntity() instanceof Hole)
								continue;
							int px = neighboor.getX();
							int py = neighboor.getY();
							
							// limpando  o lugar definindo o com um lugar vazio
							matrix.setWorld(px, py, new EmptyLocal(false, false));
						}
						System.out.println("GRUUUUUUUUUUUUUUHN - Wumpus Morreueueueueueueueue");
						ans = true;
					}
				}
				break;
			case Actor.OESTE:
				// flecha caimha para direita
				for(int y = actorPy; y >= 0; y--) {
					// verificar o No a direita, para onde a flecha prossegue
					Node place = matrix.getPlace(actorPx, y);
					// se o local que a flecha passou tem um monstro
					if(place.getEntity() instanceof Monster) {
						// limpa o lugar que tem o monstro
						matrix.setWorld(actorPx, y, new EmptyLocal(false, false));
						// remove o fedor das salas adijacentes;
						for(Node neighboor : matrix.validateStep(actorPx, y)) {
							// se for um buraco proximo ao monstro, no faça nada
							if(neighboor.getEntity() instanceof Hole)
								continue;
							int px = neighboor.getX();
							int py = neighboor.getY();
							
							// limpando  o lugar definindo o com um lugar vazio
							matrix.setWorld(px, py, new EmptyLocal(false, false));
						}
						System.out.println("GRUUUUUUUUUUUUUUHN - Wumpus Morreueueueueueueueue");
						ans = true;
					}
				}
				break;
			case Actor.NORTE:
				// a flecha caimnha para cima
				for(int x=actorPx; x>=0; x--) {
					// verificar o No a direita, para onde a flecha prossegue
					Node place = matrix.getPlace(x, actorPy);
					// se o local que a flecha passou tem um monstro
					if(place.getEntity() instanceof Monster) {
						// limpa o lugar que tem o monstro
						matrix.setWorld(x, actorPy, new EmptyLocal(false, false));
						// remove o fedor das salas adijacentes;
						for(Node neighboor : matrix.validateStep(x, actorPy)) {
							// se for um buraco proximo ao monstro, no faça nada
							if(neighboor.getEntity() instanceof Hole)
								continue;
							int px = neighboor.getX();
							int py = neighboor.getY();
							
							// limpando  o lugar definindo o com um lugar vazio
							matrix.setWorld(px, py, new EmptyLocal(false, false));
						}
						System.out.println("GRUUUUUUUUUUUUUUHN - Wumpus Morreueueueueueueueue");
						ans = true;
					}
				}
				break;
			case Actor.SUL:
				// a flecha caimha para baixo
				// a flecha caimnha para cima
				for(int x=actorPx; x<matrix.getDimensionX(); x++) {
					// verificar o No a direita, para onde a flecha prossegue
					Node place = matrix.getPlace(x, actorPy);
					// se o local que a flecha passou tem um monstro
					if(place.getEntity() instanceof Monster) {
						// limpa o lugar que tem o monstro
						matrix.setWorld(x, actorPy, new EmptyLocal(false, false));
						// remove o fedor das salas adijacentes;
						for(Node neighboor : matrix.validateStep(x, actorPy)) {
							// se for um buraco proximo ao monstro, no faça nada
							if(neighboor.getEntity() instanceof Hole)
								continue;
							int px = neighboor.getX();
							int py = neighboor.getY();
							
							// limpando  o lugar definindo o com um lugar vazio
							matrix.setWorld(px, py, new EmptyLocal(false, false));
						}
						System.out.println("GRUUUUUUUUUUUUUUHN - Wumpus Morreueueueueueueueue");
						ans = true;
					}
				}
				break;
			default:
				break;
		}
		
		// retorna se a flecha achou o monstro ou nao
		return ans;
	}
	
	
	public boolean run() {
		matrix = new Matrix(5, 5);
		int sx = matrix.getSource().getX();
		int sy = matrix.getSource().getY();
		int dx = matrix.getDestiny().getX();
		int dy = matrix.getDestiny().getY();
		//long diff = System.currentTimeMillis();
		boolean result = this.search(sx, sy, dx, dy);
		//System.out.println( (System.currentTimeMillis() - diff) / 1000 );
		return result;
	}

	// executa uma busca num labiriton de tamanho pre determinado
	public static void main(String[] args) {
		new BFSMatrix().run();
	}
	
	// permite que o usuario diga qual o tamnho do labiriton que ele deseja
	public void execute() {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		try {
			printSubtitle();
			System.out.println("Digite a dimensão do GRID do labirinto no formado N N");
			StringTokenizer tk = new StringTokenizer(buffer.readLine(), " ");
			int dimX = Integer.parseInt(tk.nextToken());
			int dimY = Integer.parseInt(tk.nextToken());
			matrix = new Matrix(dimX, dimY);
			int sx = matrix.getSource().getX()
				,sy = matrix.getSource().getY()
				,dx = matrix.getDestiny().getX()
				,dy = matrix.getDestiny().getY();
			search(sx, sy, dx, dy);
		} catch(IOException | NumberFormatException e) {}
	}
	
	// funcao que printa uma legenda
	public static void printSubtitle() {
		System.out.println("LEGENDA");
		System.out.println("HO - Buraco");
		//System.out.println("AC - Ator");
		System.out.println("EE - Caverna segura");
		System.out.println("EW - Caverna com vento");
		System.out.println("NO - Ator virado para Norte");
		System.out.println("SU - Ator virado para Sul");
		System.out.println("LE - Ator virado para Leste");
		System.out.println("OE - Ator virado para Oeste");
		System.out.println("");
	}
}
