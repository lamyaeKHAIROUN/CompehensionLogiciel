package org.example.step2.main;

import org.example.step2.graph.CallGraph;
import org.example.step2.graph.CouplingGraph;
import org.example.step2.metric.MetricClass;
import org.example.step2.processor.Processor;

import java.io.IOException;
import java.util.Scanner;

public class MainG {
    public static void main(String args[]) throws IOException, InterruptedException {
      /*  Processor processor;
        processor = new Processor("C:\\Users\\SCD UM\\Desktop\\CSPproject3\\src");
        CallGraph callGraph=new CallGraph();
        MetricClass metricClass=new MetricClass();
        CouplingGraph couplingGraph=new CouplingGraph();
        callGraph.processGraph(processor.getParser().getParse());*/
       // metricClass.allRelationsMetric(callGraph);
       // metricClass.relationsBetween2Classes(callGraph,"Main","Test");
        //double couplage=metricClass.metricMethod(callGraph,"Main","Test");
       // System.out.println("couplage entre Main et Test: "+couplage);

      //  couplingGraph.createCouplingGraph(callGraph);
        processMenu();

    }



    protected static void menu() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n\t\t\t\t\t\"******************* Bienvenue dans l'application de compréhension des programmes *****************\n");
        builder.append("\nPour calculer la metric de couplage entre deux classes tapez 1.");
        builder.append("\nPour afficher le graphe de couplage tapez 2.");
        builder.append("\nPour quitter tapez 0.");

        System.out.println(builder);
    }

    public static void processMenu() throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        Processor processor;
        processor = new Processor("C:\\Users\\SCD UM\\Desktop\\CSPproject3\\src");
        CallGraph callGraph=new CallGraph();
        MetricClass metricClass=new MetricClass();
        CouplingGraph couplingGraph=new CouplingGraph();
        callGraph.processGraph(processor.getParser().getParse());
        int choix = 0;
        while (true) {
            menu();

            choix = sc.nextInt();
            switch (choix){
                case 0:{
                    System.out.println("A dios!Pour revenir au menu, relancer le prog");
                    sc.close();
                    System.exit(0);
                    break;
                }
                case 1: {
                    Scanner scaner = new Scanner(System.in);
                    System.out.println("Inserez le nom de la première classe ");
                    String classNameA = scaner.next();
                    System.out.println("Inserez le nom de la deuxième classe ");
                    String classNameB = scaner.next();
                     double couplage=metricClass.metricMethod(callGraph,classNameA,classNameB);
                     System.out.println("couplage entre la classe "+classNameA+" et la classe "+classNameB+": "+couplage);
                    Thread.sleep(3000);
                    break;
                }
                case 2:{
                    couplingGraph.afficherCouplingGraph(couplingGraph.createCouplingGraph(callGraph));
                    couplingGraph.buildGraphWithJGraphT();
                    Thread.sleep(3000);

                    break;

                }

                default: {
                    System.err.println("Vous devez choisir un nombre parmis ceux qui sont propose dans le menu");
                    break;
                }
            }

        }
    }
}
