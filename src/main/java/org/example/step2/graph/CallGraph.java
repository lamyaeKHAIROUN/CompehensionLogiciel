package org.example.step2.graph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.eclipse.jdt.core.dom.*;
import org.example.step2.parser.MyParser;
import org.example.step2.visitor.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallGraph {
    private String path;
    private MyParser parser;
    private ClassDeclarationVisitor classDeclarationVisitor;
    private PackageDeclarationVisitor packageDeclarationVisitor;
    private Visitor visitor;
    private DefaultDirectedGraph<String, DefaultEdge> graphJGraphT= new DefaultDirectedGraph<>(DefaultEdge.class);
    private Set<String> setLink=new HashSet<>();

    public Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> getMapTheCallGraph() {
        return mapTheCallGraph;
    }

    private Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> mapTheCallGraph =new HashMap<>();

    public Map<MethodDeclaration, Set<MethodInvocation>> getAllInvocationsByMet() {
        return allInvocationsByMet;
    }

    private Map<MethodDeclaration, Set<MethodInvocation>> allInvocationsByMet =new HashMap<>();


    private Map<TypeDeclaration, Set<MethodDeclaration>> allmethodsByClass =new HashMap<>();


    //private static int nbRelations=0;
    //Graph<Vertex> graph = new Graph<Vertex>();




    public Map<TypeDeclaration, Set<MethodDeclaration>> getAllmethodsByClass() {
        return allmethodsByClass;
    }


    public Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> processGraph(CompilationUnit cu) {
        Vertex source=new Vertex();
        Vertex target=new Vertex();
        long total = 0;
        Map<MethodInvocation, Integer> mapInvocations;
      /*  String classNameM2 = "";
        IMethodBinding binding;
        TypeDeclaration typeM1;
        ITypeBinding type;
        String classNameM1;*/


        boolean isMethodNodeAdded;
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);

        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);
            allmethodsByClass.put(nodeClass, visitorClass.getClasseMapMethods().get(nodeClass));

            Map<MethodDeclaration, Set<MethodInvocation>> mapMethodDeclarationInvocation = new HashMap<>();
            String methodApplant;

            for (MethodDeclaration nodeMethod : visitorMethod.getMethods()) {
                nodeMethod.resolveBinding();
                MethodIvocationVisitor visitorMethodInvocation = new MethodIvocationVisitor();
                nodeMethod.accept(visitorMethodInvocation);
                mapMethodDeclarationInvocation.put(nodeMethod,  visitorMethodInvocation.getMethods());

                methodApplant = nodeClass.getName().toString()+"::"+nodeMethod.getName();

                isMethodNodeAdded = false;
                //recuperer les methodes invoque pour chaque methode
                allInvocationsByMet.put(nodeMethod,visitorMethodInvocation.getMethods());

                for (MethodInvocation methodInvocation : visitorMethodInvocation.getMethods()) {

//*****************************************************************************//

                 /*   binding = methodInvocation.resolveMethodBinding();
                    System.out.println("bindin : "+binding);
                    System.out.println("inv : "+methodInvocation.getName());


                    if (binding != null) {
                        type = binding.getDeclaringClass();
                        if (type != null) {
                            classNameM2 = type.getQualifiedName();
                            System.out.println("declaring class: "+classNameM2);

                            typeM1 = (TypeDeclaration) nodeMethod.getParent();
                            classNameM1 = Utility.getClassFullyQualifiedName(typeM1);
                            //verifie si la classe de la methode invoqué est bien defferente de celle de la classe ou la méthode appelante est declaré
                            //ça vaurt dire que si une methode appelle une methode declaré dans la meme classe que elle alors
                            // on va pas inrémenter le nombre car on est en train de calculer les relation entre les classes
                            if (!classNameM2.equals(classNameM1)) {
                                total ++;
                                System.out.println("total courant "+total);
                                //System.out.println("total: "+total);
                            }
                        }
                    }*/

//***********************************************************************************************//


                    String methodApplee;
                    if (methodInvocation.getExpression() != null) {
                        if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                           // System.out.println("class dec: "+methodInvocation.getExpression().resolveTypeBinding().getName());

                           // System.out.println("meth invoc "+methodInvocation.getName());

                            if (!isMethodNodeAdded) {
                                graphJGraphT.addVertex(methodApplant);

                                isMethodNodeAdded = true;
                            }
                            methodApplee = methodInvocation.getExpression().resolveTypeBinding().getName()+"::"+methodInvocation.getName();
                            graphJGraphT.addVertex(methodApplee);
                            graphJGraphT.addEdge(methodApplant, methodApplee);

                            setLink.add("\t\""+methodApplant+"\"->\""+methodApplee+"\"\n");
                        }
                    }
                    else if (methodInvocation.resolveMethodBinding() != null) {
                        if (!isMethodNodeAdded) {
                            graphJGraphT.addVertex(methodApplant);
                            isMethodNodeAdded = true;

                        }
                        methodApplee = methodInvocation.resolveMethodBinding().getDeclaringClass().getName()+"::"+methodInvocation.getName();
                        graphJGraphT.addVertex(methodApplee);
                        graphJGraphT.addEdge(methodApplant, methodApplee);
                        setLink.add("\t\""+methodApplant+"\"->\""+methodApplee+"\"\n");
                    }
                    else {
                        if (!isMethodNodeAdded) {
                            graphJGraphT.addVertex(methodApplant);
                            isMethodNodeAdded = true;

                        }
                        methodApplee = nodeClass.getName()+"::"+methodInvocation.getName();
                        graphJGraphT.addVertex(methodApplee);
                        graphJGraphT.addEdge(methodApplant, methodApplee);

                        setLink.add("\t\""+methodApplant+"\"->\""+methodApplee+"\"\n");
                    }
                }
            }

            mapTheCallGraph.put(nodeClass, mapMethodDeclarationInvocation);
        }

        return mapTheCallGraph;


    }


    public String buildGraphWithJGraphT() throws IOException {
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(graphJGraphT);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("my_graph.png");
        if (imgFile.exists())
            imgFile.delete();

        ImageIO.write(image, "PNG", imgFile);

        if (!imgFile.exists()) {
            System.err.println("Le fichier "+imgFile.getName()+" n'est pas cré !");
        }
        else {
            System.out.println("Vous allez trouvez l'image correspondante a notre graphe d'appel à cet endroit:\n "+imgFile.getAbsolutePath()+"\n");
        }
        return imgFile.getAbsolutePath()+"\n";
    }
}
