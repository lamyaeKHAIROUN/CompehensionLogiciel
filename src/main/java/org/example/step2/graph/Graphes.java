package org.example.step2.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Classe principale
public class Graphes {
    public static void main(String[] args) {
        // Insérer des exemples ici : construire un graphe, lui ajouter
        // des sommets et des arêtes, et contrôler les couleurs, les
        // composantes connexes, les accessibilités...
    }
}

// Un graphe est constitué de sommets et d'arêtes.
// On veut notamment :
// - Créer un graphe vide
// - Ajouter des sommets
// - Ajouter des arêtes
// - Effectuer un coloriage
class Graphe {
    // Ensembles de sommets et d'arêtes, directement représentés comme tels.
    private Set<Sommet> sommets;
    private Set<Arete> aretes;
    // Une composante connexe est représentée par un sommet principal
    // Idée : utiliser l'algorithme Union-Find de Tarjan (dans ce corrigé,
    // il y a au passage les optimisations "union par rangs" et "compression
    // de chemins".
    private Set<Sommet> composantes;
    // Le nombre de composantes pourrait être recalculé à la demande plutôt
    // qu'enregistré, mais ça ne coûte pas cher.
    private int nbComposantes;

    // Graphe vide : pas de sommets, pas d'arêtes, pas de composantes.
    public Graphe() {
        sommets = new HashSet<Sommet>();
        aretes = new HashSet<Arete>();
        composantes = new HashSet<Sommet>();
        nbComposantes = 0;
    }

    // Ajout d'un sommet de nom [n]
    public void ajouteSommet(String n) {
        Sommet s = new Sommet(this, n);
        sommets.add(s);
        // Un nouveau sommet crée une nouvelle composante
        composantes.add(s);
        nbComposantes++;
    }

    // Ajout d'une arête entre deux sommets [s1] et [s2]
    public void ajouteArete(Sommet s1, Sommet s2) {
        Arete a = new Arete(s1, s2);
        aretes.add(a);
        // On récupère les composantes des deux sommets reliés...
        Sommet sc1 = s1.composante();
        Sommet sc2 = s2.composante();
        // ... si elles étaient différentes, alors on les unit.
        if (sc1 != sc2) {
            nbComposantes--;
            composantes.remove(sc1);
            composantes.remove(sc2);
            composantes.add(sc1.union(sc2));
        }
    }


}

// Un sommet possède un nom et une couleur.
// On peut :
// - Créer un sommet
// - Lui ajouter des arêtes incidentes
// - Obtenir sa composante connexe
// - Lui donner une couleur
// Ici, les sommets représentent aussi des composantes connexes, on
// peut donc encore :
// - Unir la composante d'un sommet avec celle d'un autre sommet
class Sommet {
    private String nom;
    private Graphe graphe;
    // Le dominant est le père du sommet considéré dans la structure Union-Find
    private Sommet dominant;
    // Le rang est une borne sur la hauteur de la structure Union-Find
    private int rang=0;
    // Ensemble des arêtes incidentes
    private Set<Arete> incidences;
    // Les sommets accessibles sont représentés par une table associant
    // chaque sommet accessible à un chemin.
    // On veut que le chemin mémorisé soit le plus court.
    // On pourrait obtenir une représentation plus compacte en mémorisant
    // moins d'information : pour les besoins des méthodes du TP il suffirait
    // d'avoir la longueur du chemin et sa première arête.
    private Map<Sommet, Chemin> accessibles;

    public Sommet(Graphe g, String n) {
        graphe = g;
        nom = n;
        dominant = this;
        // Un sommet est créé sans arêtes incidentes.
        // Le seul sommet accessible est lui-même, via le chemin vide.
        incidences = new HashSet<Arete>();
    }

    // Ajout d'une arête incidente.
    // Pour mettre à jour l'ensemble des sommets accessible dans le graphe,
    // il faut prendre tous les sommets accessibles depuis le sommet actuel,
    // tous les sommets accessibles depuis le sommet cible, et comparer le
    // nouveau chemin créé avec le meilleur chemin qui existait déjà.
    public void ajouteArete(Arete a) {
        incidences.add(a);
        for(Map.Entry<Sommet, Chemin> accS1 : this.accessibles.entrySet()) {
            for(Map.Entry<Sommet, Chemin> accS2 :
                    a.autreExtremite(this).accessibles.entrySet()) {
                // À compléter...
                // On construit le nouveau chemin entre deux extrémités,
                // on le compare à l'éventuel chemin déjà existant, et on
                // garde le plus court.
            }
        }
    }

