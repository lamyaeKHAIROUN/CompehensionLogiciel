package org.example.step2;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.example.step2.visitor.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    public static final String projectPath = "C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur";
    public static final String projectSourcePath = projectPath + "\\src";
    public static final String jrePath = "C:\\Users\\SCD UM\\Downloads\\Comparateur\\Comparateur";
    private static int counter;
    private static int nbLinesOfCodes;
    static final File folder = new File(projectSourcePath);
    static ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
    static StringBuilder content = new StringBuilder();
    static CompilationUnit parse = null;

    static Set<String>  myPackageList=new HashSet<>();
    static int nbp=0;
    static String content2 = null;
    static CompilationUnit parse2 = null;

    public static void main(String[] args) throws IOException {

        // read java files

        //CompilationUnit parse2=null;

        for (File fileEntry : javaFiles) {
            // System.out.println(content);
            content.append(FileUtils.readFileToString(fileEntry));
           // content.append("\r");
            parse = parse(content.toString().toCharArray());

            content2 = FileUtils.readFileToString(fileEntry);
            parse2 = parse(content2.toCharArray());
            // print methods info
            //printMethodInfo(parse);
            // print packages info
            //printPackageInfo(parse2);

            // print variables info
            //printVariableInfo(parse);

            //print method invocations
            //printMethodInvocationInfo(parse);
            //print classes info
            //printClassesInfo(parse);
            //nombre de classes
           // getFileContent(projectSourcePath);
            ClassesAttributes(parse);
            nbp=nbPackages(parse,myPackageList);
            maxLigneMethodeByClass(parse2);

        }


        /* System.out.println("Le nombre de classes de l'application: " + nbClasses(parse));
       System.out.println("Le nombre de lignes de l'application: " + nbLines(parse,content));
        System.out.println("Le nombre de méthodes de l'application: " + numberOfMethods(parse));
        averageMethode(parse);
       System.out.println("Le nombre moyen de lignes par méthodes: " + averageLinesByMethod(parse));
       // System.out.println("Le nombre moyen d'attributs par classe: " + ClassesAttributes(parse) / nbClasses(parse));
        System.out.println("heeeeeeeeeeeeeeeeeeeeeer");
        classMaxNbMethode(parse);*/
        //classMaxNbAttributes(parse);
       // bothMaxMethAttrByClass(parse);
        ////System.out.println("heeeeeeeeeeeeeere");
       // classWithXmethod(parse, 16);

         //maximalParameterOfMethods(parse2);
        //nbPackages(parse,myPackageList);


        System.out.println("-----------Packages------------");
        System.out.println("Nombre de packages: "+nbp);
        int i = 1;
        for(String s: myPackageList){
            System.out.println("Package N° "+i+" "+s);
            i++;
        }
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

    // create AST
    private static CompilationUnit parse(char[] classSource) {
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

    // navigate method information
    public static void printMethodInfo(CompilationUnit parse) {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        parse.accept(visitor);

        for (MethodDeclaration method : visitor.getMethods()) {
            System.out.println("Method name: " + method.getName()
                    + " Return type: " + method.getReturnType2());
        }

    }



    public static void printPackageInfo(CompilationUnit parse) {
        PackageDeclarationVisitor visitor = new PackageDeclarationVisitor();
        parse.accept(visitor);

        for (String pack : visitor.getPackages()) {
            System.out.println("Package name: " + pack);
            counter += visitor.getPackages().size();
        }
        System.out.println("Package number: " + counter);

    }



    //1. Nombre	de	classes de	l’application.
    public static int nbClasses(CompilationUnit parse) {
        ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
        parse.accept(visitorClasses);
        return visitorClasses.getNbClasses();
    }



    //2. Nombre	de	lignes	de	code de	l’application.
    public static int nbLines(CompilationUnit cu,StringBuilder contentOfFiles) {
        ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
        cu.accept(visitorClasses);
        visitorClasses.setContent(contentOfFiles.toString());
        //System.out.println(visitorClasses.numberOfLines(content.toString()));
        return visitorClasses.numberOfLines(contentOfFiles.toString());
    }


    //3. Nombre	total	de	méthodes de	l’application.
    public static int numberOfMethods(CompilationUnit cu) {
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        int nbMéthode=0;
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);
        nbMéthode+=visitorMethod.getNbMethod();
        }

        //System.out.println("Le nombre de méthodes de l'application est: " + visitorMethod.getNbMethod());
        return nbMéthode;
    }

    public static int nbPackages(CompilationUnit parse,Set nbp) {
       // Set<String> nbp =new HashSet<>();
        PackageDeclarationVisitor visitorPackages = new PackageDeclarationVisitor();
        parse.accept(visitorPackages);
        for (String packageDeclaration : visitorPackages.getPackages()){
            nbp.add(packageDeclaration);
        }

        return nbp.size();
    }

    // navigate variables inside method
    public static void printVariableInfo(CompilationUnit parse) {

        MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
        parse.accept(visitor1);
        for (MethodDeclaration method : visitor1.getMethods()) {
            VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
            method.accept(visitor2);
            for (VariableDeclarationFragment variableDeclarationFragment : visitor2
                    .getVariables()) {
                System.out.println("variable name: "
                        + variableDeclarationFragment.getName()
                        + " variable Initializer: "
                        + variableDeclarationFragment.getInitializer());
            }
        }
    }

    // navigate method invocations inside method
    public static void printMethodInvocationInfo(CompilationUnit parse) {

        MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
        parse.accept(visitor1);
        for (MethodDeclaration method : visitor1.getMethods()) {
            MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
            method.accept(visitor2);

            for (MethodInvocation methodInvocation : visitor2.getMethods()) {
                System.out.println("method " + method.getName() + " invoc method "
                        + methodInvocation.getName());
            }

        }
    }

    public static String getFileContent(String filePath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
            Parser.nbLinesOfCodes++;
        }
        br.close();
        System.out.println("number of lines: " + sb.toString());
        return sb.toString();
    }







    //5 nombre moyen de méthode par classe

    public static void averageMethode(CompilationUnit parse) {
        System.out.println("Le nombre moyen de méthode par classe: " + (int) numberOfMethods(parse) / nbClasses(parse));
    }


    //question 6: nombre moyen de ligne par méthode



    public static int averageLinesByMethod(CompilationUnit parse) {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        parse.accept(visitor);

        int totalLines = 0;
        for (MethodDeclaration method : visitor.getMethods()) {
            int start = method.getStartPosition(); // first character position
            int end = start + method.getLength(); // last character position
            int lineStart = parse.getLineNumber(start);
            int lineEnd = parse.getLineNumber(end);
            totalLines += lineEnd - lineStart;
        }

        return (int) totalLines / numberOfMethods(parse);
    }

    //nombre moyen d'attribut par classe
    public static int getNumberOfAttributs(CompilationUnit parse) {
        ClassDeclarationVisitor visitor = new ClassDeclarationVisitor();
        parse.accept(visitor);
        return visitor.getAttribus().size();
    }


    private static int ClassesAttributes(CompilationUnit cu) {
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);

        int nbAttributes = 0;
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            FieldAccessVisitor visitorAttributes = new FieldAccessVisitor();
            nodeClass.accept(visitorAttributes);
            nbAttributes += visitorAttributes.getFields().size();
        }
        return nbAttributes;
    }


    //class info
    public static void printClassesInfo(CompilationUnit parse) {
        ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
        parse.accept(visitorClasses);

        for (TypeDeclaration ts : visitorClasses.getClasses()) {
            System.out.println("Classes name: " + ts.getName());
        }
    }

    //8. Les 10%	des	classes	qui	possèdent le plus grand	nombre	de	méthodes.
    public static List<String> classMaxNbMethode(CompilationUnit cu) {
        int disPourCentClasses = (int) 0.1 * nbClasses(cu);
        if (disPourCentClasses == 0) {
            disPourCentClasses = 1;
        }

        HashMap<String, Integer> myClasses = new HashMap<String, Integer>();
        int maxMethod = 0;
        int nbMethod = 0;
        List<String> malisteTriee = new ArrayList<>();
        List<String> myDisPercentList = new ArrayList<>();

        // l'idee est de creer un tablau ou je vais trier les classes a l'aide du nombre de methode puis je vais afficher les 10% premier case

        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);
            //System.out.println("Classes name: " + nodeClass.getName());
            nbMethod = visitorMethod.getMethods().size();
            myClasses.put(String.valueOf(nodeClass.getName()), nbMethod);
            malisteTriee = listSort(myClasses);
            Collections.reverse(malisteTriee);
        }
        for (int i = 0; i < disPourCentClasses; i++) {
            myDisPercentList.add(malisteTriee.get(i));
            System.out.println("les 10% des classes ayant le plus grand nombre de methode: " + malisteTriee.get(i));
        }
        return myDisPercentList;
    }


    //9. Les	10%	des	classes	qui	possèdent	le	plus	grand	nombre d’attributs.

    public static List<String> classMaxNbAttributes(CompilationUnit cu) {
        int disPourCentClasses = (int) 0.1 * nbClasses(cu);
        if (disPourCentClasses == 0) {
            disPourCentClasses = 1;
        }

        HashMap<String, Integer> myClasses = new HashMap<String, Integer>();
        int maxMethod = 0;
        int nbAttributes = 0;
        List<String> malisteTriee = new ArrayList<>();
        List<String> myDisPercentList = new ArrayList<>();
        // l'idee est de creer un tablau ou je vais trier les classes a l'aide du nombre de methode puis je vais afficher les 10% premier case

        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            FieldAccessVisitor visitorAttributes = new FieldAccessVisitor();
            nodeClass.accept(visitorAttributes);
            nbAttributes = visitorAttributes.getFields().size();
            //System.out.println("Classes name: " + nodeClass.getName());
            myClasses.put(String.valueOf(nodeClass.getName()), nbAttributes);
            malisteTriee = listSort(myClasses);
            Collections.reverse(malisteTriee);
        }
        for (int i = 0; i < disPourCentClasses; i++) {
            myDisPercentList.add(malisteTriee.get(i));
            System.out.println("les 10% des classes ayant le plus grand nombre d'attributs: " + malisteTriee.get(i));
        }
        return myDisPercentList;
    }


