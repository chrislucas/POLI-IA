package com.br.exerc.poli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.br.exerc.poli.actions.BFSMatrix;

/**
 * 
 * @author christoffer
 * 
 * Essa classe representa o mundo Wumpus
 * 
 * 
 * */

public class Main {

	public static void main(String[] args) {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		int n;
		try {
			do {
				System.out.println("1 - Efetuar 10 testes");
				System.out.println("2 - Efetuar N testes");
				System.out.println("0 - SAIR");
				n = Integer.parseInt(buffer.readLine());
				BFSMatrix exp = new BFSMatrix();
				double x;
				int V = 0, F = 0;
				switch(n) {
					case 1:
						x = 10.0;
						for(int i=0; i<(int)x; i++) {
							boolean ans = exp.run();
							if(ans)
								V++;
							else
								F++;
						}
						System.out.printf("V(%f) F(%f)\n", V/10.0, F/10.0);
					break;
					case 2:
						System.out.println("Informe a quantidade de testes");
						x = Double.parseDouble(buffer.readLine());
						for(int i=0; i<(int)x; i++) {
							boolean ans = exp.run();
							if(ans)
								V++;
							else
								F++;
						}
						System.out.printf("V(%f) F(%f)\n", V/x, F/x);
					break;
					case 0:
						System.out.println("SAIR");
						break;
					default:
						System.out.printf("Não existe opcao %d\n", n);
						break;
				}
				
			} while(n != 0);			
		} catch(IOException ioex) {}

	}

}
