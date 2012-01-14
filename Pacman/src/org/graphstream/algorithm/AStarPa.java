package org.graphstream.algorithm;

import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import org.graphstream.graph.*;

public class AStarPa implements Algorithm
{
	protected class BigNode
	{
		public Node		node;
		public BigNode	parent;
		public int		a;
		public int		z;
		public int		m;
		
		BigNode(Node nod, BigNode papa, int b, int y)
		{
			node = nod;
			parent = papa;
			a = b;
			z = y;
			m = a + z;
		}
		
		public void update(BigNode parent2, int n, int b)
		{
			open2.get(m).remove(node);
			parent = parent2;
			m = n;
			a = b;
			insertIntoOpen2(m, node);
			
		}
	}
	
	protected Graph									graph;
	protected Node										start;
	protected Node										finish;
	protected HashMap<Node, BigNode>				open			= new HashMap<Node, BigNode>();
	protected HashMap<Integer, HashSet<Node>>	open2			= new HashMap<Integer, HashSet<Node>>();
	protected int										best			= 0;
	protected HashMap<Node, BigNode>				close			= new HashMap<Node, BigNode>();
	protected Path										path			= new Path();
	protected String									marker;
	protected Object									object;
	protected String									weight		= "weight";
	protected String									monsterName	= "notinpath";
	
	public void init(Graph g)
	{
		graph = g;
	}
	
	public void setCondition(String mark, Object obj)
	{
		marker = mark;
		object = obj;
	}
	
	public Stack<Node> getPath()
	{
		return (Stack<Node>) path.getNodePath();
	}
	
	public void setWeight(String mark)
	{
		weight = mark;
	}
	
	public void setMonsterName(String name)
	{
		monsterName = name;
	}
	
	public void setStart(String startnode)
	{
		start = graph.getNode(startnode);
	}
	
	public void setStart(Node node)
	{
		start = node;
	}
	
	public void setFinish(String finishnode)
	{
		finish = graph.getNode(finishnode);
	}
	
	public void setFinish(Node node)
	{
		finish = node;
	}
	
	public int distMan(Node node, Node target)
	{
		if (node != null && target != null)
		{
			double xy1[] = nodePosition(node);
			double xy2[] = nodePosition(target);
			
			int dx = (int) Math.abs(xy2[0] - xy1[0]);
			int dy = (int) Math.abs(xy2[1] - xy1[1]);
			
			return dx + dy;
		} else
			return 0;
	}
	
	public int distEuc(Node node, Node target)
	{
		if (node != null && target != null)
		{
			double xy1[] = nodePosition(node);
			double xy2[] = nodePosition(target);
			
			int dx = (int) Math.abs(xy2[0] - xy1[0]);
			int dy = (int) Math.abs(xy2[1] - xy1[1]);
			
			return dx * dx + dy * dy;
		} else
			return 0;
	}
	
	public void compute()
	{
		best = distMan(start, finish);
		open.put(start, new BigNode(start, new BigNode(null, null, 0, 0), 0, best));
		
		insertIntoOpen2(best, start);
		while (!open.isEmpty())
		{
			Node bestNode = getBest();
			BigNode parent = open.get(bestNode);
			if (bestNode == finish)
			{
				close.put(bestNode, parent);
				makePath();
				break;
			}
			for (Edge edge : bestNode.getEachEdge())
			{
				Node opNode = edge.getOpposite(bestNode);
				int a = 0;
				a = cost(opNode) + open.get(bestNode).a;
				
				int z = distMan(opNode, finish);
				int m = a + z;
				
				if (close.containsKey(opNode))
				{
					continue;
				} else if (open.containsKey(opNode))
				{
					if (open.get(opNode).m > m)
					{
						open2.get(open.get(opNode).m).remove(opNode);
						open.get(opNode).update(parent, m, a);
						insertIntoOpen2(m, opNode);
					} else
					{
						continue;
					}
				} else
				{
					open.put(opNode, new BigNode(opNode, parent, a, z));
					insertIntoOpen2(m, opNode);
				}
				best = (best < m) ? best : m;
			}
			open2.get(open.get(bestNode).m).remove(bestNode);
			close.put(bestNode, open.get(bestNode));
			open.remove(bestNode);
			
		}
		
	}
	
	private void makePath()
	{
		Node node = finish;
		path.setRoot(node);
		Edge edge;
		Node nextNode = close.get(node).parent.node;
		while (nextNode != null)
		{
			node.addAttribute("ui.class", monsterName);
			edge = node.getEdgeBetween(nextNode);
			path.push(node, edge);
			node = nextNode;
			nextNode = close.get(node).parent.node;
		}
		
	}
	
	private int cost(Node node)
	{
		return (node.getAttribute("monstre") == "true") ? 10 : 1;
	}
	
	@SuppressWarnings("unused")
	private int cost(Edge edge)
	{
		// Object att = edge.getAttribute(weight);
		
		// return (Integer) ((att != null) ? att : 1);
		return 1;
	}
	
	private Node getBest()
	{
		Node result = null;
		while (result == null && graph.getNodeCount() >= best)
		{
			if (open2.get(best) == null || open2.get(best).isEmpty())
			{
				best++;
			} else
			{
				Iterator<Node> truc = open2.get(best).iterator();
				result = truc.next();
				
			}
		}
		return result;
	}
	
	private void insertIntoOpen2(int best2, Node start2)
	{
		if (open2.get(best2) == null || open2.get(best2).isEmpty())
		{
			open2.put(best2, new HashSet<Node>());
		}
		open2.get(best2).add(start2);
	}
	
	public void reset()
	{
		start = null;
		finish = null;
		open = new HashMap<Node, BigNode>();
		open2 = new HashMap<Integer, HashSet<Node>>();
		best = 0;
		close = new HashMap<Node, BigNode>();
		path = new Path();
	}
	
}
