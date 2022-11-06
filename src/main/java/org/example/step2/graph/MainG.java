package org.example.step2.graph;

import org.example.step2.metric.MetricClass;
import org.example.step2.processor.Processor;

import java.io.IOException;

public class MainG {
    public static void main(String args[]) throws IOException {
        Processor processor;
        processor = new Processor("C:\\Users\\SCD UM\\Downloads\\mini_project_in_project\\project\\src");
        CallGraph callGraph=new CallGraph();
        MetricClass metricClass=new MetricClass();
        callGraph.processGraph(processor.getParser().getParse());
       // metricClass.allRelationsMetric(callGraph);
       // metricClass.relationsBetween2Classes(callGraph,"Main","Test");
        double couplage=metricClass.metricMethod(callGraph,"Main","Test");
        System.out.println("couplage: "+couplage);

    }
}
