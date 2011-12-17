package org.graphstream.algorithm;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import java.util.*;

@SuppressWarnings("unused")
public class ToolkitPa
{
	
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
	
	public static ArrayList<Edge> getEachEdgeWhere(Node node,String marker,Object object)
	{	
		ArrayList<Edge> neighbour = new ArrayList<Edge>();
		for(Edge edge: node.getEachEdge())
		{	if(edge.getAttribute(marker)==object)
				neighbour.add(edge);
		}
		return neighbour;
		
	}
	
	public static HashSet<Node> getNeighbour(Node node)
	{	
		HashSet<Node> neighbour = new HashSet<Node>();
		for(Edge edge: node.getEachEdge())
		{neighbour.add(edge.getOpposite(node));}
		return neighbour;
		
	}
	public static ArrayList<Node> getNeighbourWhereEdge(Node node,String marker,Object object)
	{	
		ArrayList<Node> neighbour = new ArrayList<Node>();
		for(Edge edge: node.getEachEdge())
		{if(edge.getAttribute(marker)==object)
			neighbour.add(edge.getOpposite(node));
		}
		return neighbour;
		
	}
	
 //From Graphstream toolkit
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
