import static org.graphstream.ui.graphicGraph.GraphPosLengthUtils.nodePosition;
import org.graphstream.graph.*;
import static pa.pacman.Config.*;
import processing.core.*;

abstract class Perso
{
	// -------- attribut
	int		x;
	int		y;
	int		couleur;
	PApplet	p;
	
	// ------------------------
	Perso(PApplet parent)
	{
		p = parent;
		x = 0;
		y = 0;
		couleur = 0;
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
		p.fill(0);
		p.pushMatrix();
		p.translate(x * PAS + MARGE, y * PAS + MARGE);
		p.rect(1,1,PAS-1,PAS-1);
		
		if (getNode().getAttribute("gomme") == "oui")
		{
			p.fill(255);
			p.ellipse(PAS / 2, PAS / 2, PAS / 4, PAS / 4);
		}
		p.popMatrix();
		
	}
	
	protected void afficher()
	{
		p.noStroke();
		p.fill(couleur);
		p.pushMatrix();
		p.translate(x * PAS + MARGE, y * PAS + MARGE);
		paint();
		p.popMatrix();
		
	}
	protected abstract void paint();
	// ---------deplacement
	protected void haut()
	{
		if (verif(x, y, x, y - 1))
			y--;
	}
	
	protected void bas()
	{
		if (verif(x, y, x, y + 1))
			y++;
	}
	
	protected void droite()
	{
		if (verif(x, y, x + 1, y))
			x++;
	}
	
	protected void gauche()
	{
		if (verif(x, y, x - 1, y))
			x--;
	}
	
}
