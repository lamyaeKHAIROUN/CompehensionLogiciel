package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;


public class ClassDeclarationVisitor extends ASTVisitor {
    private int nbClasses;
    private CompilationUnit cu;
    private List<TypeDeclaration> classes = new ArrayList<>();
    String content;
    List<FieldDeclaration> attributs = new ArrayList<FieldDeclaration>();
    private int nbLinesOfCodes=0;

    @Override
    public boolean visit(TypeDeclaration node) {
        if (!node.isInterface()) {
            classes.add(node);
            nbClasses++;
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

    public void setCu(CompilationUnit cu) {
        this.cu = cu;
    }


}


