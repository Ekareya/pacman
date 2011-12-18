package org.graphstream.algorithm;

import org.graphstream.algorithm.Prim;
import static org.graphstream.algorithm.ToolkitPa.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;

@SuppressWarnings("unused")
public class LabyGenerator implements Algorithm
{
	
	// ---------------Data
	protected Graph	graph;
	protected Graph	subway;
	// ---------------Setup Parameters
	protected String	weightAttribute	= "weight";
	protected double	removeDeadEnd		= 0;
	protected boolean	pruning				= false;
	private boolean	remove4Cycle		= false;
	private boolean	removeBottleNeck	= false;
	
	// ---------------Setup Method
	public void setWeighAttribute(String arg0)
	{
		weightAttribute = arg0;
	}
	
	public void setRemoveDeadEnd(double arg0)
	{
		removeDeadEnd = (arg0 <= 0) ? 0 : (arg0 < 1) ? arg0 : 1;
	}
	
	public void setPruning(boolean arg0)
	{
		pruning = arg0;
	}
	
	public void setRemove4Cycle(boolean arg0)
	{
		remove4Cycle = arg0;
	}
	
	public void setRemoveBottleNeck(boolean arg0)
	{
		removeBottleNeck = arg0;
	}
	
	// ----------------Get Methode
	
	public Graph getWay()
	{
		return subway;
	}
	
	// ----------------Method
	
	public void init(Graph arg0)
	{
		graph = arg0;
		subway = null;
	}
	
