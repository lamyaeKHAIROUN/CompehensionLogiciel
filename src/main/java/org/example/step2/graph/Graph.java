package org.example.step2.graph;


import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

    // We use Hashmap to store the edges in the graph
    private Map<Vertex, Set<Vertex>> map = new HashMap<>();

    public DefaultDirectedGraph<String, DefaultEdge> getDefaultEdgeDefaultDirectedGraph() {
        return defaultEdgeDefaultDirectedGraph;
    }

    public void setDefaultEdgeDefaultDirectedGraph(DefaultDirectedGraph<String, DefaultEdge> defaultEdgeDefaultDirectedGraph) {
        this.defaultEdgeDefaultDirectedGraph = defaultEdgeDefaultDirectedGraph;
    }

    DefaultDirectedGraph<String, DefaultEdge> defaultEdgeDefaultDirectedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

    // This function adds a new vertex to the graph
    public void addVertex(Vertex s) {
        map.put(s, new LinkedHashSet<>());
    }

    // This function adds the edge
    // between source to destination
    public void addEdge(Vertex source,
                        Vertex destination)
    {

        if (!map.containsKey(source))
            addVertex(source);

        if (!map.containsKey(destination))
            addVertex(destination);

        map.get(source).add(destination);

       // defaultEdgeDefaultDirectedGraph.addEdge(source.getLabel(),destination.getLabel());

    }

    // This function gives the count of vertices
    public void getVertexCount() {
        System.out.println("The graph has "
                + map.keySet().size()
                + " vertex");
    }

    // This function gives the count of edges
    public void getEdgesCount(boolean bidirection) {
        int count = 0;
        for (Vertex v : map.keySet()) {
            count += map.get(v).size();
        }

        System.out.println("The graph has "
                + count
                + " edges.");
    }

    // This function gives whether
    // a vertex is present or not.
    public void hasVertex(T s) {
        if (map.containsKey(s)) {
            System.out.println("The graph contains "
                    + s + " as a vertex.");
        } else {
            System.out.println("The graph does not contain "
                    + s + " as a vertex.");
        }
    }

    // This function gives whether an edge is present or not.
    public void hasEdge(T s, T d) {
        if (map.get(s).contains(d)) {
            System.out.println("The graph has an edge between "
                    + s + " and " + d + ".");
        } else {
            System.out.println("The graph has no edge between "
                    + s + " and " + d + ".");
        }
    }

    // Prints the adjancency list of each vertex.
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Vertex v : map.keySet()) {
            builder.append(v.getType() + "::" + v.getLabel() + "    ");
            for (Vertex w : map.get(v)) {
                builder.append(w.getType() + "::" + w.getLabel() + "  ");
            }
            builder.append("\n");
        }

        return (builder.toString());
    }

}

// Driver Code
class MainGraph {
    public static void createGraphPng(Graph<Vertex> g) throws IOException {
        File imgFile = new File("graph.png");
        imgFile.createNewFile();

        JGraphXAdapter<String, DefaultEdge> graphAdapter =
                new JGraphXAdapter<String, DefaultEdge>(g.getDefaultEdgeDefaultDirectedGraph());
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        ImageIO.write(image, "PNG", imgFile);

    }

    public static void main(String args[]) throws IOException {

        // Object of graph is created.
        Graph<Vertex> g = new Graph<Vertex>();

        // edges are added.
        // Since the graph is bidirectional,
        // so boolean bidirectional is passed as true.

        Vertex v1 = new Vertex("n1", "t1");
        Vertex v2 = new Vertex("n2", "t1");
        Vertex v3 = new Vertex("n3", "t2");
        Vertex v4 = new Vertex("n4", "t2");
        Vertex v5 = new Vertex("n5", "t2");
        Vertex v6 = new Vertex("n6", "t3");
        Vertex v7 = new Vertex("n7", "t3");
        g.addEdge(v1, v2);
        g.addEdge(v1, v4);
        g.addEdge(v4, v2);
        g.addEdge(v1, v3);
        g.addEdge(v5, v4);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);

        // Printing the graph
        System.out.println("Graph d'appel:\n"
                + g.toString());

        // Gives the no of vertices in the graph.
        g.getVertexCount();

