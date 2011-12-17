package org.graphstream.algorithm;

import org.graphstream.algorithm.Prim;
import static org.graphstream.algorithm.ToolkitPa.*;
import static pa.pacman.Config.laby;

import org.graphstream.graph.*;

import java.util.*;

@SuppressWarnings("unused")
public class LabyGenerator implements Algorithm
{
	
	// ---------------Data
	protected Graph	theGraph;
	// ---------------Setup
	protected String	weightAttribute	= "weight";
	protected double	removeDeadEnd		= 0;
	protected boolean	pruning				= false;
	private boolean	Remove4Cycle		= false;
	
	// ---------------Setter
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
		Remove4Cycle = arg0;
		
	}
	
	// ----------------Method
	public void init(Graph arg0)
	{
		theGraph = arg0;
	}
	
	public void compute()
	{
		
//		for(Edge edge:theGraph.getEachEdge())
//		{
//			edge.addAttribute("mur",false);
//		}
		Prim prim = new Prim(weightAttribute, "mur", false, true);
		prim.init(theGraph);
		prim.compute();
		if (removeDeadEnd != 0)
		{
			for (Node node : theGraph.getEachNode())
			{
				if (getInDegreeWhereEdgeIs(node, "mur", false) == 1 && Math.random() <= removeDeadEnd)
				{
					boolean find = false;
					Boucle: for (Node node2 : getNeighbour(node))
					{
						if (getInDegreeWhereEdgeIs(node2, "mur", false) == 1)
						{
							node.getEdgeBetween(node2).addAttribute("mur", false);
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
							if (node.getEdge(alea).getAttribute("mur") == (Object) true)
							{
								find = true;
								node.getEdge(alea).addAttribute("mur", false);
							}
						}
					}
				}
			}
		}// end if deadend
		int o = 0;
		int p = 0;
		if (Remove4Cycle)
		{
			
			boolean is4Cycle = true;
			while (is4Cycle)
			{
				System.out.println(o);
				o++;
				is4Cycle = false;
				for (Node node : theGraph.getEachNode())
				{
					if (getInDegreeWhereEdgeIs(node, "mur", false) == 2)
					{
						ArrayList<Node> truc = null;
						int i = 0;
						ArrayList<Node> truc2 = getNeighbourWhereEdge(node, "mur", (Object) false);
						for (Node node2 : truc2)
						{
							if (truc == null)
							{
								truc = getNeighbourWhereEdge(node2, "mur", (Object) false);
								truc.remove(node);
							} else
								truc.retainAll(getNeighbourWhereEdge(node2, "mur", (Object) false));
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
							p = 0;
							while (!find)
							{
								System.out.println("-" + p);
								p++;
								int q = 0;
								while (!find && intset.size() < edgetruc.size())
								{
									System.out.println("--" + q);
									q++;
									int alea = (int) Math.floor(Math.random() * edgetruc.size());
									if (!intset.contains(alea))
									{
										intset.add(alea);
										edgetruc.get(alea).addAttribute("mur", true);
										find = true;
										Boucle1: for (Node node1 : truc)
										{
											if (getInDegreeWhereEdgeIs(node1, "mur", false) <= 1)
											{
												find = false;
												edgetruc.get(alea).addAttribute("mur", false);
												break Boucle1;
												
											}
										}
										
									}
								}
								if (!find)
								{
									intset.clear();
									
									boolean add = false;
									int m = 0;
									while (!add && intset.size() < truc.size())
									{
										int alea = (int) Math.floor(Math.random() * truc.size());
										
										if (!intset.contains(alea))
										{
											System.out.println("---" + m);
											m++;
											intset.add(alea);
											if (getInDegreeWhereEdgeIs(truc.get(alea), "mur", false) == 2)
											{
												ArrayList<Edge> machin = getEachEdgeWhere(truc.get(alea), "mur", true);
												machin.get((int) Math.floor(Math.random() * machin.size())).addAttribute("mur", false);
												add = true;
											}
										}
									}
								}// end if !find
							}
						}
					}
				}
			}// end 4-cycle
		}
	}
}