    // Donne le sommet représentant la composante connexe à laquelle
    // appartient [this].
    public Sommet composante() {
        if (dominant != this) {
            // Cette mise à jour correspond à l'optimisation
            // "compression de chemins".
            dominant = dominant.composante();
        }
        return dominant;
    }

    // Fait l'union de deux composantes connexes.
    // Pré-condition : [this] et [s] doivent être chacun le représentant
    // de sa composante connexe (c'est-à-dire la racine de l'arbre
    // correspondant dans la structure Union-Find), sans quoi la structure
    // de données serait corrompue.
    // Remarque : pas forcément malin de donner une pré-condition si critique
    // à une méthode qui n'est pas privée. Il serait plus raisonnable de
    // d'abord appliquer la méthode [composante()].
    protected Sommet union(Sommet s) {
        if (this.rang > s.rang) {
            s.dominant = this; return this;
        } else {
            this.dominant = s;
            if (this.rang == s.rang) s.rang++;
            return s;
        }
    }

}

// Une arête a deux extrémités (ordre arbitraire, on regarde des graphes
// non orientés).
// On peut :
// - Savoir si un sommet est une extrémité
// - Obtenir l'autre extrémité si on en a déjà une
// - Obtenir le poids de l'arête (par défaut, 1)
class Arete {
    // On représente les extrémités par un tableau à deux cases
    // Comme souvent ici : c'est une possibilité parmi d'autres
    private Sommet[] extremites;

    public Arete(Sommet s1, Sommet s2) {
        // On range les extrémité dans l'ordre donné par les paramètres
        // Variante : on les classe en fonction d'un ordre (pas utile ici)
        extremites = new Sommet[2];
        extremites[0] = s1;
        extremites[1] = s2;
        // On inscrit l'arête dans les listes d'incidence de ses extrémités
        s1.ajouteArete(this);
        s2.ajouteArete(this);
    }

    // Donne l'autre extrémité
    // Pré-condition : [s] est l'une des extrémités
    public Sommet autreExtremite(Sommet s) {
        if (s == extremites[0]) return extremites[1];
        else return extremites[0];
    }

    // Vérifie qu'un sommet est une extrémité
    public boolean admetExtremite(Sommet s) {
        return (s == extremites[0] || s == extremites[1]);
    }

    // Pour une arête non pondérée, le poids est 1
    public int getPoids() { return 1; }
}

// Une arête pondérée est une arête avec en plus un poids.
class AretePonderee extends Arete {
    private int poids;

    public AretePonderee(Sommet s1, Sommet s2, int p) {
        super(s1, s2);
        poids = p;
    }

    public int getPoids() { return poids; }
}


// Un chemin est un ensemble d'arêtes entre une source et une destination
// On peut :
// - Créer un chemin vide
// - Obtenir la longueur du chemin
// - Ajouter une arête compatible à la fin du chemin
// - Concaténer un chemin à la fin du chemin
// - Obtenir le chemin inverse
class Chemin {
    protected ArrayList<Arete> aretes;
    private Sommet source;
    private Sommet destination;

    // Le chemin vide est quand même défini au niveau d'un sommet
    public Chemin(Sommet s) {
        aretes = new ArrayList<Arete>();
        source = s;
        destination = s;
    }

    // La longueur est la somme des poids des chemins
    public int longueur() {
        int l = 0;
        for(Arete a : aretes) {
            l += a.getPoids();
        }
        return l;
    }

    // Ajout d'une arête à la fin du chemin
    // - renvoie vraie si l'arête est compatible
    // - renvoie faux (et ne fait rien) sinon
    public boolean ajouteArete(Arete a) {
        if (a.admetExtremite(destination)) {
            aretes.add(a);
            destination = a.autreExtremite(destination);
            return true;
        }
        return false;
    }

    // Concaténation d'un chemin à la fin du chemin, renvoie un booléen
    // comme l'ajout d'arête
    public boolean concatene(Chemin c) {
        if (c.source != this.destination) return false;
        for (Arete a : aretes) {
            this.ajouteArete(a);
        }
        return true;
    }

    // Construit un (nouveau) chemin renversé
    public Chemin rev() {
        Chemin c = new Chemin(destination);
        for (int i=aretes.size()-1; i>=0; i--) {
            c.ajouteArete(aretes.get(i));
        }
        return c;
    }

}
