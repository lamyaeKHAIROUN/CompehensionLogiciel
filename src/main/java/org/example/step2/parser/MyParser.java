package org.example.step2.parser;


import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MyParser {

    public static final String projectPath = "C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur";
    public static final String projectSourcePath = projectPath + "\\src";
    public  final String jrePath = "C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur";
    private  int counter;
    private  int nbLinesOfCodes;
     final File folder = new File(projectSourcePath);
     ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
     StringBuilder content = new StringBuilder();








    // create AST
    private  CompilationUnit parse(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = {projectSourcePath};
        String[] classpath = {jrePath};

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }

    // read all java files from specific folder
    public static ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                // System.out.println(fileEntry.getName());
                javaFiles.add(fileEntry);
            }
        }
        return javaFiles;
    }

    public void getParsedFiles(ArrayList<File> javaFiles,CompilationUnit parse ) throws IOException {
        javaFiles = listJavaFilesForFolder(folder);
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry);
            //content.append(FileUtils.readFileToString(fileEntry));
            parse = parse(content.toString().toCharArray());

        }

    }
}

