package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Visitor extends ASTVisitor {
    private CompilationUnit cu;



    public CompilationUnit getCu() {
        return cu;
    }

    public static void setCu(CompilationUnit cu) {
       cu = cu;
    }

    public Visitor() {
    }
}