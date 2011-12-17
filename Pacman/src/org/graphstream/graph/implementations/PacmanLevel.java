package org.graphstream.graph.implementations;

//a transformer en generator
import org.graphstream.graph.*;
import org.graphstream.util.Random;
import static org.graphstream.algorithm.ToolkitPa.*;

@SuppressWarnings("unused")
public class PacmanLevel extends SingleGraph
{

	public PacmanLevel(String id, int taille)
	{	
		super(id, true, false, taille * (taille + 1), 2 * taille * taille);
		addAttribute("taille", taille);
		Node node0 = addNode(nodeName(0, 0));
		node0.addAttribute("gomme", false);
		node0.addAttribute("superGomme", false);
		node0.addAttribute("monstre", false);
		node0.addAttribute("pacman", true);
		node0.addAttribute("xy", 0, 0);
		Random.reinit(1113);
		for (int i = 0; i < taille; i++)
		{
			String idx1 = nodeName(i, 0);
			String idy1 = nodeName(0, i);
			for (int j = 0; j < taille -1; j++)
			{
				String idx2 = nodeName(i, j + 1);
				String idy2 = nodeName(j+1,i);
				
				if (getNode(idx2) == null)
				{	
					Node node = addNode(idx2);
					node.addAttribute("gomme", false);
					node.addAttribute("superGomme", false);
					node.addAttribute("monstre", false);
					node.addAttribute("pacman", false);
					node.addAttribute("xy", i, j+1);
				}
				
				if (getNode(idy2) == null)
				{
					Node node = addNode(idy2);
					node.addAttribute("gomme", false);
					node.addAttribute("superGomme", false);
					node.addAttribute("monstre", false);
					node.addAttribute("pacman", false);
					node.addAttribute("xy", j+1, i);
				}

				Edge edgex = addEdge(idx1 + "-" + idx2, idx1, idx2);
				edgex.addAttribute("weight", Random.next());
				Edge edgey = addEdge(idy1 + "-" + idy2, idy1, idy2);
				edgey.addAttribute("weight", Random.next());

				idx1 = idx2;
				idy1 = idy2;
			}
		}
		
	
	}
	//from graphstream source
	protected String nodeName(int x, int y)
	{
		return Integer.toString(x) + "_" + Integer.toString(y);
	}

}
