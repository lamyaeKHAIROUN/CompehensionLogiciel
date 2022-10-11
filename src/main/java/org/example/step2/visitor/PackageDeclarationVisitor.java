
package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.HashSet;
import java.util.Set;

public class PackageDeclarationVisitor extends ASTVisitor {
    private Set<String> packages = new HashSet<String>();
    private int nbPackages;

    @Override
    public boolean visit(PackageDeclaration node) {
        packages.add(node.getName().toString());
        //System.out.println("nm package: " + packages);
        return super.visit(node);
    }

    public Set<String> getPackages(){
        return packages;
    }
}
