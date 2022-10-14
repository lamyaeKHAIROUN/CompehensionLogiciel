
package org.example.step2.visitor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.HashSet;
import java.util.Set;

public class PackageDeclarationVisitor extends Visitor {
    private Set<String> packages = new HashSet<String>();


    private  CompilationUnit compilationUnit;

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public static void setCompilationUnit(CompilationUnit compilationUnit) {
        compilationUnit = compilationUnit;
    }

    public int getNbPackages() {
        return nbPackages;
    }

    public void setNbPackages(int nbPackages) {
        this.nbPackages = nbPackages;
    }


    private int nbPackages;

   /* @Override
    public boolean visit(PackageDeclaration node) {
        packages.add(node.getName().toString());
        //System.out.println("nm package: " + packages);
        return super.visit(node);
    }*/
   @Override
   public boolean visit(PackageDeclaration packageNode) {
       if (!packages.contains(packageNode.getName().toString())) {
           nbPackages++;
           packages.add(packageNode.getName().toString());
       }
       return true;
   }


    public Set<String> getPackages(){
        return packages;
    }
}