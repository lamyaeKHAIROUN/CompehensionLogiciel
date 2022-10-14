package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ClassDeclarationVisitor extends Visitor {
    private int nbClasses;
    private CompilationUnit cu;
    List<MethodDeclaration> methodDeclarations =new ArrayList<>();
    private List<TypeDeclaration> classes = new ArrayList<>();
    private Map<TypeDeclaration, Map<String, Integer>> classeCollection = new HashMap<>();

    public Map<TypeDeclaration, Set<MethodDeclaration>> getClasseMapMethods() {
        return classeMapMethods;
    }

    private Map<TypeDeclaration, Set<MethodDeclaration>> classeMapMethods = new HashMap<>();

    String content;
    List<FieldDeclaration> attributs = new ArrayList<FieldDeclaration>();
    private int nbLinesOfCodes=0;

    public Map<TypeDeclaration, Map<String, Integer>> getClasseCollection() {
        return classeCollection;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        if (!node.isInterface()) {
            classes.add(node);
            nbClasses++;
            Map<String, Integer> methodsCollection = new HashMap<>();
            Set<MethodDeclaration> methodList = new HashSet<>();
            for (MethodDeclaration methodDeclaration : node.getMethods()) {
                methodsCollection.put(methodDeclaration.getName().getIdentifier(), countLines(methodDeclaration.getBody().toString()));
                methodList.add(methodDeclaration);

            }
            classeCollection.put(node, methodsCollection);
            classeMapMethods.put(node,methodList);

        }
        return super.visit(node);
    }

    public List<TypeDeclaration> getClasses() {
        return classes;
    }

    public int getNbClasses() {
        return nbClasses;
    }

    //EXERCICE 2 Nombre de lignes de code de l’application.
    public int numberOfLines(String s) {
        int rslt = 0;
        rslt += countLines(s);
        //return ("Le nombre de lignes de code de l’application est:"+rslt);
        return rslt;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    public List<FieldDeclaration> getAttribus() {

        return attributs;
    }

    public static void setCu(CompilationUnit cu) {

       cu = cu;
    }
    public String getFileContent(String filePath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
            //MyParser.nbLinesOfCodes++;
        }
        br.close();
        return sb.toString();
    }

}