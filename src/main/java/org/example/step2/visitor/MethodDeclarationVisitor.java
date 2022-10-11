package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	int nbMethod=0;
	private String contentMethod;

	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		nbMethod++;
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
