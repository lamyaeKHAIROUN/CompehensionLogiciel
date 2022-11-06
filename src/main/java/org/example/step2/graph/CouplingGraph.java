package org.example.step2.graph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.example.step2.metric.MetricClass;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CouplingGraph {

    Map<String, Map<String, Float>> classCalls = new TreeMap<>();

    ListenableDirectedWeightedGraph<String, MyWeightedEdge> graphJGraphT= new ListenableDirectedWeightedGraph<String, MyWeightedEdge>(MyWeightedEdge.class);

    //private SimpleWeightedGraph<String, DefaultWeightedEdge> graphJGraphT= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    float couplage=0;
    public Map<String, Map<String, Float>> createCouplingGraph(CallGraph callGraph){
        Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> mapTheCallGraph = callGraph.getMapTheCallGraph();
        MetricClass metricClass=new MetricClass();
        String class1;
        String class2;
        for (TypeDeclaration ts : mapTheCallGraph.keySet()) {
            class1=ts.getName().getIdentifier();
            graphJGraphT.addVertex(class1);
            //ajout de classe dans la map
            classCalls.put(class1,new HashMap<String, Float>());

            for(TypeDeclaration ts2 : mapTheCallGraph.keySet()){
                class2=ts2.getName().getIdentifier();
                if (!class1.equals(class2)){
                    //calculer le couplage entre les deux classes
                    couplage= metricClass.metricMethod(callGraph,class1,class2);
                    if (couplage>0){
                        //System.out.println("couplage entre class "+class1+ " et class: "+class2+" : "+couplage);
                        //si le couplage entre les deux classes est sup à 0, alors on ajoute cette classe dans la liste de classe appelées avec la valeur de couplage
                        classCalls.get(class1).put(class2,couplage);
                        graphJGraphT.addVertex(class2);
                        if(graphJGraphT.getEdge(class1,class2)==null){
                            MyWeightedEdge e= graphJGraphT.addEdge(class1,class2);
                             graphJGraphT.setEdgeWeight( e,(double) couplage);

                        }


                    }
                }
            }
        }
        afficherCouplingGraph(classCalls);
        return classCalls;
        }


        public  void afficherCouplingGraph(Map<String, Map<String, Float>> couplingGraph){

            for (String s: couplingGraph.keySet()){
                //recuperer la map des classes appelé avec leur valeur de couplage
                Map<String, Float> classes=couplingGraph.get(s);
                System.out.println("\nLa classe: "+s+" a un couplages avec:");
                for (String s2: classes.keySet()){
                    System.out.println("\t La classe: "+s2+" ==> pourcentage de couplage (poids): "+classes.get(s2));

                }

            }

        }


    public void buildGraphWithJGraphT() throws IOException {
        JFrame frame = new JFrame("Coupling graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));

        JGraphXAdapter<String, MyWeightedEdge> graphAdapter = new JGraphXAdapter<String, MyWeightedEdge>(graphJGraphT);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        frame.add(new mxGraphComponent(graphAdapter));

        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }

}
