package pa.pacman;

import org.graphstream.graph.*;

public final class Config
{
//option graphique
	public static final int	PAS	= 40;//nombre de pixel par case
	public static final int	MARGE	= 50;//marge autour du plateau de jeu, éviter d'avoir MARGE<PAS
//option du plateau de jeu
	public static final int	H		= 15;//nombre de case verticale
	public static final int W		= 10;//nombre de case horizontale
	public static final int TUNNELHORI = 0;//nombre max de tunnel horizontal
	public static final int TUNNELVERT = 0;//nombre max de tunnel vertical
	public static final int SUPERGOMME = 0;//nombre de super gomme
//option du jeu
	public static final boolean IMMORTALITY = false;//tricheur :p
	public static final int trackingDistance=15;//champ de vision des monstre
	public static final int deadTime=20;//temps durant lequel les monstres restent mort
	public static final int PACMANSPEED=18;//vitesse de pacman en nombre d'apelle de la fonction draw()
	public static final int MONSTERSPEED=24;//vitesse des monstre ....
	public static final int MONSTERCOST=100;//configure le comportement des monstres. plus le nombre est bas et plus ils auront tendance à se suivre./!\ ne pas mettre en dessous de 1
	public static int life=3;//nombre de vie de pacman

//variable globale
	public static Graph laby = null;//le graphe du labyrinthe
	public static Graph smallLaby=null;//le graphe simplifié du labyrinthe (utilisation non implementé)
	public static Node pacNode=null;//le noeud ou se trouve pacman actuellement
	public static int hunted = 0;//compteur pour les supergommes
	public static int score =0;
}
