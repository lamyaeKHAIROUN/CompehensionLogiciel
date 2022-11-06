package org.example.step2.graph;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge {
    @Override
    public String toString() {
        return String.valueOf(this.getWeight());
    }

    public MyWeightedEdge() {
        super();
    }
}