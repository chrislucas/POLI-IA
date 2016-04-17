package com.br.exerc.poli.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.StringTokenizer;

import com.br.exerc.poli.worlds.Matrix;
import com.br.exerc.poli.worlds.Matrix.Node;

public class DFS implements TypeSearches {

	private Matrix matrix;
	
	
	public void run() {
		long diff = System.currentTimeMillis();
		matrix = new Matrix(8, 8);
		System.out.println( (System.currentTimeMillis() - diff) / 1000 );
	}
	
	public void execute() {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		try {
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
	
	
	@Override
	public void search(int sx, int sy, int dx, int dy) {
		// TODO Auto-generated method stub
		Stack<Node> stack = new Stack<>();
		stack.push(matrix.getSource());
		while( ! stack.empty() ) {
			Node top = stack.pop();
			if(top.equals(matrix.getDestiny())) {
				break;
			}
			
			int x = top.getX(),
				y = top.getY();
			
			for(Node next : matrix.validateStep(x, y)) {
				x = next.getX();
				y = next.getY();
				if( ! matrix.isVisited(x, y)) {
					matrix.setVisited(x, y, true);
					stack.push(next);
				}
			}
		}
	}

}
