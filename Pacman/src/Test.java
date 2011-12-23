import java.util.HashSet;
import processing.core.*;
import static pa.pacman.Config.*;
import org.graphstream.algorithm.LabyGenerator;
import org.graphstream.graph.*;
import static org.graphstream.algorithm.ToolkitPa.*;
import static org.graphstream.algorithm.Toolkit.*;

public class Test extends PApplet

{
public static void main(String [] args){
   PApplet.main(new String[] { "Test" });
	
} 
	
	Pacman			pacman;	
	Monstre			blinky;	
	Monstre			pinky	;	
	Monstre			inky	;	
	Monstre			clyde	;	
	HashSet<Perso>	perso		= new HashSet<Perso>();
	int				oldscore	= score;
	
	//-------------------------
	public void setup()
	{	

	
		size(MARGE * 2 + W * PAS, MARGE * 2 + H * PAS);
		background(0);

		laby = weight(rectGridGenerator(H, W, 0));
		String stylesheet= 
				"node.notinpath{fill-color:black;size:1px;}"+
				"node.blinky{fill-color:red;size:10px;}"+
				"node.pinky{fill-color:pink;size:10px;}"+
				"node.inky{fill-color:cyan;size:10px;}"+
				"node.clyde{fill-color:orange;size:10px;}";
		
		laby.addAttribute("ui.stylesheet",stylesheet);
		//laby.display(false);
		LabyGenerator gen = new LabyGenerator();
		gen.init(laby);
		gen.setRemoveDeadEnd(1);
		gen.setRemove4Cycle(true);//
		gen.setPruning(true);
		gen.setRemoveBottleNeck(true);
		gen.compute();
		
		for (Node node : laby)
		{	node.addAttribute("ui.label",node.getId());
			node.addAttribute("gomme", "true");
			node.addAttribute("superGomme", "false");
			node.addAttribute("monstre", "false");
			node.addAttribute("pacman", "false");
			node.addAttribute("ui.class","notinpath");
		}
		
		laby.getNode("0_0").addAttribute("pacman", "true");
		laby.getNode("0_0").addAttribute("gomme", "false");
		
		for(Edge edge:laby.getEachEdge())
		{
			edge.addAttribute("weight",1);
		}
			
		
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

pacman	= new Pacman(this, "pacman");
blinky	= new Monstre(this, "blinky");
pinky		= new Monstre(this, "pinky");
inky		= new Monstre(this, "inky");
clyde		= new Monstre(this, "clyde");
perso.add(pacman);
perso.add(blinky);
perso.add(pinky);
perso.add(inky);
perso.add(clyde);

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
		Perso mort=null;
		for(Perso bidule :perso)
		{
			if(bidule==pacman)
				continue;
			if(bidule.x==pacman.x && bidule.y==pacman.y)
			{
				mort=bidule;
			}
		}
		if(mort!=null)
			perso.remove(pacman);
		
		
		
		if (score > oldscore)
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
		pushMatrix();
		translate(x * PAS + MARGE, y * PAS + MARGE);
		fill(255);
		ellipse(PAS / 2, PAS / 2, PAS / 4, PAS / 4);
		popMatrix();
	}
	private void line2(int x0, int y0, int x1, int y1, int color)
	{
		stroke(color);
		line(x0 * PAS + MARGE, y0 * PAS + MARGE, x1 * PAS + MARGE, y1 * PAS + MARGE);
	}
}
