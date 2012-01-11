import java.util.Stack;

import org.graphstream.graph.*;
import org.graphstream.algorithm.*;
import static pa.pacman.Config.*;
import processing.core.*;
import static org.graphstream.algorithm.Toolkit.*;
import static org.graphstream.algorithm.ToolkitPa.*;

public class Monstre extends Perso
{
	Stack<Node>	c;
	int			ia;
	boolean		useSmallGraph	= false;
	AStarPa		astar				= new AStarPa();
	int			deadCount	;
	
	Monstre(PApplet parent, String nom)
	{
		this(parent, nom, 1, 0);
		
	}
	
	Monstre(PApplet parent, String nom, int comportement)
	{
		this(parent, nom, comportement, 0);
	}
	
	Monstre(PApplet parent, String nom, int comportement, int frame2)
	{
		super(parent);
		name = nom;
		p = parent;
		while (distMan(pacNode, getNode()) < trackingDistance)
		{
			x = (int) p.random(W - 1);
			y = (int) p.random(H - 1);
		}
		vitesse = 22;
		ia = comportement;
		direction = 0;
		frame = frame2;
		astar.setMonsterName(name);
		astar.init(laby);
		c = new Stack<Node>();
		orienter();
		deadCount= vitesse * deadTime;
		
	}
	
	// --------------------
	public void setUseSmallGraph(boolean arg)
	{
		useSmallGraph = arg;
	}
	
	protected void paintoff()
	{
		PImage b;
		b = p.loadImage("../data/black1.PNG");
		p.fill(0);
		p.image(b, PAS / 6, PAS / 6, 2 * PAS / 3, 2 * PAS / 3);
	}
	
	protected void paint()
	{
		String imagename = "";
		if (dead)
		{
			String dir = "up";
			switch (direction)
			{
				case PConstants.UP:
					dir = "up";
					break;
				case PConstants.DOWN:
					dir = "down";
					break;
				case PConstants.LEFT:
					dir = "left";
					break;
				case PConstants.RIGHT:
					dir = "right";
					break;
			}
			imagename = "../data/mort" + dir + ".PNG";
		} else if (hunted < 0)
		{
			int truc = (int) Math.abs(frame / (vitesse / 4)) % 2 + 1;
			int truc1 = (int) Math.abs(frame / (vitesse / 2)) % 2 + 1;
			imagename = "../data/malade" + Integer.toString(truc1) + Integer.toString(truc) + ".PNG";
		} else
		{
			String dir = "up";
			switch (direction)
			{
				case PConstants.UP:
					dir = "up";
					break;
				case PConstants.DOWN:
					dir = "down";
					break;
				case PConstants.LEFT:
					dir = "left";
					break;
				case PConstants.RIGHT:
					dir = "right";
					break;
			}
			int truc = (int) Math.abs(frame / (vitesse / 4));
			imagename = "../data/" + name + dir + Integer.toString((truc % 2) + 1) + ".PNG";
		}
		PImage b;
		b = p.loadImage(imagename);
		p.fill(0);
		p.image(b, PAS / 6, PAS / 6, 2 * PAS / 3, 2 * PAS / 3);
		
	}
	
	public void deplacer()
	{
		if (frame == vitesse)
		{
			getNode().addAttribute("monstre", "false");
			switch (direction)
			{
				case PConstants.UP:
					haut();
					break;
				case PConstants.DOWN:
					bas();
					break;
				case PConstants.LEFT:
					gauche();
					break;
				case PConstants.RIGHT:
					droite();
					break;
			}
			orienter();
			getNode().addAttribute("monstre", "true");
		} else
			frame++;
		if (dead)
		{
			deadCount--;
			if (deadCount <= 0)
			{
				dead = false;
				deadCount = vitesse * deadTime;
			}
		}
		
	}
	
	public void orienter()
	{
		double[] nextpos = null;
		if (hunted < 0 || dead)
			nextpos = hunted();
		else
		{
			switch (ia)
			
			{
				case 1:
					nextpos = welcomeToRandomLand();
					break;
				case 2:
					nextpos = aStarIsBorn();
					break;
			// case 3:
			// dijkstraction();
			// break;
			// case 4:
			// hunted();
			// break;
			}
		}
		
		int x0 = (int) nextpos[0];
		int y0 = (int) nextpos[1];
		if (Math.abs(x0 - x + y0 - y) != 1)
		{
			if (x == x0)
			{
				if (y > y0)
					direction = PConstants.DOWN;
				else if (y < y0)
					direction = PConstants.UP;
			} else if (y == y0)
			{
				if (x > x0)
					direction = PConstants.RIGHT;
				else if (x < x0)
					direction = PConstants.LEFT;
				
			}
		} else
		{
			if (x == x0)
			{
				if (y < y0)
					direction = PConstants.DOWN;
				else if (y > y0)
					direction = PConstants.UP;
			} else if (y == y0)
			{
				if (x < x0)
					direction = PConstants.RIGHT;
				else if (x > x0)
					direction = PConstants.LEFT;
			}
		}
	}
	
	private double[] welcomeToRandomLand()
	{
		if (c.size() < 1)
		{
			astar.reset();
			astar.setStart(getNode());
			Node finish = getNode();
			while (astar.distMan(finish, getNode()) <= 4)
			{
				finish = randomNode(laby);
				astar.setFinish(finish);
			}
			
			astar.compute();
			c = astar.getPath();
			c.pop();
		}
		c.peek().addAttribute("ui.class", "notinpath");
		return nodePosition(c.pop());
	}
	
	private double[] hunted()
	{
		int dist2Pac = astar.distMan(getNode(), pacNode);
		if (c.size() > 1 && dist2Pac <= trackingDistance)
		{
			c = new Stack<Node>();
		}
		if (dist2Pac <= trackingDistance / 2)
		{
			Node nextNode = getNode();
			int dist = 0;
			for (Node node : getNeighbourSet(getNode()))
			{
				if (astar.distMan(node, pacNode) > dist)// dist ==0 evite que le
																		// monstre // reste sur
																		// place
				{
					nextNode = node;
					dist = dist2Pac;
				}
			}
			return nodePosition(nextNode);
		} else
			return welcomeToRandomLand();
	}
	
	// private void dijkstraction()
	// {}
	
	private double[] aStarIsBorn()
	{
		if (astar.distMan(getNode(), pacNode) <= trackingDistance)
		{
			if (c.size() < 1 || (random.nextBoolean() && distMan(getNode(), pacNode) < distMan(c.peek(), pacNode)))
			{
				astar.reset();
				astar.setStart(getNode());
				astar.setFinish(pacNode);
				astar.compute();
				c = astar.getPath();
				c.pop();
			}
			c.peek().addAttribute("ui.class", "notinpath");
			return nodePosition(c.pop());
		} else
			return welcomeToRandomLand();
	}
	
}
