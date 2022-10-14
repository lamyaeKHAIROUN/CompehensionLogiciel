package org.example.step2.parser;


import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyParser {

    private int nbLinesOfCodes;

    public static final String jrePath = "C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur\\";

    public final String projectPath = new String();



    public String projectSourcePath="";
    public   final File folder;
    ArrayList<File> javaFiles ;

    public MyParser(String path) {
        this.projectSourcePath=path;
        this.folder = new File(projectSourcePath);
        this.javaFiles = listJavaFilesForFolder(folder);
    }

    public  ArrayList<File> getJavaFiles() {
        return javaFiles;
    }
    public void setProjectSourcePath(String sourcePath){
        this.projectSourcePath=sourcePath;
    }
    public String getProjectSourcePath() {
        return projectSourcePath;
    }

    public  void setJavaFiles(ArrayList<File> javaFiles) {
       javaFiles = javaFiles;
    }




    public  int getNbLinesOfCodes() {
        return nbLinesOfCodes;
    }

    public void setNbLinesOfCodes(int nbLinesOfCodes) {
        nbLinesOfCodes = nbLinesOfCodes;
    }

    public CompilationUnit getCompilationUnit(String filePath) throws FileNotFoundException, IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
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
            nbLinesOfCodes++;
        }
        br.close();
        return sb.toString();
    }
    public String incrNBLines(String filePath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
            nbLinesOfCodes++;
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
    public ArrayList<File> listJavaFilesForFolder(final File folder) {
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

    public CompilationUnit parse(char[] classSource) {
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


    public CompilationUnit getParse() throws IOException {
      //  List<String> javaFilesPaths = this.getJavaFiles();
        CompilationUnit parse = null;
        StringBuilder content =  new StringBuilder();;


        for (File fileEntry : getJavaFiles()) {
            content.append(FileUtils.readFileToString(fileEntry));
            // content.append("\r");
            parse = parse(content.toString().toCharArray());
        }
        return parse;
    }

    public List<CompilationUnit> getCompUnits() throws IOException {
        //  List<String> javaFilesPaths = this.getJavaFiles();
        CompilationUnit parse = null;
        StringBuilder content =  new StringBuilder();;
        String content2=new String();
        List<CompilationUnit> listCu=null;
        for (File fileEntry : getJavaFiles()) {
           // content.append(FileUtils.readFileToString(fileEntry));
            //parse = parse(content.toString().toCharArray());

            content2 = FileUtils.readFileToString(fileEntry);
            parse = parse(content2.toCharArray());
            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhh");
            listCu.add(parse);
            System.out.println(listCu);
        }
        return listCu;
    }



}