        // Gives the no of edges in the graph.
        g.getEdgesCount(true);
       // createGraphPng(g);

        // Tells whether the edge is present or not.
        //  g.hasEdge(v2, v4);

        // Tells whether vertex is present or not
        //  g.hasVertex(v5);

        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        JGraph jgraph = new JGraph(new JGraphModelAdapter( g.defaultEdgeDefaultDirectedGraph));
        frame.getContentPane().add(jgraph);
        frame.setVisible(true);
    }


}
/*
import java.util.*;

class Graph {
    //chaque noeud a une liste des noeuds en relation ==> chaque méthode a une
  //  private Map<Vertex, List<Vertex>> adjVertices= new HashMap<Vertex, List<Vertex>>();
    private Set<Vertex> adjVertices;
    // standard constructor, getters, setters

    /*public Graph(Map<Vertex, List<Vertex>> adjVertices) {
        this.adjVertices = adjVertices;
    }
*//*
    public Graph() {
        adjVertices=new HashSet<>();
    }

    public Set<Vertex> getAdjVertices() {
        return adjVertices;
    }

    public void setAdjVertices(Set<Vertex> adjVertices) {
        this.adjVertices = adjVertices;
    }


    void addVertex(Vertex v) {
        adjVertices.add(v);
    }

   /* void removeVertex(String label) {
        Vertex v = new Vertex(label);
        adjVertices.values().stream().forEach(e -> e.remove(v));
        adjVertices.remove(new Vertex(label));
    }*/
/*

    void addEdge(Vertex source, Vertex target) {
        //Vertex v1 = new Vertex(label1);
        //Vertex v2 = new Vertex(label2);
        //adjVertices.get(v1).add(v2);
        source.getListAdj().add(target);
        System.out.println("cccccc "+ source.getListAdj().size());

        /*if (adjVertices.get(v1) != null) {
        }
        /*if (adjVertices.get(v2) != null) {
            adjVertices.get(v2).add(v1);
        }*//*
    }


//exemple
    Graph createGraph() {
        Graph graph = new Graph();
        Vertex v=new Vertex("n1");
        Vertex v2=new Vertex("n2");
        Vertex v3=new Vertex("n3");
        Vertex v4=new Vertex("n4");
        Vertex v5=new Vertex("n5");
        Vertex v6=new Vertex("n6");
        Vertex v7=new Vertex("n7");
        graph.addVertex(new Vertex("n2"));
        graph.addVertex(new Vertex("n3"));
        graph.addVertex(new Vertex("n4"));
        graph.addVertex(new Vertex("n5"));
        graph.addVertex(new Vertex("n6"));
        graph.addVertex(new Vertex("n7"));
        graph.addEdge(v, v2);
        graph.addEdge(v3, v4);

        graph.addEdge(v3, v5);
        graph.addEdge(v5, v6);
        graph.addEdge(v5, v7);
        graph.addEdge(v7, v);
        graph.addEdge(v7, v3);
        return graph;
    }
    //obtenir la listes des sommets adjascents d'un sommet




    public static void main(String[] args) {
        Vertex v1=new Vertex("v1");
        Vertex v2=new Vertex("v2");
        List<Vertex> l1= new ArrayList<>();
        List<Vertex> l2= new ArrayList<>();
        l1.add(new Vertex("v11"));
        l1.add(new Vertex("v12"));
        l1.add(new Vertex("v13"));
        l1.add(new Vertex("v21"));
        l1.add(new Vertex("v22"));
        l1.add(new Vertex("v23"));
        Map<Vertex, List<Vertex>> adjVertices=new HashMap<>();
        adjVertices.put(v1,l1);
        adjVertices.put(v2,l2);

        List<Vertex> listeAdj=new ArrayList<>();
       Graph graph =new Graph();
          Graph g=graph.createGraph();
      //  Set<String> list = new LinkedHashSet<String>();
        //list=breadthFirstTraversal(graph,"Bob");
        Set<Vertex> set=g.getAdjVertices();

        for(Vertex v: set){
            System.out.println("ver: "+v.getLabel());
            System.out.println("liste des vertexes: ");
            System.out.println("size: "+v.getListAdj().size());
            for (Vertex v3: v.getListAdj()){
                System.out.println(v3.getLabel());
            }
        }

    }
}*/