//10. Les	classes	qui	font	partie	en	même	temps	des	deux	catégories	précédentes.

    public static List<String> bothMaxMethAttrByClass(CompilationUnit cu) {
        List<String> listOfClassesMaxMethod = new ArrayList<>();
        List<String> listOfClassesMaxAttrb = new ArrayList<>();
        List<String> listOfBoth = new ArrayList<>();
        listOfClassesMaxMethod = classMaxNbMethode(cu);
        listOfClassesMaxAttrb = classMaxNbAttributes(cu);
        for (int i = 0; i < listOfClassesMaxMethod.size(); i++) {
            if (listOfClassesMaxMethod.contains(listOfClassesMaxAttrb.get(i))) {
                listOfBoth.add(listOfClassesMaxAttrb.get(i));
            }
        }
        for (String s : listOfBoth) {
            System.out.println("Les 10% des classes ayant le plus grand nombre de méthodes et d'attributs: " + s);

        }
        return listOfBoth;
    }

    //11. Les	classes	qui	possèdent plus	de	X	méthodes	(la	valeur	de X	est	donnée).
    public static List<String> classWithXmethod(CompilationUnit cu, int x) {
        List<String> myClassList = new ArrayList<>();
        HashMap<String, Integer> myClasses = new HashMap<String, Integer>();

        int nbMethod = 0;
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);
            //System.out.println("Classes name: " + nodeClass.getName());
            nbMethod = visitorMethod.getMethods().size();
            if (nbMethod >= x) {
                myClasses.put(String.valueOf(nodeClass.getName()), nbMethod);
            }

        }
        Set<Map.Entry<String, Integer>> mylistXclass = myClasses.entrySet();
        System.out.println("Les classes ayant un nombre de méthodes superieur à " + x + ": ");
        for (Map.Entry<String, Integer> mapping : mylistXclass) {
            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
        }

        return myClassList;

    }

    //12. Les	10%	des	méthodes	qui	possèdent	le	plus	grand	nombre	de	lignes	de	code	(par classe).

    public static void maxLigneMethodeByClass(CompilationUnit cu) {
        /*pour chaque classe on va recuperer le nombre des methodes, puis on va calculer les 10% de ce nombre, puis on parcour les methode
        et on calcule le nombre de ligne pour chacune, puis on trie la liste selon la valeur du nombre des ligne et on affiche la liste
        *
        * */
        HashMap<String, Integer> myMethods = new HashMap<String, Integer>();
        HashMap<String, Integer> myMethodList = new HashMap<>();

        int nbMethod = 0;
        int percent = 0;
        int nbLines = 0;
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);
            System.out.println("Classes name: " + nodeClass.getName());
            nbMethod = visitorMethod.getMethods().size();
            percent = (int) (nbMethod * 0.1);
            if (percent == 0) {
                percent = 1;
            }
            for (MethodDeclaration method : visitorMethod.getMethods()) {
                int start = method.getStartPosition(); // first character position
                int end = start + method.getLength(); // last character position
                int lineStart = parse.getLineNumber(start);
                int lineEnd = parse.getLineNumber(end);
                nbLines = lineEnd - lineStart;

                myMethods.put(String.valueOf(method.getName()), nbLines);
            }
            myMethodList = (HashMap<String, Integer>) sortMapMethodIntegerAndLimit(myMethods,percent);

            Set set = myMethodList.entrySet();

            // Obtenir l'iterator pour parcourir la liste
            Iterator itr = set.iterator();
            //System.out.println("******** myMethodList size: "+myMethodList.size());
            System.out.println("------------nb de methode de la classe " + nodeClass.getName() + ": " + nbMethod+"--------------------");

            // Afficher tous les éléments de la liste
            while(itr.hasNext()) {
                Map.Entry mentry = (Map.Entry)itr.next();
                System.out.print("Methode: "+mentry.getKey() + " - ");
                System.out.println("NBLigne: "+mentry.getValue()+"\n\n\n");
            }
            // Collections.reverse(myMethodList);
           /* System.out.println("les 10% des méthode ayant le plus grand nombre de lignes pour la classe: " + nodeClass.getName());
            for (int i = 0; i < percent; i++) {
                System.out.println(" " + myMethodList.get(i));
            }*/
        }
    }


    //13. Le	nombre	maximal	de	paramètres	par	rapport	à	toutes	les	méthodes	de l’application.
    public static void maximalParameterOfMethods(CompilationUnit cu) {
        HashMap<String, Integer> myMethods = new HashMap<String, Integer>();

        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {

            MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
            parse.accept(visitor);

            for (MethodDeclaration method : visitor.getMethods()) {

                myMethods.put(String.valueOf(method.getName()), method.parameters().size());
            }
        }
        HashMap<String, Integer> myMethodsSortedByPram = (HashMap<String, Integer>) sortMapMethodIntegerAndLimit(myMethods, 1);
        Map.Entry<String, Integer> mapping = myMethodsSortedByPram.entrySet().iterator().next();
        String key = mapping.getKey();
        String value = String.valueOf(mapping.getValue());
        System.out.println("La méthode ayant le plus grand nombre de paramettres est: " + key + " le nombre de ses paramettres est: " + value);

    }

    //Méthode pour faire le trie d'une HashMap
    public static List<String> listSort(HashMap<String, Integer> myMap) {
        //	System.out.println("HashMap before sorting, random order ");
        Set<Map.Entry<String, Integer>> entries = myMap.entrySet();
		/*for(Map.Entry<String, Integer> entry : entries){
			System.out.println(entry.getKey() + " ==> " + entry.getValue()); }*/


        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        List<Map.Entry<String, Integer>> listOfEntries
                = new ArrayList<Map.Entry<String, Integer>>(entries);

        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());

        for (Map.Entry<String, Integer> entry : listOfEntries) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }
        List<String> maliste = new ArrayList<>();
        //System.out.println("HashMap after sorting entries by values ");
        Set<Map.Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet();
        for (Map.Entry<String, Integer> mapping : entrySetSortedByValue) {
            maliste.add(mapping.getKey());
          //  System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
        }

        return maliste;

    }


    public static HashMap<String, Integer> hashMapSort(HashMap<String, Integer> myMap) {
        //	System.out.println("HashMap before sorting, random order ");
        Set<Map.Entry<String, Integer>> entries = myMap.entrySet();
		/*for(Map.Entry<String, Integer> entry : entries){
			System.out.println(entry.getKey() + " ==> " + entry.getValue()); }*/


        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                Integer v1 = e1.getValue();
                Integer v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };

        List<Map.Entry<String, Integer>> listOfEntries
                = new ArrayList<Map.Entry<String, Integer>>(entries);

        Collections.sort(listOfEntries, valueComparator);
        LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());

        for (Map.Entry<String, Integer> entry : listOfEntries) {
            sortedByValue.put(entry.getKey(), entry.getValue());
        }
        //List<String> maliste = new ArrayList<>();
        //System.out.println("HashMap after sorting entries by values ");
        Set<Map.Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet();
        /*for (Map.Entry<String, Integer> mapping : entrySetSortedByValue) {
            maliste.add(mapping.getKey());
           // System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
        }*/

        return sortedByValue;

    }


    private static Map<String, Integer> sortMapMethodIntegerAndLimit(Map<String, Integer> map, int numberExpected) {
        // desc on map values
        return map
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // delete Comparator.reverseOrder() from parenthesis -> asc on map values
                .limit(numberExpected)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    }
}
