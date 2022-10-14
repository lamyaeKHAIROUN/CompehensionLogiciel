package org.example.step2.processor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.example.step2.Main;
import org.example.step2.graph.Graph;
import org.example.step2.graph.Vertex;
import org.example.step2.parser.MyParser;
import org.example.step2.visitor.*;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class Processor {
    static  String  str=new String();

	private String path;
	private MyParser parser;
	private ClassDeclarationVisitor classDeclarationVisitor;
	private PackageDeclarationVisitor packageDeclarationVisitor;
	private Visitor visitor;
	private DefaultDirectedGraph<String, DefaultEdge> graphJGraphT= new DefaultDirectedGraph<>(DefaultEdge.class);
    private Set<String> setLink=new HashSet<>();
    private Map<TypeDeclaration, Map<MethodDeclaration, Set<MethodInvocation>>> mapTheCallGraph =new HashMap<>();




	public Processor(String path) {

		this.parser = new MyParser(path);
		this.path = parser.getProjectSourcePath();
		this.classDeclarationVisitor = new ClassDeclarationVisitor();
		this.packageDeclarationVisitor = new PackageDeclarationVisitor();
		visitor = new Visitor();
	}

	public void parseClasses() throws FileNotFoundException, IOException {
		List<String> javaFilesPaths = this.getJavaFiles();
		CompilationUnit parse;
		for (String filePath : javaFilesPaths) {
			parse = parser.getCompilationUnit(filePath);
			classDeclarationVisitor.setCu(parse);
			parse.accept(classDeclarationVisitor);

		}

	}

	public void infoApplication() throws IOException {
		int choix = 0;

		while (true) {
			System.out.println("- Tapez 1 pour afficher le nombre de classes de l'application ");
			System.out.println("- Tapez 2 pour afficher le nombre de lignes de code de l'application ");
			System.out.println("- Tapez 3 pour afficher nombre total de methodes de l'application ");
			System.out.println("- Tapez 4 pour afficher nombre total de packages de l'application ");
			System.out.println("- Tapez 5 pour afficher nombre moyen de methodes par classe ");
			System.out.println("- Tapez 6 pour afficher nombre moyen de lignes de code  par methode ");
			System.out.println("- Tapez 7 pour afficher nombre moyen d'attributs par classe ");
			System.out.println("- Tapez 8 pour afficher les 10% des classes avec le plus grand nbr de methode ");
			System.out.println("- Tapez 9 pour afficher les 10% des classes avec le plus grand nbr d'attributs ");
			System.out.println("- Tapez 10 pour afficher les 10% des classes avec le plus grand nbr d'attributs et methodes");
			System.out.println("- Tapez 11 pour afficher les classes ayant un nombre de methodes superieur a X");
			System.out.println("- Tapez 12 pour afficher les 10% des classes avec le plus grand nbr d'attributs et methodes");
			System.out.println("- Tapez 13 pour Le nombre maximal de param par rapport a tous les meth de l'appli");
			System.out.println("- Tapez 14 pour l'affichage du graphe d'appel sur la console");
			System.out.println("- Tapez 0 pour quitter le menu");

			Scanner sc = new Scanner(System.in);
			choix = sc.nextInt();
			int choix13=0;
			switch (choix) {
			case 0:{
				System.out.println("A dios!Pour revenir au premiere menu, relancer le prog");
				sc.close();
				System.exit(0);
				break;
			}
			case 1: {
				System.out.println("************** Nombre de classes de l'application : " + nbClasses(parser.getParse())+" **************");
				break;
			}
			case 2: {
				System.out.println("************* Nombre de ligne de l'application : " + parser.getNbLinesOfCodes()+" **************");
				break;
			}
			case 3: {
				System.out.println("************* Nombre total de methode de l'application : " + numberOfMethods(parser.getParse())+" *************");
				break;
			}
			case 4: {
				System.out.println("************* Nombre total de package de l'application: " + packageDeclarationVisitor.getNbPackages()+" *************");
				break;

			}
			case 5: {
				System.out.println("************* Nombre moyen de methode: " + averageMethode(parser.getParse())+" *************");
				break;
			}
			case 6: {
				System.out.println("************* Nombre moyen de ligne par methode: " + averageLinesByMethod(parser.getParse())+" *************");
				break;
			}
			case 7: {
				System.out.println(
						"************* Nombre d'attributs moyen par classe: " + ClassesAttributes(parser.getParse()) / nbClasses(parser.getParse())+" *************");

				break;
			}
			case 8: {
				top10ClassByMthodNumber(parser.getParse());
				break;
			}
			case 9: {
				top10ClassByAttributes(parser.getParse());
				break;
			}
			case 10: {
				top10ClaassByMethAndAttr(parser.getParse());
				break;
			}
			case 11: {
				System.out.println("Rentrer une valeur pour x :");
				choix13 = sc.nextInt();
                
				classWithXmethod(parser.getParse(), choix13);
				break;
			}
			case 12: {
				
				System.out.println("Les 10% des methodes ayant le plus grand nombre de lignes par classe: ");
		        top10PercentMethodByLinesByClass();
				break;
			}
			case 13: {
				maximalParameterOfMethods(parser.getParse());
				break;
			}
			
			case 14: {
				System.out.println("Vous allez trouvez l'image correspondante a notre graphe d'appel à cet endroit: ");
				//callGraph();
				graphData(parser.getParse());
				buildGraphWithJGraphT();
				break;
			}
			default: {
				System.err.println("Vous devez choisir un nombre parmis ceux qui sont propose dans le menu");
				break;
			}
			}

		}
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
		// CompilationUnit cu = null;
		ClassDeclarationVisitor visitorClasses = new ClassDeclarationVisitor();
		visitorClasses.setCu(cu);
		cu.accept(visitorClasses);
		String fileContent = null;
		int nbLines = 0;
		// visitorClasses.setContent(contentOfFiles.toString());
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

		// System.out.println("Le nombre de méthodes de l'application est: " +
		// visitorMethod.getNbMethod());
		return nbMéthode;
	}

	// 4
	public int nbPackages() throws IOException {
		/*
		 * int nbPackage=0; PackageDeclarationVisitor visitorPackages = new
		 * PackageDeclarationVisitor(); cu.accept(visitorPackages); for (String
		 * packageDeclaration : visitorPackages.getPackages()){ //
		 * nbp.add(packageDeclaration); nbPackage +=
		 * visitorPackages.getPackages().size();
		 * System.out.println("package :"+packageDeclaration); }
		 */

		Set<String> nbp = new HashSet<>();
		List<CompilationUnit> listCu = new ArrayList<CompilationUnit>();
		// listCu= parser.getCompUnits();
		// System.out.println("cu: "+listCu.size());

		int nbPackage = 0;
		for (CompilationUnit cu : listCu) {
			PackageDeclarationVisitor visitorPackages = new PackageDeclarationVisitor();
			cu.accept(visitorPackages);
			nbPackage += visitorPackages.getNbPackages();
			/*
			 * for (String packageDeclaration : visitorPackages.getPackages()){
			 * nbp.add(packageDeclaration); nbPackage+=visitorPackages.getPackages().size();
			 * System.out.println("package :"+packageDeclaration); }
			 */
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

		// l'idee est de creer un tablau ou je vais trier les classes a l'aide du nombre
		// de methode puis je vais afficher les 10% premier case

		ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
		cu.accept(visitorClass);
		for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
			MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
			nodeClass.accept(visitorMethod);
			// System.out.println("Classes name: " + nodeClass.getName());
			nbMethod = visitorMethod.getMethods().size();
			myClasses.put(String.valueOf(nodeClass.getName()), nbMethod);
			malisteTriee = listSort(myClasses);
			Collections.reverse(malisteTriee);
		}
		for (int i = 0; i < disPourCentClasses; i++) {
			myDisPercentList.add(malisteTriee.get(i));
			System.out.println("***** les 10% des classes ayant le plus grand nombre de methode: " + malisteTriee.get(i)+" *****");
		}
		return myDisPercentList;
	}

	public static List<String> listSort(HashMap<String, Integer> myMap) {
		// System.out.println("HashMap before sorting, random order ");
		Set<Entry<String, Integer>> entries = myMap.entrySet();
		/*
		 * for(Map.Entry<String, Integer> entry : entries){
		 * System.out.println(entry.getKey() + " ==> " + entry.getValue()); }
		 */

		Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
				Integer v1 = e1.getValue();
				Integer v2 = e2.getValue();
				return v1.compareTo(v2);
			}
		};

		List<Entry<String, Integer>> listOfEntries = new ArrayList<Entry<String, Integer>>(entries);

		Collections.sort(listOfEntries, valueComparator);
		LinkedHashMap<String, Integer> sortedByValue = new LinkedHashMap<String, Integer>(listOfEntries.size());

		for (Entry<String, Integer> entry : listOfEntries) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		List<String> maliste = new ArrayList<>();
		// System.out.println("HashMap after sorting entries by values ");
		Set<Entry<String, Integer>> entrySetSortedByValue = sortedByValue.entrySet();
		for (Entry<String, Integer> mapping : entrySetSortedByValue) {
			maliste.add(mapping.getKey());
			// System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
		}

		return maliste;

	}

	public List<String> top10PercentMethodByLinesByClass() throws FileNotFoundException, IOException {
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
        List<String> listString=new ArrayList<>();

        Iterator itr = set.iterator();
        Map<String, Integer> mapMethod = new HashMap<>();
        Map<String, Integer> mapMethodSort = new HashMap<>();
        // Afficher tous les éléments de la liste
        while (itr.hasNext()) {
            Entry mentry = (Entry) itr.next();
            TypeDeclaration typeDeclaration = (TypeDeclaration) mentry.getKey();
            percent = (int) ((typeDeclaration.getMethods().length) * 0.1);
            if (percent == 0) {
                percent = 1;
            }
            String s=String.valueOf(typeDeclaration.getName());
            mapMethod = (Map<String, Integer>) mentry.getValue();

            List<String> ls = listSort((HashMap<String, Integer>) mapMethod);
            Collections.reverse(ls);
            if(ls.size()==0){
               // System.out.println( "Cette classe n'a pas de méthode\n");
                str=" Classe : "+ s +"\n Cette classe n'a pas de méthode\t";
                listString.add(str);
            } else {
            for (int i=0;i<percent;i++){
            	int index=i+1;
                str="Classe : "+s+" ,methode numero "+index+" : "+ls.get(i)+"\n";
                listString.add(str);

                //return  str;
            }

            }

            System.out.println(str);

        }

        return listString;
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
		// l'idee est de creer un tablau ou je vais trier les classes a l'aide du nombre
		// de methode puis je vais afficher les 10% premier case

		ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
		cu.accept(visitorClass);
		for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
			FieldAccessVisitor visitorAttributes = new FieldAccessVisitor();
			nodeClass.accept(visitorAttributes);
			nbAttributes = visitorAttributes.getFields().size();
			// System.out.println("Classes name: " + nodeClass.getName());
			myClasses.put(String.valueOf(nodeClass.getName()), nbAttributes);
			malisteTriee = listSort(myClasses);
			Collections.reverse(malisteTriee);
		}
		for (int i = 0; i < disPourCentClasses; i++) {
			myDisPercentList.add(malisteTriee.get(i));
			System.out.println("***** les 10% des classes ayant le plus grand nombre d'attributs: " + malisteTriee.get(i)+" *****");
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
			System.out.println(" ***** Les 10% des classes ayant le plus grand nombre de méthodes et d'attributs: " + s +" *****");

		}
		return listOfBoth;
	}

	// 11. Les classes qui possèdent plus de X méthodes (la valeur de X est donnée).
	public Set<Entry<String, Integer>> classWithXmethod(CompilationUnit cu, int x) {
		List<String> myClassList = new ArrayList<>();
		HashMap<String, Integer> myClasses = new HashMap<String, Integer>();

		int nbMethod = 0;
		ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
		cu.accept(visitorClass);
		for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
			MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
			nodeClass.accept(visitorMethod);
			// System.out.println("Classes name: " + nodeClass.getName());
			nbMethod = visitorMethod.getMethods().size();
			if (nbMethod >= x) {
				myClasses.put(String.valueOf(nodeClass.getName()), nbMethod);
			}

		}
		Set<Entry<String, Integer>> mylistXclass = myClasses.entrySet();
		System.out.println("***** Les classes ayant un nombre de methodes superieur a " + x + " sont : *****************");
		for (Entry<String, Integer> mapping : mylistXclass) {
			System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
		}

		return mylistXclass;

	}
	// 12. Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code
	// (par classe).

	public void maxLigneMethodeByClass(CompilationUnit cu) throws IOException {
		/*
		 * pour chaque classe on va recuperer le nombre des methodes, puis on va
		 * calculer les 10% de ce nombre, puis on parcour les methode et on calcule le
		 * nombre de ligne pour chacune, puis on trie la liste selon la valeur du nombre
		 * des ligne et on affiche la liste
		 *
		 */
		HashMap<String, Integer> myMethods = new HashMap<String, Integer>();
		HashMap<String, Integer> myMethodList = new HashMap<>();

		int nbMethod = 0;
		int percent = 0;
		int nbLines = 0;
		ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
		cu.accept(visitorClass);
		for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
			MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
			// nodeClass.accept(visitorMethod);
			// System.out.println("Classes name: " + nodeClass.getName());
			nbMethod = visitorMethod.getMethods().size();
			percent = (int) (nbMethod * 0.1);
			if (percent == 0) {
				percent = 1;
			}
			System.out.println("*******************************************************");
			System.out.println(
					" - Classes : " + nodeClass.getName().toString() + " ,nombre de methods : " + nodeClass.getMethods().length);
			for (MethodDeclaration m : nodeClass.getMethods()) {
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
			// System.out.println("******** myMethodList size: "+myMethodList.size());
			System.out.println("Nombre de methode de la classe " + nodeClass.getName() + ": " + nbMethod);

			// Afficher tous les éléments de la liste
			while (itr.hasNext()) {
				Entry mentry = (Entry) itr.next();
				System.out.print("Methode: " + mentry.getKey() + " - ");
				System.out.println("NBLigne: " + mentry.getValue() + "\n\n\n");
			}

		}
	}

	// 13. Le nombre maximal de paramètres par rapport à toutes les méthodes de
	// l’application.
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
		HashMap<String, Integer> myMethodsSortedByPram = (HashMap<String, Integer>) sortMapMethodIntegerAndLimit(
				myMethods, 1);
		Entry<String, Integer> mapping = myMethodsSortedByPram.entrySet().iterator().next();
		String key = mapping.getKey();
		String value = String.valueOf(mapping.getValue());
		System.out.println("**************************************************");
		System.out.println(" - La methode ayant le plus grand nombre de paramettres est: " + key);
		System.out.println(" - Le nombre de ses paramettres est: " + value);
		s = "************************************************** \n"+"- La méthode ayant le plus grand nombre de paramettres est: " + key + "\n -Le nombre de ses paramettres est: "
				+ value;
		System.out.println("**************************************************");

		return s;
	}

	private static Map<String, Integer> sortMapMethodIntegerAndLimit(Map<String, Integer> map, int numberExpected) {
		// desc on map values
		return map.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())) // delete
																										// Comparator.reverseOrder()
																										// from
																										// parenthesis
																										// -> asc on map
																										// values
				.limit(numberExpected).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	}

	public String btn1() throws IOException {
		return String.valueOf(nbClasses(parser.getParse()));
	}

	public String btn2() throws IOException {
		return String.valueOf(parser.getNbLinesOfCodes());
	}

	public String btn3() throws IOException {
		return String.valueOf(numberOfMethods(parser.getParse()));
	}

	public String btn4() throws IOException {
		return String.valueOf(packageDeclarationVisitor.getNbPackages());
	}

	public String btn5() throws IOException {
		return String.valueOf(averageMethode(parser.getParse()));
	}

	public String btn6() throws IOException {
		return String.valueOf(averageLinesByMethod(parser.getParse()));
	}

	public String btn7() throws IOException {
		return String.valueOf(ClassesAttributes(parser.getParse()) / nbClasses(parser.getParse()));
	}

	public String btn8() throws IOException {
		return returnListAsString(top10ClassByMthodNumber(parser.getParse()));

	}

	public String btn9() throws IOException {
		return returnListAsString(top10ClassByAttributes(parser.getParse()));

	}

	public String btn10() throws IOException {
		return returnListAsString(top10ClaassByMethAndAttr(parser.getParse()));

	}

	public String exercice11() throws IOException {
		return returnAsString(classWithXmethod(parser.getParse(), 3));

	}
	 public String btn12() throws IOException {
	        return returnListAsString(top10PercentMethodByLinesByClass());

	    }

	public String btn11(int x) throws IOException {
		return returnAsString(classWithXmethod(parser.getParse(), x));
	}

	public String btn13() throws IOException {
		return maximalParameterOfMethods(parser.getParse());

	}
	
	public String btn14() throws IOException {
		graphData(parser.getParse());
		String s=buildGraphWithJGraphT();
	
		return  "Vous allez trouvez l'image correspondante a notre graphe d'appel à cet endroit:\n "+s;

	}

	public String returnAsString(Set<Entry<String, Integer>> set) {
		StringBuilder sb = new StringBuilder();
		if (set.isEmpty()) {
			sb.append("Aucun élément à afficher");
		} else {
			for (Entry<String, Integer> elt : set) {
				sb.append(Main.indentationFormat + "" + elt +" \n");
			}
		}
		return sb.toString();
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

            Map<TypeDeclaration, Set<MethodDeclaration>> classContent = new HashMap<>(visitor.getClasseMapMethods());
            Set set = classContent.entrySet();

            Iterator itr = set.iterator();
            Set<MethodDeclaration> mapMethod = new HashSet<>();
            while (itr.hasNext()) {
                Entry mentry = (Entry) itr.next();
                //la classe en question
                TypeDeclaration typeDeclaration = (TypeDeclaration) mentry.getKey();
                //get la liste de methodes
                mapMethod = (Set<MethodDeclaration>) mentry.getValue();


                for (MethodDeclaration method : mapMethod) {
                	MethodIvocationVisitor visitor2 = new MethodIvocationVisitor();
                    method.accept(visitor2);
                    source = new Vertex(String.valueOf(method.getName()), String.valueOf(typeDeclaration.getName()));
                    for (MethodInvocation methodInvocation : visitor2.getMethods()) {
                       
                    	String type=null;
                        target = new Vertex(String.valueOf(methodInvocation.getName()), type);
                        g.addEdge(source.getType()+"::"+source.getLabel(), target.getLabel());

                    }

                }
            }
        }
        System.out.println("\nGraph d'appel:\n"
                +g.afficherGraph());

        return g;
    }
	
	
    private void graphData(CompilationUnit cu) {

        boolean isMethodNodeAdded;
        ClassDeclarationVisitor visitorClass = new ClassDeclarationVisitor();
        cu.accept(visitorClass);

        for (TypeDeclaration nodeClass : visitorClass.getClasses()) {
            MethodDeclarationVisitor visitorMethod = new MethodDeclarationVisitor();
            nodeClass.accept(visitorMethod);

            Map<MethodDeclaration, Set<MethodInvocation>> mapMethodDeclarationInvocation = new HashMap<>();
            String caller;

            for (MethodDeclaration nodeMethod : visitorMethod.getMethods()) {
                nodeMethod.resolveBinding();
                MethodIvocationVisitor visitorMethodInvocation = new MethodIvocationVisitor();
                nodeMethod.accept(visitorMethodInvocation);
                mapMethodDeclarationInvocation.put(nodeMethod,  visitorMethodInvocation.getMethods());

                caller = nodeClass.getName().toString()+"::"+nodeMethod.getName();

                isMethodNodeAdded = false;

                for (MethodInvocation methodInvocation : visitorMethodInvocation.getMethods()) {

                    String callee;

                    if (methodInvocation.getExpression() != null) {
                        if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                            if (!isMethodNodeAdded) {
                                graphJGraphT.addVertex(caller);
                                isMethodNodeAdded = true;
                            }
                            callee = methodInvocation.getExpression().resolveTypeBinding().getName()+"::"+methodInvocation.getName();
                            graphJGraphT.addVertex(callee);
                            graphJGraphT.addEdge(caller, callee);

                            setLink.add("\t\""+caller+"\"->\""+callee+"\"\n");
                        }
                    }
                    else if (methodInvocation.resolveMethodBinding() != null) {
                        if (!isMethodNodeAdded) {
                            graphJGraphT.addVertex(caller);
                            isMethodNodeAdded = true;
                        }
                        callee = methodInvocation.resolveMethodBinding().getDeclaringClass().getName()+"::"+methodInvocation.getName();
                        graphJGraphT.addVertex(callee);
                        graphJGraphT.addEdge(caller, callee);

                        setLink.add("\t\""+caller+"\"->\""+callee+"\"\n");
                    }
                    else {
                        if (!isMethodNodeAdded) {
                            graphJGraphT.addVertex(caller);
                            isMethodNodeAdded = true;
                        }
                        callee = nodeClass.getName()+"::"+methodInvocation.getName();
                        graphJGraphT.addVertex(callee);
                        graphJGraphT.addEdge(caller, callee);

                        setLink.add("\t\""+caller+"\"->\""+callee+"\"\n");
                    }
                }
            }
            mapTheCallGraph.put(nodeClass, mapMethodDeclarationInvocation);
        }
    


 

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
	 