	/**
	 * creer un arbre couvrant de poid minimum a l'aide de l'algorithme de prim (
	 * fonctionne pour n'importe quel graphe connexe)
	 * enleve les culs de sac avec une proba de removeDeadEnd
	 * puis s'occupe des cycles et enleve la plupart des noeuds maitres ( ceux
	 * possedant bcp de cycle d'esclave ne sont detecté que si on fait tourner
	 * l'algo plusieur fois)
	 * la detection des 4-cycles utilise une heuristique pour un graphe grille et
	 * possedant peu de 4-cycle collé les un aux autres et ne doit pas
	 * fonctionner lorsque des 3-cycles sont present
	 * l'heuristique demandant d'avoir exactement 2 arretes mur ce qui ne se
	 * produit pas quand le 4cycle est immediatement entouré d'autre 4 cycle.ou
	 * dans une configuration en etoile cependant
	 * ses phenomenes sont assez rare du fait que l'on part d'un arbre couvrant
	 * (donc acyclique)
	 * heuristique des 4 cycles: prendre un sommet de degré 2, regarder la
	 * reunions des voisins de ses voisins, si il y a un autre sommet que lui
	 * même c'est que l'on a un cycle.
	 * ensuite il suffit d'enlever une des arretes du cycles, puis verifier si il
	 * y a creation de cul de sac, refuser selon la proba de removeDeadEnd,
	 * si on n'a as put enlever une arrete rajouter une arrete a partir du sommet
	 * selectionner vers l'exterieur et recommencer
	 * ensuite on chercher les noeuds esclaves, l'heuristique utilisé ne
	 * fonctionne pas avec des cul de sac d'ou la condition de removeDeadEnd a 1
	 * de plus si on accepte de laisser des cul de sac dans le labyrinthe, un cul
	 * de sac cyclique n'est pas très génant.
	 * l'heuristique utiliser exploite encore le fait que l'on soit partit d'un
	 * arbre couvrant donc avec bcp de node de degré 2
	 * l'heuristique simplifie le graphe en enlevant tout les sommet de degre2
	 * tout en gardant les connections. donc les cul de sac cyclique se
	 * retrouveront sous la forme de cul de sac,
	 * le noeud maitre etant le noeud directement relié au noeud resté du cyle
	 * une fois le noeud maitre et un noeud du cycle obtenus il est facile de
	 * recuperer tout les noeuds du cycle. et il suffit de rajouter une arretes
	 * depuis un somment du cycle
	 * vers un sommet non compris dans le cycle (et non compris dans les noeuds
	 * qui relie le maitre au cycle.
	 * 
	 * le labyrinthe obtenus avec l'algorithme de prim donne un très bon
	 * labyrinthe cependant j'avais besoin d'un peu plus de modularité pour
	 * adapter le labyrinthe
	 * au td d'I.A.(pacman) et j'avais besoin de rendre le labyrinthe jouable.
	 * donc pas de noeud maitre et pas de 4-cycles ( car moche )
	 * de plus le graphe utiliser pour detecter les noeuds maitre me seras utile
	 * pour bouger les fantomes ( grapheplus petit que le graphe complet donc
	 * calcul plus facile)
	 * 
	 * 
	 * 
	 */
	public void compute()
	{
		
		Prim prim = new Prim(weightAttribute, "mur", "false", "true");
		prim.init(graph);
		prim.compute();
		
		if (removeDeadEnd != 0)
		{
			for (Node node : graph.getEachNode())
			{
				if (getInDegreeWhereEdgeIs(node, "mur", "false") == 1 && Math.random() <= removeDeadEnd)
				{
					boolean find = false;
					Boucle: for (Node node2 : getNeighbourSet(node))
					{
						
						if (getInDegreeWhereEdgeIs(node2, "mur", "false") == 1)// selectionne
																									// en
																									// priorité
																									// les
																									// voisins
																									// cul
																									// de
																									// sac
						{
							node.getEdgeBetween(node2).addAttribute("mur", "false");
							find = true;
							break Boucle;
							
						}
					}
					// j'avais implémenté la recherche aleatoire de sommet a l'aide
					// d'un hashset qui se remplissait avec la valeur aleatoire,
					// cependant
					// il etait parfois assez long de parcourir tout les sommets si
					// un chiffre voulait pas sortir a l'alea. du coup j'ai fait
					// l'inverse j'ai supprimer la valeur obtenus
					// et donc ca me permet d'être sur que chaque sommet seras
					// visiter une seule fois
					ArrayList<Integer> intset = new ArrayList<Integer>();
					if (!find)
					{
						for (int i = 0; i < node.getInDegree(); i++)
							intset.add(i);
					}
					// pas trouver de voisins cul de sac donc on ajoute un mur
					// aleatoirement
					while (!find && intset.size() > 0)// tq on n'a pas visiter tout
																	// les arretes
					{
						
						int alea = (int) Math.floor(Math.random() * intset.size());
						int num = intset.get(alea);
						intset.remove(alea);
						if (node.getEdge(num).getAttribute("mur") == "true")
						{
							find = true;
							node.getEdge(num).addAttribute("mur", "false");
						}
					}
				}
			}
		}// end if deadend
		if (remove4Cycle)
		{
			
			boolean is4Cycle = true;
			while (is4Cycle)
			{
				is4Cycle = false;
				for (Node node : graph.getEachNode())
				{
					if (getInDegreeWhereEdgeIs(node, "mur", "false") == 2)// heuristique
																							// :a par
																							// dans de
																							// très
																							// rare cas
																							// un
																							// 4-cycle
																							// dans une
																							// grille
																							// possede
																							// au moins
																							// un
																							// sommet
																							// d'ordre
																							// 2
					{
						ArrayList<Node> nodeCycle = null;
						ArrayList<Node> voisin = getNeighbourWhereEdge(node, "mur", "false");
						for (Node node2 : voisin)// pour tout les voisins connecté on
															// recupère leur voisins et on fait
															// la reunion
						{
							if (nodeCycle == null)
							{
								nodeCycle = getNeighbourWhereEdge(node2, "mur", "false");
								nodeCycle.remove(node);
							} else
								nodeCycle.retainAll(getNeighbourWhereEdge(node2, "mur", "false"));
						}
						
						if (!nodeCycle.isEmpty())// i.e on a detecté un 4-cycle
						{
							is4Cycle = true;
							nodeCycle.add(node);
							nodeCycle.addAll(voisin);// on recupère tout les sommets
							ArrayList<Edge> edgeCycle = new ArrayList<Edge>();// puis
																								// les
																								// arretes
							for (int k = 0; k < nodeCycle.size(); k++)
							{
								for (int j = k + 1; j < nodeCycle.size(); j++)
								{
									if (nodeCycle.get(k).hasEdgeBetween(nodeCycle.get(j)))
										edgeCycle.add(nodeCycle.get(k).getEdgeBetween(nodeCycle.get(j)));
								}
							}
							boolean find = false;
							ArrayList<Integer> intset = new ArrayList<Integer>();
							
							for (int i = 0; i < edgeCycle.size(); i++)
								intset.add(i);
							while (!find && intset.size() > 0)// on test toutes les
																			// arretes pour voir si
																			// on peu les supprimer
																			// sans faire de cul de
																			// sac
							{
								int alea = (int) Math.floor(Math.random() * intset.size());
								int num = intset.get(alea);
								intset.remove(alea);
								edgeCycle.get(num).addAttribute("mur", "true");
								find = true;
								Boucle1: for (Node node1 : nodeCycle)
								{
									if (getInDegreeWhereEdgeIs(node1, "mur", "false") <= 1)
									{
										find = false;
										// rajouter la proba
										edgeCycle.get(num).addAttribute("mur", "false");
										break Boucle1;
										
									}
								}
							}
							if (!find)// on n'a pas reussit a enlever le 4-cycle donc
											// on va rajouter une arretes vers l'exterieur
											// pour recommencer
							{
								for (int i = 0; i < nodeCycle.size(); i++)
									intset.add(i);
								
								boolean add = false;
								while (!add && intset.size() > 0)
								{
									int alea = (int) Math.floor(Math.random() * intset.size());
									int num = intset.get(alea);
									intset.remove(alea);
									if (getInDegreeWhereEdgeIs(nodeCycle.get(num), "mur", "false") == 2 && nodeCycle.get(num).getInDegree() > 2)
									{
										ArrayList<Edge> machin = getEachEdgeWhere(nodeCycle.get(num), "mur", "true");
										int alea2 = (int) Math.floor(Math.random() * machin.size());
										machin.get(alea2).addAttribute("mur", "false");
										add = true;
									}
								}
							}// end if !find
						}
						
					}
				}
				
				// reductible loop detection ou bottleneck
				// http://www.cs.ucr.edu/~gupta/teaching/201-09/My2.pdf
				if (removeBottleNeck && removeDeadEnd == 1 && !is4Cycle)
				// ne gere pas les cul de sacs et rajoute des 4 cycles.
				{
					subway = subGraph(graph, "mur", "false");
					int d = subway.getEdgeCount();
					
					for (Node node : subway)// on simplifie le graphe rajouter les poids.
					{
						int i = node.getInDegree();
						if (i == 2)
						{
							ArrayList<Node> temp = getNeighbourList(node);
							if (!temp.get(0).hasEdgeBetween(temp.get(1)))
								subway.addEdge(Integer.toString(d), temp.get(0), temp.get(1));
							subway.removeEdge(temp.get(0), node);
							subway.removeEdge(temp.get(1), node);
							
							d++;
						}
						
					}
					
					HashSet<ArrayList<Node>> nodelist = new HashSet<ArrayList<Node>>();// liste
																												// des
																												// couples
																												// maitre
																												// esclave
																												// detecté
					for (Node node : subway)
					{
						if (node.getInDegree() == 1)// presence d'un noeud maitre, on
																// le traite et on reteste pour
																// savoir si l'on a pas creer un
																// 4-cycle
						{
							is4Cycle = true;
							Node node2 = node.getEdge(0).getOpposite(node);
							ArrayList<Node> list = new ArrayList<Node>();
							list.add(node2);// node maitre
							list.add(node);// node esclave
							nodelist.add(list);
							// h.removeEdge(node,node2);
						}
					}
					
					for (ArrayList<Node> nodes : nodelist)
					{
						Node start = graph.getNode(nodes.get(1).getId());
						Node finish = graph.getNode(nodes.get(0).getId());
						
						ArrayList<Node> boucle = new ArrayList<Node>();
						
						boucle.add(finish);
						boucle.add(start);
						
						for (int i = 1; i < boucle.size(); i++)// parcours en largeur
																			// depuis l'esclave
																			// vers le maitre pour
																			// recuperer tout les
																			// esclaves
						{
							Node node = boucle.get(i);
							ArrayList<Node> voisins = getNeighbourWhereEdge(node, "mur", "false");
							for (Node voisin : voisins)
							{
								if (!boucle.contains(voisin))
									boucle.add(voisin);
							}
							
						}
						boucle.remove(0);
						Node lastnode = finish;
						HashSet<Node> ficelle = new HashSet<Node>();
						boolean ok = false;
						do// separe la "boucle" de la "ficelle"
						{
							int i = 0;
							ArrayList<Node> voisins = getNeighbourWhereEdge(lastnode, "mur", "false");
							Node next = null;
							for (Node voisin : voisins)
							{
								if (boucle.contains(voisin))
								{
									i++;
									next = voisin;
								}
							}
							if (i < 2)
							{
								ficelle.add(lastnode);
								boucle.remove(lastnode);
								lastnode = next;
							} else
							{
								ok = true;
							}
							
						} while (!ok);
						
						ArrayList<Integer> intset = new ArrayList<Integer>();
						for (int i = 0; i < boucle.size(); i++)
							intset.add(i);
						boolean find = false;
						while (!find && intset.size() > 0)// rajoute une arrete depuis
																		// la boucle vers le reste
																		// du graphe
						{
							int alea = (int) Math.floor(Math.random() * intset.size());
							int num = intset.get(alea);
							intset.remove(alea);
							Node nodetest = boucle.get(num);
							ArrayList<Node> voisins = getNeighbourWhereEdge(nodetest, "mur", "true");
							for (Node node : voisins)
							{
								if (!boucle.contains(node) && !ficelle.contains(node))
								{
									find = true;
									nodetest.getEdgeBetween(node).addAttribute("mur", "false");
								}
							}
							
						}
						
					}
				}
			}// end while 4-cycle
		}// end 4-cycle
		if (pruning)// elague le graphe des murs. permet d'utiliser l'algo de
						// dijsktra de la biblio de graphstream
		{
			for (int i = 0; i < graph.getEdgeCount(); i++)
			{
				Edge edge = graph.getEdge(i);
				
				if (edge.getAttribute("mur") == "true")
				{
					graph.removeEdge(edge);
					i--;
				}
				
			}
			
		}
		
	}// end compute
}
