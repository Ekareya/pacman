import java.util.ArrayList;
import java.util.HashSet;
import processing.core.*;
import static pa.pacman.Config.*;
import org.graphstream.algorithm.LabyGenerator;
import org.graphstream.graph.*;

import static org.graphstream.algorithm.ToolkitPa.*;
import static org.graphstream.algorithm.Toolkit.*;

public class Test extends PApplet

{
	public static void main(String[] args)
	{
		PApplet.main(new String[] { "Test" });
		
	}
	
	Pacman			pacman;
	Monstre			blinky;
	Monstre			pinky;
	Monstre			inky;
	Monstre			clyde;
	HashSet<Perso>	perso		= new HashSet<Perso>();
	int				oldscore	= score;
	
	// -------------------------
	public void setup()
	{
		
		size(MARGE * 2 + W * PAS, MARGE * 2 + H * PAS);
		background(0);
		
		laby = weight(rectGridGenerator(H, W, 0));
		String stylesheet = "node.notinpath{fill-color:black;size:10px;}" + "node.blinky{fill-color:red;size:10px;}" + "node.pinky{fill-color:pink;size:10px;}"
				+ "node.inky{fill-color:cyan;size:10px;}" + "node.clyde{fill-color:orange;size:10px;}";
		
		laby.addAttribute("ui.stylesheet", stylesheet);
		// laby.display(false);
		LabyGenerator gen = new LabyGenerator();
		gen.init(laby);
		gen.setRemoveDeadEnd(1);
		gen.setRemove4Cycle(true);//
		gen.setPruning(true);
		gen.setRemoveBottleNeck(true);
		gen.compute();
		
		for (Node node : laby)
		{
			//node.addAttribute("ui.label", node.getId());
			node.addAttribute("gomme", "true");
			node.addAttribute("monstre", "false");
			node.addAttribute("superGomme", "false");
			node.addAttribute("ui.class", "notinpath");
		}
		
		for (Edge edge : laby.getEachEdge())
		{
			edge.addAttribute("weight", 1);
		}
		
		for (int i = 0; i < 3; i++)
		{	
			Node node = randomNode(laby);
			if(node.getAttribute("superGomme")=="false")
			{
				node.addAttribute("superGomme","true");
			}
			else
				i--;
		}
		
		stroke(0, 200, 225);
		for (int i = 0; i <= H; i++)
		{
			line2(0, i, W, i, color(0, 200, 225));
		}
		for (int i = 0; i <= W; i++)
		{
			line2(i, 0, i, H, color(0, 200, 225));
		}
		
		for (Edge edge : laby.getEachEdge())
		{
			
			double[] node0 = nodePosition(edge.getNode0());
			
			int x0 = (int) node0[0];
			int y0 = (int) node0[1];
			
			double[] node1 = nodePosition(edge.getNode1());
			
			int x1 = (int) node1[0];
			int y1 = (int) node1[1];
			
			if (Math.abs(x0 + 1 - x1) + Math.abs(y0 - y1 - 1) == 1)
			{
				line2(x0 + 1, y0, x1, y1 + 1, 0);
			} else
			{
				line2(x0, y0 + 1, x1 + 1, y1, 0);
			}
		}

		for (int i = 0; i < TUNNELHORI; i++)
		{
				int alea = (int) Math.floor(Math.random() * H);
				
				Edge tunnel = laby.addEdge(Integer.toString(2 * laby.getEdgeCount()), nodeName(0, alea), nodeName(W - 1, alea));
				tunnel.addAttribute("mur", "false");
				tunnel.addAttribute("weight", 1);// augmenter si on veux que les
															// fantomes passent moins
				line2(0, alea, 0, alea + 1, 0);
				line2(W, alea, W, alea + 1, 0);
			
		}
		
		
		for (int i = 0; i < TUNNELVERT; i++)
		{
			int alea = (int) Math.floor(Math.random() * W);
				Edge tunnel = laby.addEdge(Integer.toString(2 *laby.getEdgeCount()), nodeName(alea, 0), nodeName(alea,H-1));
				tunnel.addAttribute("mur", "false");
				tunnel.addAttribute("weight", 1);// augmenter si on veux que les
															// fantomes ne passent pas
				line2(alea, 0, alea + 1, 0,0);
				line2(alea,H, alea+1, H, 0);
		}
		
		for (Node node : laby)
		{
			if (node.getAttribute("gomme") == "true")
			{
				double[] xyz = nodePosition(node);
				int x0 = (int) xyz[0];
				int y0 = (int) xyz[1];
				if(node.getAttribute("superGomme")=="true")
					drawPill(x0,y0,true);
				else
					drawPill(x0, y0 , false);
			}
		}
		fill(255);
		text(Integer.toString(score), 10, 15);
		
		smallLaby = subGraph(laby, "mur", "false");
		
		for (Edge edge : smallLaby.getEachEdge())
		{
			edge.addAttribute("weight", 1);
		}
		
		int d = smallLaby.getEdgeCount();
		edgeStyle(laby, "mur", "false", "fill-color:blue;size:5px;");
		//smallLaby.display(false);
		//laby.display(false);
		for (Node node : smallLaby)
		{
			int i = node.getInDegree();
			if (i == 2)
			{
				ArrayList<Node> temp = getNeighbourList(node);
				Edge edge0 = node.getEdgeBetween(temp.get(0));
				Edge edge1 = node.getEdgeBetween(temp.get(1));
				if (!temp.get(0).hasEdgeBetween(temp.get(1)))// verif pour garder un
																			// graphe simple
				{
					Edge edge = smallLaby.addEdge(Integer.toString(d), temp.get(0), temp.get(1));
					d++;
					int poid0 = (Integer) edge0.getAttribute("weight");
					int poid1 = (Integer) edge1.getAttribute("weight");
					edge.addAttribute("weight", poid0 + poid1);
					edge.addAttribute("ui.label", poid0 + poid1);
					
				} else
				{
					Edge edge = temp.get(0).getEdgeBetween(temp.get(1));
					int poid = (Integer) edge0.getAttribute("weight") + (Integer) edge1.getAttribute("weight");
					if (poid < (Integer) edge.getAttribute("weight"))
					{
						edge.addAttribute("weight", poid);
						edge.addAttribute("ui.label", poid);
					}
				}
				smallLaby.removeEdge(edge0);
				smallLaby.removeEdge(edge1);
				
			}
		}
		for (Node node : smallLaby)
		{
			if (node.getInDegree() == 0)
				node.addAttribute("ui.style", "size:0px;");
			else
				node.addAttribute("ui.label", node.getId());
		}
		
		pacman = new Pacman(this, "pacman");
		pacNode = pacman.getNode();
		blinky = new Monstre(this, "blinky", 1);
		pinky = new Monstre(this, "pinky", 2);
		inky = new Monstre(this, "inky", 2);
		clyde = new Monstre(this, "clyde", 2);
		
		perso.add(pacman);
		perso.add(blinky);
//		perso.add(pinky);
//		perso.add(inky);
//		perso.add(clyde);
		
		for (Perso bidule : perso)
		{
			bidule.afficher();
		}
	}
	
