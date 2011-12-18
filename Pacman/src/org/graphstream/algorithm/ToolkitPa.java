package org.graphstream.algorithm;
import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.*;

public class ToolkitPa
{	
	public static Graph subGraph(Graph graph,String marker,Object object)
	{
		Graph h = new SingleGraph("sub_"+graph.getId());
		for(Node node : graph)
		{
			Node node2= h.addNode(node.getId());
			node2.addAttribute("xyz",nodePosition(node));
		}
		int d=0;
		for(Edge edge: graph.getEachEdge())
		{
			if(edge.getAttribute(marker)==object)
			{
				h.addEdge(Integer.toString(d),edge.getNode0().getId(),edge.getNode1().getId());
				d++;
			}
		}
		return h;
	}
	
	public static void edgeStyle(Graph g,String marker,Object value,String style)
	{
		for(Edge edge: g.getEachEdge())
		{
			if(edge.getAttribute(marker)==value)
				edge.addAttribute("ui.style", style);
		}
	}
	
	public static Graph weight(Graph g)
	{
		for(Edge edge:g.getEachEdge())
		{
			edge.addAttribute("weight",Math.random());
		}
		return g;
	}
	
	public static Graph rectGridGenerator(int size)
	{
		return rectGridGenerator(size, size, 0);
	}
	
	public static Graph rectGridGenerator(int height, int width)
	{
		return rectGridGenerator(height, width, 0);
	}
	

	
	public static Graph rectGridGenerator(int height, int width, int deep)
	{
		Graph gx = new SingleGraph("x", false, true);
		gx.addNode("0").addAttribute("xyz", 0, 0, 0);
		for (int i = 1; i < width; i++)
		{
			gx.addEdge(Integer.toString(i), Integer.toString(i - 1), Integer.toString(i));
			gx.getNode(Integer.toString(i)).addAttribute("xyz", i, 0, 0);
			
		}
		
		Graph gy = new SingleGraph("y", false, true);
		gy.addNode("0").addAttribute("xyz", 0, 0, 0);
		for (int i = 1; i < height; i++)
		{
			gy.addEdge(Integer.toString(i), Integer.toString(i - 1), Integer.toString(i));
			gy.getNode(Integer.toString(i)).addAttribute("xyz", 0, i, 0);
			
		}
		if (deep != 0)
		{
			Graph gz = new SingleGraph("z", false, true);
			gz.addNode("0").addAttribute("xyz", 0, 0, 0);
			for (int i = 1; i < deep; i++)
			{
				gz.addEdge(Integer.toString(i), Integer.toString(i - 1), Integer.toString(i));
				gz.getNode(Integer.toString(i)).addAttribute("xyz", 0, 0, i);
				
			}
			return cartesianProduct(cartesianProduct(gx, gy), gz);
		} 
		else
			return cartesianProduct(gx, gy);
	}
	
	/**
	 * @see http://en.wikipedia.org/wiki/Cartesian_product_of_graphs
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public static Graph cartesianProduct(Graph g1, Graph g2)
	{
		Graph g = new SingleGraph(g1.getId() + " x " + g2.getId());
		
		for (Node node1 : g1)
		{
			double xyz1[] = nodePosition(node1);
			String id1 = node1.getId();
			for (Node node2 : g2)
			{
				double xyz2[] = nodePosition(node2);
				String id2 = id1 + "_" + node2.getId();
				Node node12 = g.addNode(id2);
				node12.addAttribute("xyz", xyz1[0] + xyz2[0], xyz1[1] + xyz2[1], xyz1[2] + xyz2[2]);
			}
		}
		
		int i = 0;
		for (Node node1 : g1)
		{
			String id1 = node1.getId();
			for (Node node2a : g2)
			{
				String id2 = id1 + "_" + node2a.getId();
				
				for (Node node2b : getNeighbourSet(node2a))
				{
					String id3 = id1 + "_" + node2b.getId();
					Node truc1 = g.getNode(id2);
					Node truc2 = g.getNode(id3);
					if (truc1.getEdgeBetween(truc2) == null)
					{
						g.addEdge(Integer.toString(i), truc1, truc2);
						i++;
					}
				}
			}
		}
		
		for (Node node2 : g2)
		{
			String id2 = node2.getId();
			for (Node node1a : g1)
			{
				String id1 = node1a.getId() + "_" + id2;
				
				for (Node node1b : getNeighbourSet(node1a))
				{
					String id3 = node1b.getId() + "_" + id2;
					Node truc1 = g.getNode(id1);
					Node truc2 = g.getNode(id3);
					if (truc1.getEdgeBetween(truc2) == null)
					{
						g.addEdge(Integer.toString(i), truc1, truc2);
						i++;
					}
				}
			}
		}
		
		return g;
		
	}
	
	/**
	 * @author GraphStream
	 * @param x
	 * @param y
	 * @return
	 * @see org.graphstream.algorithm.Toolkit#nodeName
	 */
	protected String nodeName(int x, int y)
	{
		return Integer.toString(x) + "_" + Integer.toString(y);
	}
	
	/**
	 * 
	 * @param node
	 * @param marker
	 * @param object
	 * @return
	 */
	public static int getInDegreeWhereEdgeIs(Node node, String marker, Object object)
	{
		int result = 0;
		for (Edge edge : node.getEdgeSet())
		{
			if (edge.getAttribute(marker) == object)
				result++;
		}
		return result;
		
	}
	
	/**
	 * 
	 * @param node
	 * @param marker
	 * @param object
	 * @return
	 */
	public static ArrayList<Edge> getEachEdgeWhere(Node node, String marker, Object object)
	{
		ArrayList<Edge> neighbour = new ArrayList<Edge>();
		for (Edge edge : node.getEachEdge())
		{
			if (edge.getAttribute(marker) == object)
				neighbour.add(edge);
		}
		return neighbour;
		
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public static HashSet<Node> getNeighbourSet(Node node)
	{
		HashSet<Node> neighbour = new HashSet<Node>();
		for (Edge edge : node.getEachEdge())
		{
			neighbour.add(edge.getOpposite(node));
		}
		return neighbour;
		
	}
	public static ArrayList<Node> getNeighbourList(Node node)
	{
		ArrayList<Node> neighbour = new ArrayList<Node>();
		for (Edge edge : node.getEachEdge())
		{
			neighbour.add(edge.getOpposite(node));
		}
		return neighbour;
		
	}
	/**
	 * 
	 * @param node
	 * @param marker
	 * @param object
	 * @return
	 */
	public static ArrayList<Node> getNeighbourWhereEdge(Node node, String marker, Object object)
	{
		ArrayList<Node> neighbour = new ArrayList<Node>();
		for (Edge edge : node.getEachEdge())
		{
			if (edge.getAttribute(marker) == object)
				neighbour.add(edge.getOpposite(node));
		}
		return neighbour;
		
	}
	
	/**
	 * @see org.graphstream.algorithm.Toolkit#communities
	 * @param graph
	 * @param marker
	 * @return
	 */
	public static HashMap<Object, HashSet<Edge>> edgeCommunities(Graph graph, String marker)
	{
		HashMap<Object, HashSet<Edge>> communities = new HashMap<Object, HashSet<Edge>>();
		
		for (Edge edge : graph.getEachEdge())
		{
			Object communityMarker = edge.getAttribute(marker);
			
			if (communityMarker == null)
				communityMarker = "NULL_COMMUNITY";
			
			HashSet<Edge> community = communities.get(communityMarker);
			
			if (community == null)
			{
				community = new HashSet<Edge>();
				communities.put(communityMarker, community);
			}
			
			community.add(edge);
		}
		
		return communities;
	}
}
