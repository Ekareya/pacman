import java.util.Stack;

import org.graphstream.graph.*;
import org.graphstream.algorithm.*;
import static pa.pacman.Config.*;
import processing.core.*;
import static org.graphstream.algorithm.Toolkit.*;


public class Monstre extends Perso
{
	Stack<Node>		c;
	int		ia;
	boolean	useSmallGraph	= false;
	
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
		x = (int) p.random(W - 1);
		y = (int) p.random(H - 1);
		vitesse = 22;
		ia = comportement;
		direction = 0;
		frame = frame2;
		c=new Stack<Node>();
		orienter();
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
		PImage b;
		b = p.loadImage("../data/" + name + dir + ((truc % 2) + 1) + ".PNG");
		p.fill(0);
		p.image(b, PAS / 6, PAS / 6, 2 * PAS / 3, 2 * PAS / 3);
		
	}
	
	public void deplacer()
	{
		if (frame == vitesse)
		{
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
		} else
			frame++;
		
	}
	
	public void orienter()
	{	double[] nextpos = null;
		switch (ia)
		{
			case 1:
				nextpos=welcomeToRandomLand();
				break;
			case 2:
				aStarIsBorn();
				break;
			case 3:
				dijkstraction();
				break;
			case 4:
				hunted();
				break;
		}
		
		int x0 = (int) nextpos[0];
		int y0 = (int) nextpos[1];
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
	
	private double[] welcomeToRandomLand()
	{	
		while (c.size() < 1)
		{	AStarPa astar = new AStarPa();
			astar.setStart(getNode());
			astar.setMonsterName(name);
			Node finish=getNode();
			while(astar.distMan(finish,getNode())<=4)
			{	finish = randomNode(laby);
				astar.setFinish(finish);
			}
			astar.init(laby);
			astar.compute();
			c = astar.getPath();
			c.pop();
		}
		c.peek().addAttribute("ui.class","notinpath");
		return nodePosition(c.pop());
	}
	
	private void hunted()
	{}
	
	private void dijkstraction()
	{}
	
	private void aStarIsBorn()
	{}
	
}
