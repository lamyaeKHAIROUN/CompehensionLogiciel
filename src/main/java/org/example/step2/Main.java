package org.example.step2;


import org.example.step2.processor.Processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * Hello world!
 *
 */
public class Main {
    static String path;
    public final static String indentationFormat = "";

    public static void main(String[] args) throws FileNotFoundException, IOException {
		/*if(args.length>0) {
			App.path = args[0];
		}else {
			System.out.println("Veuillez insérer le chemin de dossier à analyser en argument");
			System.out.println("veuillez insérer la ligne suivante en argument de la VM pour éviter certains bugs sur l'interface graphique ");
			System.out.println("-Djava.awt.headless=false");
		}*/
        Main.Menu();


    }

    public static void Menu() throws FileNotFoundException, IOException {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        Processor processor;
        //Window window;
        //processor = new Processor("C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur\\src1");
        /*processor.process();
        processor.processGraph();
        processor.saveGraph();
        processor.saveGraphAsPNG();*/
        while (true) {
            System.out.println(
                    "********Bienvenue dans Notre application permettant l'analyse statique d'un programme*******");
            System.out.println(
                    "Pour obtenir les informations des classes du projet (infos de la partie 2 du TP) tapez 1");
            System.out.println("Pour la version graphique de l'exercice 1 tapez 2");
            System.out.println("Pour le graphe d'appels tapez 3");
            System.out.println("Pour la version graphique de l'exercice 2 tapez 4");
            System.out.println("Pour quitter tapez 0");
            choice = sc.nextInt();
            switch (choice) {
                case 1: {
                   // processor.display();
                    break;
                }
               /* case 2: {
                    System.out.println("La fenetre va s'ouvrir !");
                    window = new Window(processor);
                    break;
                }
                case 3: {
                    System.out.println("Le graphe d'appels est :");
                    System.out.println(processor.getGraph());
                    break;
                }
                case 4: {
                    System.out.println("La fenetre va s'ouvrir !");
                    window = new Window();
                    window.showGraph();
                    ;
                    break;
                }*/
                case 0: {
                    System.out.println("Au revoir ! ");
                    sc.close();
                    System.exit(0);
                    break;
                }
                default:
                    break;
            }

        }

    }

}
