package org.example.step2.graph;


import java.util.ArrayList;
import java.util.List;

public class Vertex {
    String label;
    String type;

    public Vertex() {

    }

    public List<Vertex> getListAdj() {
        return listAdj;
    }

    public void setListAdj(List<Vertex> listAdj) {
        this.listAdj = listAdj;
    }

    List<Vertex> listAdj;
    public Vertex(String label) {
        this.label = label;
        listAdj=new ArrayList<>();
    }

    public String displayNode(){
            return (this.getType()+"::"+this.label);
        //return null;
    }

    public Vertex(String label, String type) {
        this.label = label;
        this.type=type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
