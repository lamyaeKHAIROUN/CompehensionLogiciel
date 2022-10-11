package org.example.step2.parser;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyParser {

    private static int nbLinesOfCodes;

    public static int getNbLinesOfCodes() {
        return MyParser.nbLinesOfCodes;
    }

    public static void setNbLinesOfCodes(int nbLinesOfCodes) {
        MyParser.nbLinesOfCodes = nbLinesOfCodes;
    }

    public CompilationUnit getCompilationUnit(String filePath) throws FileNotFoundException, IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.8
        char[] fileContent = this.getFileContent(filePath).toCharArray();
        parser.setSource(fileContent);
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        return cu;
    }

    public String getFileContent(String filePath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
            MyParser.nbLinesOfCodes++;
        }
        br.close();
        return sb.toString();
    }

    public List<String> getFilesPaths(File directory) {

        List<String> filesPaths = new ArrayList<>();

        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) {
                if (this.isJavaFile(file)) {
                    filesPaths.add(file.getAbsolutePath());
                }
            } else {
                filesPaths.addAll(getFilesPaths(file));
            }
        }
        return filesPaths;
    }

    private boolean isJavaFile(File file) {

        final String extentionWanted = ".java";
        int extentionIndex = file.getName().length() - 5;
        int endFileIndex = file.getName().length();
        final String fileExtention = file.getName().substring(extentionIndex, endFileIndex);

        return fileExtention.equals(extentionWanted);
    }



}

