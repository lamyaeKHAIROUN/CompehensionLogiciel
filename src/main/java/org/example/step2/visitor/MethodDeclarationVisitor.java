package org.example.step2.visitor;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.*;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();

	public Map<MethodDeclaration, Set<MethodInvocation>> getMethodInvocationMap() {
		return methodInvocationMap;
	}

	Map<MethodDeclaration,Set<MethodInvocation>> methodInvocationMap =new HashMap();
	int nbMethod=0;

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	private CompilationUnit cu;

	private String contentMethod;

	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		nbMethod++;
		MethodIvocationVisitor visitor2 = new MethodIvocationVisitor();
		node.accept(visitor2);
		Set<MethodInvocation> methodInvoc=new HashSet<>();
		for (MethodInvocation methodInvocation : visitor2.getMethods()) {
			methodInvoc.add(methodInvocation);
		}
		methodInvocationMap.put(node,methodInvoc);

		return super.visit(node);
	}

	public int getNbMethod() {
		return nbMethod;
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}

	public int numberOfLinesMethod(String s) {
		int rslt = 0;
		rslt+= countLinesMethoed(s);
		//return ("Le nombre de lignes de code de l’application est:"+rslt);
		return rslt;
	}
	public String getContentMethod(){
		return this.contentMethod;
	}

	public void setContentMethod(String contentMethod) {
		this.contentMethod = contentMethod;
	}

	public int countLinesMethoed(String str){
		String[] lines = str.split("\r\n|\r|\n");
		return  lines.length;
	}
}