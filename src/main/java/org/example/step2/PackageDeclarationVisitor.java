
package org.example.step2;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageDeclarationVisitor extends ASTVisitor {
    Set<PackageDeclaration> packages = new HashSet<PackageDeclaration>();
    int nbPackage=0;
    ArrayList<CompilationUnit> project;

    private List<String> packagesNames = new ArrayList<>();




    private int nbPackages;

    /*public boolean visit(PackageDeclaration node) {
        nbPackage++;
        packages.add(node);
        for (PackageDeclaration pack:packages){
           // if(pack.getNodeType(PackageDeclaration)){}
            nbPackage++;
        }
        return super.visit(node);

    }*/



    @Override
    public boolean visit(PackageDeclaration node) {
        packages.add(node);
        nbPackages++;
        return super.visit(node);
    }
    public int getNbPackages(){
        return nbPackages;
    }
    public void setNbPackages(int nbPackages) {
        this.nbPackages = nbPackages;
    }
    /*@Override
    public boolean visit(PackageDeclaration packageNode) {
        if (!packagesNames.contains(packageNode.getName().toString())) {

            packagesNames.add(packageNode.getName().toString());
        }
        return true;
    }*/

    public Set<PackageDeclaration> getPackages(){
        return packages;
    }
}
