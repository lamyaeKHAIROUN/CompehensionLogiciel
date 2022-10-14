
package org.example.step2;

import org.example.step2.interfaceGraphique.InterfaceGraphique;
import org.example.step2.processor.Processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Main {
	
	static String path;
	public final static String indentationFormat = "";
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Main.Menu();
	}

	public static void Menu() throws FileNotFoundException, IOException {
		Scanner sc = new Scanner(System.in);
		
		int choix = 0;
		Processor processor;
		processor = new Processor("C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur\\src");
		// processor.parseClasses();
		processor.parsePackages();
		InterfaceGraphique i = new InterfaceGraphique();

		// 1
		// processor.parseV();
		while (true) {

			System.out.println(
					"************************************** Bienvenue dans l'application d'analyse statique de code  **************************************");
			System.out.println("- Tapez 1 pour continuer au console ");
			System.out.println("- Tapez 2 pour continuer sur l'interface graphique ");
			choix = sc.nextInt();

			switch (choix) {
			case 1: {
				processor.infoApplication();
				break;
			}
			case 2: {
				System.out.println("L'interface vas s'ouvrir maintenat");
				i.displayInterface(processor);
				break;
			}
			default:
				break;

			}

		}

	}
}