package org.graphstream.algorithm;

import org.graphstream.algorithm.Prim;
import static org.graphstream.algorithm.ToolkitPa.*;
import static pa.pacman.Config.laby;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;

@SuppressWarnings("unused")
public class LabyGenerator implements Algorithm
{
	
	// ---------------Data
	protected Graph	graph;
	// ---------------Setup Parameters
	protected String	weightAttribute	= "weight";
	protected double	removeDeadEnd		= 0;
	protected boolean	pruning				= false;
	private boolean	Remove4Cycle		= false;
	
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
	
	// http://perso.ens-lyon.fr/eric.thierry/Graphes2009/nicolas-brunie.pdf
	public void setRemove4Cycle(boolean arg0)
	{
		Remove4Cycle = arg0;
	}
	
	// ----------------Method
	public void init(Graph arg0)
	{
		graph = arg0;
	}
	
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
						if (getInDegreeWhereEdgeIs(node2, "mur", "false") == 1)
						{
							node.getEdgeBetween(node2).addAttribute("mur", "false");
							find = true;
							break Boucle;
							
						}
					}
					
					HashSet<Integer> intset = new HashSet<Integer>();
					while (!find && intset.size() < node.getInDegree())
					{
						
						int alea = (int) Math.floor(Math.random() * node.getDegree());
						if (!intset.contains(alea))
						{
							intset.add(alea);
							if (node.getEdge(alea).getAttribute("mur") == "true")
							{
								find = true;
								node.getEdge(alea).addAttribute("mur", "false");
							}
						}
					}
				}
			}
		}// end if deadend
		if (Remove4Cycle)
		{
			
			boolean is4Cycle = true;
			while (is4Cycle)
			{
				is4Cycle = false;
				for (Node node : graph.getEachNode())
				{
					if (getInDegreeWhereEdgeIs(node, "mur", "false") == 2)
					{
						ArrayList<Node> truc = null;
						int i = 0;
						ArrayList<Node> truc2 = getNeighbourWhereEdge(node, "mur", "false");
						for (Node node2 : truc2)
						{
							if (truc == null)
							{
								truc = getNeighbourWhereEdge(node2, "mur", "false");
								truc.remove(node);
							} else
								truc.retainAll(getNeighbourWhereEdge(node2, "mur", "false"));
						}
						if (!truc.isEmpty())
						{
							is4Cycle = true;
							truc.add(node);
							truc.addAll(truc2);
							ArrayList<Edge> edgetruc = new ArrayList<Edge>();
							// a revoir.
							for (int k = 0; k < truc.size(); k++)
							{
								for (int j = k + 1; j < truc.size(); j++)
								{
									if (truc.get(k).hasEdgeBetween(truc.get(j)))
										edgetruc.add(truc.get(k).getEdgeBetween(truc.get(j)));
								}
							}
							boolean find = false;
							HashSet<Integer> intset = new HashSet<Integer>();
							while (!find)
							{
								intset.clear();
								while (!find && intset.size() < edgetruc.size())
								{
									int alea = (int) Math.floor(Math.random() * edgetruc.size());
									if (!intset.contains(alea))
									{
										intset.add(alea);
										edgetruc.get(alea).addAttribute("mur", "true");
										find = true;
										Boucle1: for (Node node1 : truc)
										{
											if (getInDegreeWhereEdgeIs(node1, "mur", "false") <= 1)
											{
												find = false;
												edgetruc.get(alea).addAttribute("mur", "false");
												break Boucle1;
												
											}
										}
										
									}
								}
								if (!find)
								{
									intset.clear();
									
									boolean add = false;
									while (!add && intset.size() < truc.size())
									{
										int alea = (int) Math.floor(Math.random() * truc.size());
										
										if (!intset.contains(alea))
										{
											intset.add(alea);
											if (getInDegreeWhereEdgeIs(truc.get(alea), "mur", "false") == 2 && truc.get(alea).getInDegree() > 2)
											{
												ArrayList<Edge> machin = getEachEdgeWhere(truc.get(alea), "mur", "true");
												int alea2 = (int) Math.floor(Math.random() * machin.size());
												machin.get(alea2).addAttribute("mur", "false");
												add = true;
											}
										}
									}
								}// end if !find
							}
						}
					}
				}
			}// end while 4-cycle
		}// end 4-cycle
			// reductible loop detection ou bottleneck
			// http://www.cs.ucr.edu/~gupta/teaching/201-09/My2.pdf
		if (true)
		{
			Graph h = subGraph(graph, "mur", "false");
			Graph r = subGraph(graph, "mur", "false");
			int d = h.getEdgeCount();
			
			for (Node node : h)
			{
				int i = node.getInDegree();
				if (i == 2)
				{
					ArrayList<Node> temp = getNeighbourList(node);
					if (!temp.get(0).hasEdgeBetween(temp.get(1)))
						h.addEdge(Integer.toString(d), temp.get(0), temp.get(1));
					h.removeEdge(temp.get(0), node);
					h.removeEdge(temp.get(1), node);
					
					d++;
				}
				
			}
			
			HashSet<ArrayList<Node>> nodelist = new HashSet<ArrayList<Node>>();
			for (Node node : h)
			{
				if (node.getInDegree() == 0)
					node.addAttribute("ui.style", "size:0px;");
				if (node.getInDegree() == 1)
				{
					node.addAttribute("ui.style", "fill-color:red;size:10px;");
					Node node2 = node.getEdge(0).getOpposite(node);
					ArrayList<Node> list = new ArrayList<Node>();
					list.add(node2);
					list.add(node);
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
				
				
				for (int i=1;i<boucle.size();i++)
				{  Node node=boucle.get(i);
					node.addAttribute("ui.style", "fill-color:red;size:10px;");
					ArrayList<Node> voisins = getNeighbourWhereEdge(node, "mur","false");
					for(Node voisin:voisins)
					{	if(!boucle.contains(voisin))
							boucle.add(voisin);
					}
					
				}
				boucle.remove(0);
				
				for(Node node:boucle)
				{
					//rajouter l'arrete qui va bien
				}
				
			}
			
			final String stylesheet = "node {fill-color:blue;size:5px;}";
			
			r.addAttribute("ui.stylesheet", stylesheet);
			h.addAttribute("ui.stylesheet", stylesheet);
			for (Node node : h)
			{
				
				// if(node.getInDegree()==0)
				// node.addAttribute("ui.style","size:0px;");
				// else
				// node.addAttribute("ui.label",node.getInDegree());
				if (node.getInDegree() == 1)
					node.addAttribute("ui.style", "fill-color:red;size:10px;");
			}
			
			h.display(false);
		}
		if (pruning)
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
