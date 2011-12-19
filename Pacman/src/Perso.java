import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;
import org.graphstream.graph.*;
import static pa.pacman.Config.*;
import processing.core.*;

abstract class Perso
{
	// -------- attribut
	int		x;
	int		y;
	int		direction;
	float		frame;
	float		vitesse;
	PApplet	p;
	String	name;
	
	// ------------------------
	Perso(PApplet parent)
	{
		p = parent;
		x = 0;
		y = 0;
		frame = 0;
		direction = 0;
	}
	
	protected String getId()
	{
		return Integer.toString(x) + "_" + Integer.toString(y);
	}
	
	protected String getId(int x0, int y0)
	{
		return Integer.toString(x0) + "_" + Integer.toString(y0);
	}
	
	protected Node getNode()
	{
		return laby.getNode(getId());
	}
	
	protected Node getNode(String arg)
	{
		return laby.getNode(arg);
	}
	
	abstract void deplacer();
	public abstract void orienter();
	protected void node2coord(Node node)
	{
		double xy0[] = nodePosition(node);
		x = (int) xy0[0];
		y = (int) xy0[1];
	}
	
	protected boolean verif(int x0, int y0, int x1, int y1)
	{
		Node node = getNode(getId(x0, y0));
		return node.hasEdgeBetween(getId(x1, y1));
	}
	
	protected void enlever()
	{
		p.noStroke();
		p.pushMatrix();
		
		if (getNode(getId()).getAttribute("gomme") == "true")
		{
			p.translate(x * PAS + MARGE, y * PAS + MARGE);
			p.fill(255);
			p.ellipse(PAS / 2, PAS / 2, PAS / 4, PAS / 4);
			p.popMatrix();
			p.pushMatrix();
		}
		translate();
		paintoff();
		p.popMatrix();
	}
	
	protected void translate()
	{
		switch (direction)
		{
			case PConstants.UP:
				p.translate(x * PAS + MARGE, (y - (frame / vitesse)) * PAS + MARGE);
				break;
			case PConstants.DOWN:
				p.translate(x * PAS + MARGE, (y + (frame / vitesse)) * PAS + MARGE);
				break;
			case PConstants.LEFT:
				p.translate((x - (frame / vitesse)) * PAS + MARGE, y * PAS + MARGE);
				break;
			case PConstants.RIGHT:
				p.translate((x + (frame / vitesse)) * PAS + MARGE, y * PAS + MARGE);
				break;
			default:
				p.translate(x * PAS + MARGE, y * PAS + MARGE);
				break;
		}
	}
	
	protected void afficher()
	{
		p.pushMatrix();
		translate();
		paint();
		p.popMatrix();
		
	}
	
	protected abstract void paint();
	
	protected abstract void paintoff();
	
	// ---------deplacement
	protected void move()
	{
		switch (direction)
		{
			case PConstants.UP:
				y--;
				break;
			case PConstants.DOWN:
				y++;
				break;
			case PConstants.LEFT:
				x--;
				break;
			case PConstants.RIGHT:
				x++;
				break;
				default:break;
		}
		direction=0;
		frame = 0;
	}
	protected void haut()
	{	move();
		if (verif(x, y, x, y - 1))
		{
			direction=PConstants.UP;
		}
	}
	
	public double[] getCoord()
	{
		return null;
	}
	protected void bas()
	{	move();
		if (verif(x, y, x, y + 1))
		{
			direction=PConstants.DOWN;
		}
	}
	
	protected void droite()
	{	move();
		if (verif(x, y, x + 1, y))
		{
			direction=PConstants.RIGHT;
		}
	}
	
	protected void gauche()
	{	move();
		if (verif(x, y, x - 1, y))
		{
			direction=PConstants.LEFT;
		}
	}
	
}
