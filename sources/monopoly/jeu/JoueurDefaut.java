package monopoly.jeu;

import monopoly.proprietes.Propriete;

import monopoly.evenements.Evenement;
import monopoly.evenements.TirerDes;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Observable;

import java.awt.Color;

import java.util.Random;



/**
  * Cette classe représente un joueur humain,
  * qui se contrôle manuellement donc ( entrée/sortie )
  */
public class JoueurDefaut extends Observable implements Joueur
{
    private int numero;         // Numero du joueur
    private int especes;        // Espèces possédées par le joueur
    private String nom;         // Nom du joueur
    private Case position;      // Case sur laquelle se situe le joueur
    private Color c;
    private boolean enPrison;   // Vrai si le joueur est en prison, faux sinon
    private boolean elimine;    // Vrai si le joueur est éliminé, faux sinon
    private List<Propriete> titres;         // Les titres de propriétés du joueur
    private List<Evenement> cartes;         // Les cartes conservées par le joueur
    private Stack<Evenement> chosesAFaire;  // Les actions que le joueur doit exécuter
    
    private static List<Joueur> joueurs = new ArrayList<Joueur>();  // La liste des joueurs, sauf soi-même et les éliminés
    
    
    
    /**
      * Crée un joueur en fonction d'un numéro d'identification,
      * de son nom, ainsi que de sa case de départ.
      */
    public JoueurDefaut(int numero, String nom, Case position)
    {
        this.numero     = numero;
        this.nom        = nom;
        this.position   = position;
        
        Random g = new Random();
        int red     =   (int)(g.nextFloat()*256),
            green   =   (int)(g.nextFloat()*256),
            blue    =   (int)(g.nextFloat()*256);
        c = new Color(red, green, blue);
        
        especes         = 20000;
        enPrison        = false;
        titres          = new ArrayList<Propriete>();
        cartes          = new ArrayList<Evenement>();
        chosesAFaire    = new Stack<Evenement>();
        
        joueurs.add(this);
    }
    
    
    
    public int numero()
    {
        return numero;
    }
    
    public String nom()
    {
        return nom;
    }
    
    public void joue()
    {
        chosesAFaire().add(new TirerDes(this));

        while ( ! chosesAFaire.empty() ) {
            Evenement e = chosesAFaire.pop();
            if( e!= null ) {
                e.cibler(this);
                    
                setChanged();
                notifyObservers(e.toString());
                
                e.executer();
            }
        }
        setChanged();
        notifyObservers("finTour");
    }
    
    public boolean enPrison()
    {
        return enPrison;
    }
    
    public void emprisonner()
    {
        enPrison = true;
    }
    
    public void liberer()
    {
        enPrison = false;
    }
    
    public boolean elimine()
    {
        return elimine;
    }
    
    public void eliminer()
    {
		joueurs.remove(this);
        elimine = true;
    }
    
    public int especes()
    {
        return especes;
    }
    
    public boolean payer(int somme)
    {
        if (especes > somme) {
            especes -= somme;
            return true;
        }

        return false;
    }
    
    public void verser(int somme)
    {
        especes += somme;
    }
    
    public Case position()
    {
        return position;
    }
    
    public Color couleur()
    {
        return c;
    }
    
    public void placerSur(Case c)
    {
        position = c;
        if (c.evenement() != null) {
            Evenement e = c.evenement();
            e.cibler(this);
            chosesAFaire.add(e);
        }
        setChanged();
        notifyObservers(null);
    }
    
    public List<Joueur> adversaires()
    {
		List<Joueur> adversaires = new ArrayList<Joueur>(joueurs);
		adversaires.remove(this);
		return adversaires;
    }
    
    public List<Propriete> titres()
    {
        return titres;
    }
    
    public List<Evenement> cartes()
    {
        return cartes;
    }
    
    public Stack<Evenement> chosesAFaire()
    {
        return chosesAFaire;
    }

    

    public boolean equals(Object o)
    {
        return (o == this || (o instanceof JoueurDefaut && numero == ((JoueurDefaut) o).numero()));
    }



    public String toString()
    {
        return nom + " (" + numero + ")";
    }
}
