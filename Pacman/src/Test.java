import com.nootropic.processing.layers.*;
import processing.core.*;
import static pa.pacman.Config.*;

import org.graphstream.algorithm.LabyGenerator;
import org.graphstream.graph.*;
import static org.graphstream.algorithm.ToolkitPa.*;
import static org.graphstream.algorithm.Toolkit.*;

public class Test extends PApplet
{
	Pacman							moi					= new Pacman(this);
	int								oldscore				= score;
	AppletLayers					layers;
	PFont								maTypo				= createFont("arial", 60);
	
	private static final long	serialVersionUID	= 1L;
	
	private void line2(int x0, int y0, int x1, int y1, int color)
	{
		stroke(color);
		line(x0 * PAS + MARGE, y0 * PAS + MARGE, x1 * PAS + MARGE, y1 * PAS + MARGE);
	}
	
	public void drawPill(int x, int y)
	{
		pushMatrix();
		translate(x * PAS + MARGE, y * PAS + MARGE);
		fill(255);
		ellipse(PAS / 2, PAS / 2, PAS / 4, PAS / 4);
		popMatrix();
	}
	
	public void setup()
	{
		size(MARGE * 2 + W * PAS, MARGE * 2 + H * PAS);
		background(0);
		layers = new AppletLayers(this);
		laby = weight(rectGridGenerator(H, W, 0));
		LabyGenerator gen = new LabyGenerator();
		gen.init(laby);
		gen.setRemoveDeadEnd(1);
		gen.setRemove4Cycle(true);//
		gen.setPruning(true);
		gen.setRemoveBottleNeck(true);
		gen.compute();
		
		for (Node node : laby)
		{
			node.addAttribute("gomme", "true");
			node.addAttribute("superGomme", "false");
			node.addAttribute("monstre", "false");
			node.addAttribute("pacman", "false");
		}
		
		laby.getNode("0_0").addAttribute("pacman", "true");
		laby.getNode("0_0").addAttribute("gomme", "false");
		for (int i = 0; i < 3; i++)
		{	
			
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
		for (Node node : laby)
		{
			if (node.getAttribute("gomme") == "true")
			{
				double[] xyz = nodePosition(node);
				int x0 = (int) xyz[0];
				int y0 = (int) xyz[1];
				drawPill(x0, y0);
			}
		}
		fill(255);
		text(Integer.toString(score), 10, 15);
		moi.afficher();
	}
	
	public void draw()
	{
		if (score > oldscore)
		{
			oldscore = score;
			fill(0);
			rect(0, 0, H * PAS, MARGE);
			fill(255);
			text(Integer.toString(score), MARGE / 2, 3 * MARGE / 4);
		}
		if(score==H*W-1)
		{			
		fill(0);
		rect(0, 0, H * PAS, MARGE);
		fill(255);
		text("Bravo!!!", MARGE / 2, 3 * MARGE / 4);
			
		}
	}
	
	public void keyPressed()
	{
		if (key == CODED)
		{
			moi.deplacer();
		}
	}
}
