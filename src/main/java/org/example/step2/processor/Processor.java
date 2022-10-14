package org.example.step2.processor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.example.step2.Main;
import org.example.step2.graph.Graph;
import org.example.step2.graph.Vertex;
import org.example.step2.parser.MyParser;
import org.example.step2.visitor.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Processor {


    private String path;
    private MyParser parser;//=new MyParser("C:\\Users\\SCD UM\\Downloads\\mini_project_in_project\\project\\src");
    private ClassDeclarationVisitor classDeclarationVisitor;
    private PackageDeclarationVisitor packageDeclarationVisitor;
    private Visitor visitor;

    public Processor(String path) {

        this.parser = new MyParser(path);
        this.path = parser.getProjectSourcePath();
        this.classDeclarationVisitor = new ClassDeclarationVisitor();
        this.packageDeclarationVisitor = new PackageDeclarationVisitor();
        visitor = new Visitor();
    }


    //12.bis
    public String top10PercentMethodByLinesByClass() throws FileNotFoundException, IOException {
        List<String> filesContent = this.getJavaFiles();
        ClassDeclarationVisitor visitor = new ClassDeclarationVisitor();
        CompilationUnit parse;
        Map<String, Integer> classMethodSorted = new HashMap<>();

        int percent = 0;

        for (String fileContent : filesContent) {
            parse = parser.getCompilationUnit(fileContent);
            parse.accept(visitor);
        }
        Map<TypeDeclaration, Map<String, Integer>> classContent = new HashMap<>(visitor.getClasseCollection());

        Set set = classContent.entrySet();

        Iterator itr = set.iterator();
        Map<String, Integer> mapMethod = new HashMap<>();
        Map<String, Integer> mapMethodSort = new HashMap<>();
        String str=new String();
        // Afficher tous les éléments de la liste
        while (itr.hasNext()) {
            Map.Entry mentry = (Map.Entry) itr.next();
            TypeDeclaration typeDeclaration = (TypeDeclaration) mentry.getKey();
            percent = (int) ((typeDeclaration.getMethods().length) * 0.1);
            if (percent == 0) {
                percent = 1;
            }
            System.out.println("nom de classe : "+typeDeclaration.getName());
            mapMethod = (Map<String, Integer>) mentry.getValue();

            List<String> ls = listSort((HashMap<String, Integer>) mapMethod);
            Collections.reverse(ls);
            if(ls.size()==0){

               // System.out.println( "Cette classe n'a pas de méthode\n");
                str="Cette classe n'a pas de méthode\n";
            } else {
            for (int i=0;i<percent;i++){
                str="Methode N° "+i+1+": "+ls.get(i)+"\n";
                //return  str;
            }

            }
            System.out.println(str);


        }



        return str;
    }



    private int percentageCalculation(int nbMethod) {
        int percent = 0;
        percent = ((int) (nbMethod * 0.1));
        if (percent == 0) {
            percent = 1;
        }
        return percent;
    }


    public void infoApplication() throws IOException {
        // 1
        // System.out.println("Nombre de classes de l'application : " + classDeclarationVisitor.getNbClasses());
        System.out.println("Nombre de classes de l'application : " + nbClasses(parser.getParse()));

        //2
        System.out.println("Nombre de ligne de l'application : " + parser.getNbLinesOfCodes());
        //3
        System.out.println("Nombre de methode de l'application : " + numberOfMethods(parser.getParse()));
        //4. Nombre	total	de	packages	de	l’application.
        System.out.println("Nombre de package de l'application: " + packageDeclarationVisitor.getNbPackages());
        //5
        System.out.println("Nombre moyen de méthode: " + averageMethode(parser.getParse()));
        //6
        System.out.println("Nombre moyen de ligne par méthode: " + averageLinesByMethod(parser.getParse()));
        //7. Nombre	moyen	d’attributs	par	classe.

        System.out.println("Nombre attr moy: " + ClassesAttributes(parser.getParse()) / nbClasses(parser.getParse()));

        //8. Les	10%	des	classes	qui	possèdent	le	plus	grand	nombre	de	méthodes.
        top10ClassByMthodNumber(parser.getParse());
        //9. Les	10%	des	classes	qui	possèdent	le	plus	grand	nombre d’attributs.
        top10ClassByAttributes(parser.getParse());
        //10. Les	classes	qui	font	partie	en	même	temps	des	deux	catégories	précédentes.
        top10ClaassByMethAndAttr(parser.getParse());
        //11. Les	classes	qui	possèdent plus	de	X	méthodes	(la	valeur	de X	est	donnée).
        classWithXmethod(parser.getParse(), 3);
        //12. Les	10%	des	méthodes	qui	possèdent	le	plus	grand	nombre	de	lignes	de	code	(par cl
        //maxLigneMethodeByClass(parser.getParse());
        System.out.println("Les 10% des méthodes ayant le plus grand nombre de lignes par classe: ");
        top10PercentMethodByLinesByClass();
        //13
        maximalParameterOfMethods(parser.getParse());
        callGraph();
    }


    public int nbClasses(CompilationUnit parse) {
        ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
        parse.accept(visitorClasses);
        return visitorClasses.getNbClasses();
    }


    public List<String> getJavaFiles() {
        File directory = new File(path);
        return parser.getFilesPaths(directory);
    }

    public int getNbLines(CompilationUnit cu) {
        //CompilationUnit cu = null;
        ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
        visitorClasses.setCu(cu);
        cu.accept(visitorClasses);
        String fileContent = null;
        int nbLines = 0;
        //visitorClasses.setContent(contentOfFiles.toString());
        File directory = new File(path);
        List<String> listContent = parser.getFilesPaths(directory);
        String content = null;
        for (String s : listContent) {
            fileContent = visitorClasses.getContent();
            nbLines += visitorClasses.numberOfLines(fileContent);
            System.out.println("nb lines :" + nbLines);
        }
        System.out.println("my content " + content);

        return nbLines;
    }

    public int numberOfMethods(CompilationUnit cu) {
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        int nbMéthode = 0;
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);
            nbMéthode += visitorMethod.getNbMethod();
        }

        //System.out.println("Le nombre de méthodes de l'application est: " + visitorMethod.getNbMethod());
        return nbMéthode;
    }

    //4
    public int nbPackages() throws IOException {
        /*int nbPackage=0;
        PackageDeclarationVisitor visitorPackages = new PackageDeclarationVisitor();
        cu.accept(visitorPackages);
        for (String packageDeclaration : visitorPackages.getPackages()){
           // nbp.add(packageDeclaration);
             nbPackage += visitorPackages.getPackages().size();
            System.out.println("package :"+packageDeclaration);
        }*/

        Set<String> nbp = new HashSet<>();
        List<CompilationUnit> listCu = new ArrayList<CompilationUnit>();
        // listCu= parser.getCompUnits();
        //System.out.println("cu: "+listCu.size());

        int nbPackage = 0;
        for (CompilationUnit cu : listCu) {
            PackageDeclarationVisitor visitorPackages = new PackageDeclarationVisitor();
            cu.accept(visitorPackages);
            nbPackage += visitorPackages.getNbPackages();
           /* for (String packageDeclaration : visitorPackages.getPackages()){
                nbp.add(packageDeclaration);
                nbPackage+=visitorPackages.getPackages().size();
                System.out.println("package :"+packageDeclaration);
            }*/
        }


        return nbPackage;
    }

    public void parsePackages() throws FileNotFoundException, IOException {
        List<String> javaFilesPaths = this.getJavaFiles();
        CompilationUnit parse = null;

        for (String filePath : javaFilesPaths) {
            parse = parser.getCompilationUnit(filePath);
            PackageDeclarationVisitor.setCompilationUnit(parse);
            parse.accept(packageDeclarationVisitor);
        }

    }

    public void parseV() throws FileNotFoundException, IOException {
        List<String> javaFilesPaths = this.getJavaFiles();
        CompilationUnit parse = null;

        for (String filePath : javaFilesPaths) {
            parse = parser.getCompilationUnit(filePath);
            Visitor.setCu(parse);
            parse.accept(visitor);
        }
    }


    public int averageMethode(CompilationUnit parse) {
        return (int) numberOfMethods(parse) / nbClasses(parse);
    }


    public int averageLinesByMethod(CompilationUnit parse) {
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

    public List<String> top10ClassByMthodNumber(CompilationUnit cu) {
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


    public List<String> top10ClassByAttributes(CompilationUnit cu) {
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

    public List<String> top10ClaassByMethAndAttr(CompilationUnit cu) {
        List<String> listOfClassesMaxMethod = new ArrayList<>();
        List<String> listOfClassesMaxAttrb = new ArrayList<>();
        List<String> listOfBoth = new ArrayList<>();
        listOfClassesMaxMethod = top10ClassByMthodNumber(cu);
        listOfClassesMaxAttrb = top10ClassByAttributes(cu);
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
    public String classWithXmethod(CompilationUnit cu, int x) {
        List<String> myClassList = new ArrayList<>();
        HashMap<String, Integer> myClasses = new HashMap<String, Integer>();
        String s = null;

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
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> mapping : mylistXclass) {
            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
            s += mapping.getKey() + " ==> " + mapping.getValue() + "\n";
        }

        return s;

    }
    //12. Les	10%	des	méthodes	qui	possèdent	le	plus	grand	nombre	de	lignes	de	code	(par classe).

    public void maxLigneMethodeByClass(CompilationUnit cu) throws IOException {
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





        /*for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            // nodeClass.accept(visitorMethod);
            //System.out.println("Classes name: " + nodeClass.getName());
            nbMethod = visitorMethod.getMethods().size();
            percent = (int) (nbMethod * 0.1);
            if (percent == 0) {
                percent = 1;
            }
            System.out.println("classes : " + nodeClass.getName().toString() + " nb methods " + nodeClass.getMethods().length);
            for(MethodDeclaration m : nodeClass.getMethods()) {
               // System.out.println("methodss " + m);
            }
            for (MethodDeclaration method : nodeClass.getMethods()) {
                int start = method.getStartPosition(); // first character position
                int end = start + method.getLength(); // last character position
                int lineStart = parser.getParse().getLineNumber(start);
                int lineEnd = parser.getParse().getLineNumber(end);
                nbLines = lineEnd - lineStart;

                myMethods.put(String.valueOf(method.getName()), nbLines);
            }
            myMethodList = (HashMap<String, Integer>) sortMapMethodIntegerAndLimit(myMethods, percent);

            Set set = myMethodList.entrySet();

            // Obtenir l'iterator pour parcourir la liste
            Iterator itr = set.iterator();
            //System.out.println("******** myMethodList size: "+myMethodList.size());
            System.out.println("------------nb de methode de la classe " + nodeClass.getName() + ": " + nbMethod + "--------------------");

            // Afficher tous les éléments de la liste
            while (itr.hasNext()) {
                Map.Entry mentry = (Map.Entry) itr.next();
                System.out.print("Methode: " + mentry.getKey() + " - ");
                System.out.println("NBLigne: " + mentry.getValue() + "\n\n\n");
            }
            // Collections.reverse(myMethodList);
           /* System.out.println("les 10% des méthode ayant le plus grand nombre de lignes pour la classe: " + nodeClass.getName());
            for (int i = 0; i < percent; i++) {
                System.out.println(" " + myMethodList.get(i));
            }
        }*/


    }


    //13. Le	nombre	maximal	de	paramètres	par	rapport	à	toutes	les	méthodes	de l’application.
    public String maximalParameterOfMethods(CompilationUnit cu) throws IOException {
        HashMap<String, Integer> myMethods = new HashMap<String, Integer>();
        String s = null;
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);
        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {

            MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
            parser.getParse().accept(visitor);

            for (MethodDeclaration method : visitor.getMethods()) {

                myMethods.put(String.valueOf(method.getName()), method.parameters().size());
            }
        }
        HashMap<String, Integer> myMethodsSortedByPram = (HashMap<String, Integer>) sortMapMethodIntegerAndLimit(myMethods, 1);
        Map.Entry<String, Integer> mapping = myMethodsSortedByPram.entrySet().iterator().next();
        String key = mapping.getKey();
        String value = String.valueOf(mapping.getValue());
        System.out.println("La méthode ayant le plus grand nombre de paramettres est: " + key + " le nombre de ses paramettres est: " + value);
        s = "La méthode ayant le plus grand nombre de paramettres est: " + key + "\n le nombre de ses paramettres est: " + value;
        return s;
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








    /*Pour l'interface graphique********************************************/


    public String exercice1() throws IOException {
        return String.valueOf(nbClasses(parser.getParse()));
    }

    public String exercice2() throws IOException {
        return String.valueOf(parser.getNbLinesOfCodes());
    }

    public String exercice3() throws IOException {
        return String.valueOf(numberOfMethods(parser.getParse()));
    }

    public String exercice4() throws IOException {
        return String.valueOf(packageDeclarationVisitor.getNbPackages());
    }

    public String exercice5() throws IOException {
        return String.valueOf(averageMethode(parser.getParse()));
    }

    public String exercice6() throws IOException {
        return String.valueOf(averageLinesByMethod(parser.getParse()));
    }

    public String exercice7() throws IOException {
        return String.valueOf(ClassesAttributes(parser.getParse()) / nbClasses(parser.getParse()));
    }

    public String exercice8() throws IOException {
        return returnListAsString(top10ClassByMthodNumber(parser.getParse()));

    }

    public String exercice9() throws IOException {
        return returnListAsString(top10ClassByAttributes(parser.getParse()));

    }

    public String exercice10() throws IOException {
        return returnListAsString(top10ClaassByMethAndAttr(parser.getParse()));

    }

    public String exercice11() throws IOException {
        return classWithXmethod(parser.getParse(), 3);

    }
    public String exercice12() throws IOException {
        return top10PercentMethodByLinesByClass();

    }

    public String exercice13() throws IOException {
        return maximalParameterOfMethods(parser.getParse());

    }

    public String returnListAsString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list.isEmpty()) {
            sb.append("Aucun élément à afficher");
        } else {
            for (String elt : list) {
                sb.append(Main.indentationFormat + "" + elt);
            }
        }
        return sb.toString();
    }


    /*-----------------------------------------------*/
    //graph
    public Graph callGraph() throws IOException {
        Graph<Vertex> g = new Graph<Vertex>();
        Vertex source=new Vertex();
        Vertex target=new Vertex();
        List<String> filesContent = this.getJavaFiles();
        ClassDeclarationVisitor visitor = new ClassDeclarationVisitor();
        CompilationUnit parse;
        for (String fileContent : filesContent) {
            parse = parser.getCompilationUnit(fileContent);
            parse.accept(visitor);
        }
        Map<TypeDeclaration, Set<MethodDeclaration>> classContent = new HashMap<>(visitor.getClasseMapMethods());
        Set set = classContent.entrySet();

        Iterator itr = set.iterator();
        Set<MethodDeclaration> mapMethod = new HashSet<>();
        while (itr.hasNext()) {
            Map.Entry mentry = (Map.Entry) itr.next();
            //la classe en question
            TypeDeclaration typeDeclaration = (TypeDeclaration) mentry.getKey();
            //get la liste de methodes
            mapMethod = (Set<MethodDeclaration>) mentry.getValue();


            for (MethodDeclaration method : mapMethod) {
                MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
                method.accept(visitor2);
                source=new Vertex(String.valueOf(method.getName()),String.valueOf(typeDeclaration.getName()));
                for (MethodInvocation methodInvocation : visitor2.getMethods()) {
                   /* System.out.println("method " + method.getName() + " invoc method "
                            + methodInvocation.getName());*/
                    String type=" type invoc";
                    target=new Vertex(String.valueOf(methodInvocation.getName()),type);
                    g.addEdge(source,target);


                }

            }

        }

        System.out.println("Graph:\n"
                +g.toString());

        return g;
    }

}