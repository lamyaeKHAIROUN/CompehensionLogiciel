package org.example.step2.metric;

import org.eclipse.jdt.core.dom.*;
import org.example.step2.graph.CallGraph;

import java.util.Map;
import java.util.Set;

public class MetricClass {

    //  =new CallGraph();
    public int allRelationsMetric(CallGraph callGraph) {
        int nbRelation = 0;
        Map<MethodDeclaration, Set<MethodInvocation>> allInvocationsByMet = callGraph.getAllInvocationsByMet();
        for (Set<MethodInvocation> set : allInvocationsByMet.values()) {
            nbRelation += set.size();

        }
       // System.out.println("nbRelation: " + nbRelation);
        return nbRelation;

    }

    public int relationsBetween2Classes(CallGraph callGraph, String class1, String class2) {
        int nbRelation1 = 0;
        int nbRelation2 = 0;
        int nbrelation = 0;
        //on récupere la map qui contient toutes les classes, leurs méthodes et leurs invocations
        Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> mapTheCallGraph = callGraph.getMapTheCallGraph();
        Map<MethodDeclaration, Set<MethodInvocation>> allInvocationsByMet;
        Set<MethodInvocation> invocations;
        IMethodBinding binding;
        ITypeBinding type;


        for (TypeDeclaration ts : mapTheCallGraph.keySet()) {
            //on parcours les classes et on vérifie si la classe courante corresponde à la première classe données en parametre
            if (ts.getName().getIdentifier().equals(class1)) {
               // System.out.println("find class " + class1);
                //on récupere les méthodes et leurs invocations de la classe courante
                allInvocationsByMet = mapTheCallGraph.get(ts);
                //on boucle sur les méthodes
                for (MethodDeclaration method : allInvocationsByMet.keySet()) {
                   // System.out.println("methode de classe :" + method.getName());
                    //on récupère les invocations de la méthode courante
                    invocations = allInvocationsByMet.get(method);
                    for (MethodInvocation invocation : invocations) {
                        binding = invocation.resolveMethodBinding();
                     //   System.out.println("binding : " + binding);
                      //  System.out.println("invcation : " + invocation.getName());
                        if (binding != null) {
                            //on récupère la classe déclarante de la méthode invoquée
                            type = binding.getDeclaringClass();
                            if (type != null) {
                                //System.out.println("type : " + type.getName());
                                //on verifie si la classe déclarante est bien la deuxiemme classe donnée en parametre
                                if (type.getName().equals(class2)){
                                   // System.out.println("find method of "+class2+ " in "+class1);
                                    //on incrémente le nombre de relations entre les 2 classe
                                    nbRelation1++;
                                }

                            }}

                        }

                    }

                }

                //on fait la meme chose pour récuperer les invocations du premiere classe dans la deuxiemme classe
                if (ts.getName().getIdentifier().equals(class2)) {
                    //System.out.println("find class2 " + class2);

                    allInvocationsByMet = mapTheCallGraph.get(ts);
                    // Set<MethodDeclaration> methods= mapTheCallGraph.keySet().get(ts);
                    for (MethodDeclaration method : allInvocationsByMet.keySet()) {
                       // System.out.println("methode de classe2 :" + method.getName());
                        invocations = allInvocationsByMet.get(method);
                        for (MethodInvocation invocation : invocations) {
                            binding = invocation.resolveMethodBinding();
                           // System.out.println("bindin2 : " + binding);
                           // System.out.println("inv2 : " + invocation.getName());
                            if (binding != null) {
                                type = binding.getDeclaringClass();
                                if (type != null) {
                                  //  System.out.println("type2 : " + type.getName());
                                    if (type.getName().equals(class1)){
                                      //  System.out.println("find method of "+class1+ " in "+class2);
                                        nbRelation2++;
                                    }

                                }}

                        }

                    }
                }

            }
            //nbr de relations total est la somme des deux relations existantes dans les deux classes
            nbrelation=nbRelation1+nbRelation2;
            //System.out.println("nb relation total :"+nbrelation);
            return nbrelation;
        }

        public float metricMethod(CallGraph callGraph, String class1, String class2){
            int i1=relationsBetween2Classes(callGraph,class1,class2);
            int i2=allRelationsMetric(callGraph);
            //System.out.println("i1 "+i1);
            //System.out.println("i2 "+i2);

            return ((float) i1/i2);
        }
    }
