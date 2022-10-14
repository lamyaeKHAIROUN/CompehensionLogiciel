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
    private Map<String, Set<String>> map = new HashMap<>();

    public DefaultDirectedGraph<String, DefaultEdge> getDefaultEdgeDefaultDirectedGraph() {
        return defaultEdgeDefaultDirectedGraph;
    }

    public void setDefaultEdgeDefaultDirectedGraph(DefaultDirectedGraph<String, DefaultEdge> defaultEdgeDefaultDirectedGraph) {
        this.defaultEdgeDefaultDirectedGraph = defaultEdgeDefaultDirectedGraph;
    }

    DefaultDirectedGraph<String, DefaultEdge> defaultEdgeDefaultDirectedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

    // This function adds a new vertex to the graph
    public void addVertex(String s) {
        map.put(s, new LinkedHashSet<>());
    }

    // This function adds the edge
    // between source to destination
    public void addEdge(String source,
                        String destination)
    {

        if (!map.containsKey(source))
            addVertex(source);

        /*if (!map.containsKey(destination))
            addVertex(destination);*/

        map.get(source).add(destination);
        //defaultEdgeDefaultDirectedGraph.addEdge(source,destination);

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
        for (String v : map.keySet()) {
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
    public String afficherGraph() {
        StringBuilder builder = new StringBuilder();

        for (String v : map.keySet()) {
            builder.append(v.toString() + ": ==> ");
            for (String w : map.get(v)) {
                builder.append(w.toString()+ "  ");
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
        g.addEdge(v1.getLabel(), v2.getLabel());
        g.addEdge(v1.getLabel(), v4.getLabel());
        g.addEdge(v4.getLabel(), v2.getLabel());
        g.addEdge(v1.getLabel(), v3.getLabel());
        g.addEdge(v5.getLabel(), v4.getLabel());
        g.addEdge(v2.getLabel(), v3.getLabel());
        g.addEdge(v3.getLabel(), v4.getLabel());

        // Printing the graph
        System.out.println("Graph d'appel:\n"
                + g.afficherGraph());

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
