package org.example.step2.processor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.example.step2.parser.MyParser;
import org.example.step2.visitor.ClassDeclarationVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Processor {

    private String path;
    private MyParser parser;
    private ClassDeclarationVisitor classDeclarationVisitor;

    public Processor(String path) {
        this.path = path;
        this.parser = new MyParser();
        this.classDeclarationVisitor = new ClassDeclarationVisitor();
    }

    public void parse() throws FileNotFoundException, IOException {
        List<String> javaFilesPaths = this.getJavaFiles();
        CompilationUnit parse;

        for (String filePath : javaFilesPaths) {
            parse = parser.getCompilationUnit(filePath);
            classDeclarationVisitor.setCu(parse);
            parse.accept(classDeclarationVisitor);
        }

    }
    public void display() {
        // 1
        System.out.println("Nombre de classes de l'application : " + classDeclarationVisitor.getNbClasses());
        //2
       // System.out.println("Nombre de ligne de l'application : " + classDeclarationVisitor.getLinesNB());


    }


    public static int nbClasses(CompilationUnit parse) {
        ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
        parse.accept(visitorClasses);
        return visitorClasses.getNbClasses();
    }

    public String exercice1() {
        return String.valueOf(classDeclarationVisitor.getNbClasses());
    }


    public List<String> getJavaFiles() {
        File directory = new File(path);
        return parser.getFilesPaths(directory);
    }

}