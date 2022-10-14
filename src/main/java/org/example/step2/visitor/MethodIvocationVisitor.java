package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodIvocationVisitor extends ASTVisitor  {
	Set<MethodInvocation> methods = new HashSet<>();
	int nbrMethod=0;
	List<SuperMethodInvocation> superMethods = new ArrayList<SuperMethodInvocation>();
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		nbrMethod++;
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethods.add(node);
		return super.visit(node);
	}

	public int getNbrMethod(){
		return nbrMethod;
	}
	public Set<MethodInvocation> getMethods() {
		return methods;
	}
	
	public List<SuperMethodInvocation> getSuperMethod() {
		return superMethods;
	}
}

