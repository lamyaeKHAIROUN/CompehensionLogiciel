package org.example.step2;


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
        int choice = 0;
        Processor processor;
        processor = new Processor("C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur\\src");
        processor.parse();


        while (true) {
            System.out.println(
                    "-----------------Bienvenue-------------------");
            System.out.println(
                    "Tapez 1 pour afficher des information concarnant l'application java analysée!");



            choice = sc.nextInt();

            switch (choice) {
            case 1: {
                processor.display();
                break;
            }
            default:
                break;





            }


        }


    }
}