	int	i	= 0;
	
	public void draw()
	{	

		for (Perso bidule : perso)
		{
			bidule.enlever();
		}

		for (Perso bidule : perso)
		{
			bidule.deplacer();
		}

		for (Perso bidule : perso)
		{
			bidule.afficher();
		}
		for (Perso bidule : perso)
		{
			if (bidule == pacman)
				continue;
			else if (bidule.x == pacman.x && bidule.y == pacman.y)
			{	
				bidule.dead=true;//c'est le monste qui meurt a chaque fois
			}
		}
		
/*		if (score > oldscore)
		{
			oldscore = score;
			fill(0);
			rect(0, 0, H * PAS, MARGE - 1);
			fill(255);
			text(Integer.toString(score), MARGE / 2, 3 * MARGE / 4);
		}
		if (score == H * W - 1)
		{
			fill(0);
			rect(0, 0, H * PAS, MARGE - 1);
			fill(255);
			text("Bravo!!! Maintenant t'as l'air malin, coincé dans un labyrinthe sans rien à manger...", MARGE / 2, 3 * MARGE / 4);
			
		}
		*/
		fill(0);
		rect(0, 0, H * PAS, MARGE - 1);
		fill(255);
		text(Integer.toString(i), MARGE / 2, 3 * MARGE / 4);
		i++;
	}
	
	public void keyPressed()
	{
		if (key == CODED)
		{
			pacman.orienter();
			
		}
		
	}
	
	private static final long	serialVersionUID	= 1L;
	
	public void drawPill(int x, int y)
	{
		drawPill(x,y,false);
	}
	public void drawPill(int x, int y , boolean big)
	{
		int size=(big)?PAS / 2:PAS / 4;
		pushMatrix();
		translate(x * PAS + MARGE, y * PAS + MARGE);
		ellipse(PAS / 2, PAS / 2, size,size);
		popMatrix();
	}
	
	private void line2(int x0, int y0, int x1, int y1, int color)
	{
		stroke(color);
		line(x0 * PAS + MARGE, y0 * PAS + MARGE, x1 * PAS + MARGE, y1 * PAS + MARGE);
	}
}
