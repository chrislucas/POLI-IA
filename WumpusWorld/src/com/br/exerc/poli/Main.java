package com.br.exerc.poli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.br.exerc.poli.actions.BFSMatrix;
import com.br.exerc.poli.actions.TypeSearches;

/**
 * 
 * @author christoffer
 * 
 * Classe principal, onde o programa comeca
 * 
 * 
 * */

public class Main {

	public static void main(String[] args) {
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		BFSMatrix.printSubtitle();
		int n;
		// Menu para o usuario selecionar como quer ver os testes
		try {
			do {
				System.out.println("1 - Efetuar 100 testes");
				System.out.println("2 - Efetuar N testes");
				System.out.println("0 - SAIR");
				n = Integer.parseInt(buffer.readLine());
				TypeSearches exp = new BFSMatrix();
				double x;
				int V = 0, F = 0;
				switch(n) {
					case 1:
						x = 100.0;
						for(int i=0; i<(int)x; i++) {
							System.out.printf("Caso %d\n", (i+1));
							boolean ans = exp.run();
							if(ans)
								V++;
							else
								F++;
						}
						System.out.printf("V(%.2f) F(%.2f) \n", (V/x)*100.0, (F/x)*100.0);
					break;
					case 2:
						System.out.println("Informe a quantidade de testes");
						x = Double.parseDouble(buffer.readLine());
						for(int i=0; i<(int)x; i++) {
							System.out.printf("Caso %d\n", (i+1));
							boolean ans = exp.run();
							if(ans)
								V++;
							else
								F++;
						}
						System.out.printf("V(%.2f) F(%.2f) \n", (V/x)*100.0, (F/x)*100.0);
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
