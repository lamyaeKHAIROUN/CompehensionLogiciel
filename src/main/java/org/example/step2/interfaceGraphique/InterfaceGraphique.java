package org.example.step2.interfaceGraphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.example.step2.processor.Processor;

public class InterfaceGraphique extends JFrame implements ActionListener {

	private JFrame maFenetre = new JFrame();
	private JPanel conteneur = new JPanel(new GridBagLayout());
	private GridBagConstraints gbc = new GridBagConstraints();
	private Processor processeur;
	private JLabel texte = new JLabel(
			"<html> <font color=red> <h1><strong> Bienvenu dans  l'application <br> de l'analyse statique </strong></h1><hr>   </font> </html>",
			JLabel.CENTER);
	private JButton bouton1 = new JButton("Nombre de classe de l'application");
	private JButton bouton2 = new JButton("Nombre de lignes de code de l'application");
	private JButton bouton3 = new JButton("Nombre total de méthodes de l'application");
	private JButton bouton4 = new JButton("Nombre total de packages de l'application");
	private JButton bouton5 = new JButton("Nombre moyen de méthodes par classe");
	private JButton bouton6 = new JButton("Nombre moyen de ligne par méthode");
	private JButton bouton7 = new JButton("Nombre moyen d'attributs par classe");

	private JButton bouton8 = new JButton("les 10% des classes vc plus grand nbr de méthode");
	private JButton bouton9 = new JButton(" les 10% des classes vc plus grand nbr d'att");
	private JButton bouton10 = new JButton("Les 10% des classes ayant le plus grand nombre de méthode et attr ");
	private JButton bouton11 = new JButton("Les classes ayant un nombre de méthodes supérieur à X ");
	
	private JButton bouton12 = new JButton("Les 10% des méthodes avec le plus grand nbr de lignes par classe");
	private JButton bouton13 = new JButton("Le nombre maximal de param par rapport à tous les meth de l'appli");
	private JButton bouton14 = new JButton("Le graphe d'appel");

	public void displayInterface(Processor processeur) {
		this.processeur = processeur;
		this.setVisible(true);
		setTitle("Analyse statique");
		setSize(1000,500);
		setResizable(false);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		conteneur.setBackground(Color.white);
		bouton1.setPreferredSize(null);
		maFenetre.add(texte);
		conteneur.add(texte, gbc);
		conteneur.add(bouton1, gbc);
		conteneur.add(bouton2, gbc);
		conteneur.add(bouton3, gbc);
		conteneur.add(bouton4, gbc);
		conteneur.add(bouton5, gbc);
		conteneur.add(bouton6, gbc);
		conteneur.add(bouton7, gbc);
		conteneur.add(bouton8, gbc);
		conteneur.add(bouton9, gbc);
		conteneur.add(bouton10, gbc);
		conteneur.add(bouton11, gbc);
		conteneur.add(bouton12,gbc);
		conteneur.add(bouton13, gbc);
		conteneur.add(bouton14, gbc);


		this.bouton1.addActionListener(this);
		this.bouton2.addActionListener(this);
		this.bouton3.addActionListener(this);
		this.bouton4.addActionListener(this);
		this.bouton5.addActionListener(this);
		this.bouton6.addActionListener(this);
		this.bouton7.addActionListener(this);
		this.bouton8.addActionListener(this);
		this.bouton9.addActionListener(this);
		this.bouton10.addActionListener(this);
		this.bouton11.addActionListener(this);
		this.bouton12.addActionListener(this);
		this.bouton13.addActionListener(this);
		this.bouton14.addActionListener(this);

		setContentPane(conteneur);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String affichage;
		try {
			
		if (arg0.getSource().equals(bouton1)) {
			
				affichage = processeur.btn1();
	        	JOptionPane.showMessageDialog(conteneur, affichage, "Nombre de classe ", JOptionPane.CLOSED_OPTION);

			 
		}else if (arg0.getSource().equals(bouton2)) {
			affichage = processeur.btn2();
			JOptionPane.showMessageDialog(conteneur, affichage, "Nombre de lignes de tous l'appli", JOptionPane.CLOSED_OPTION);
		} else if (arg0.getSource().equals(bouton3)) {
			affichage = processeur.btn3();
			JOptionPane.showMessageDialog(conteneur, affichage, "Nombre total de méthodes", JOptionPane.CLOSED_OPTION);
		} else if (arg0.getSource().equals(bouton4)) {
			affichage = processeur.btn4();
			JOptionPane.showMessageDialog(conteneur, affichage, "Nombre total de package", JOptionPane.CLOSED_OPTION);
		} else if (arg0.getSource().equals(bouton5)) {
			affichage = processeur.btn5();
			JOptionPane.showMessageDialog(conteneur, affichage, "Nombre moyen de methodes par classe", JOptionPane.CLOSED_OPTION);
		} else if (arg0.getSource().equals(bouton6)) {
			affichage = processeur.btn6();
			JOptionPane.showMessageDialog(conteneur, affichage, "Nombre moyen de ligne par méthode", JOptionPane.CLOSED_OPTION);
		} else if (arg0.getSource().equals(bouton7)) {
			affichage = processeur.btn7();
			JOptionPane.showMessageDialog(conteneur, affichage, "Nombre moyen d'attr par classe", JOptionPane.CLOSED_OPTION);
		} else if (arg0.getSource().equals(bouton8)) {
			affichage = processeur.btn8();
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}else if (arg0.getSource().equals(bouton9)) {
			affichage = processeur.btn9();
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}else if (arg0.getSource().equals(bouton10)) {
			affichage = processeur.btn10();
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}else if (arg0.getSource().equals(bouton11)) {
			String val = JOptionPane.showInputDialog("Choisir une valeur pour X");
			int valInt = Integer.parseInt(val);
			affichage = processeur.btn11(valInt);
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}else if (arg0.getSource().equals(bouton12)) {
			affichage = processeur.btn12();
			UIManager.put("OptionPane.minimumSize",new Dimension(500,300)); 
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}else if (arg0.getSource().equals(bouton13)) {
			affichage = processeur.btn13();
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}
		else if (arg0.getSource().equals(bouton14)) {
			affichage = processeur.btn14();
			JOptionPane.showMessageDialog(conteneur, affichage, "Réponse", JOptionPane.CLOSED_OPTION);
		}

	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}