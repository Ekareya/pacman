import static pa.pacman.Config.*;
import processing.core.*;

public class Pacman extends Perso
{
	int	nextDir;
	
	Pacman(PApplet parent,String nom)
	{
		super(parent);
		vitesse = PACMANSPEED;
		name=nom;
	}
	
	// ---------------
	void deplacer()
	{
		if (frame >= vitesse)
		{	
			switch (nextDir)
			{  case -PConstants.UP:
				case PConstants.UP:
					haut();
					break;
				case -PConstants.DOWN:
				case PConstants.DOWN:
					bas();
					break;
				case -PConstants.LEFT:
				case PConstants.LEFT:
					gauche();
					break;
				case -PConstants.RIGHT:
				case PConstants.RIGHT:
					droite();
					break;
				default: frame=vitesse;break;
			}
			pacNode=getNode();

			if (pacNode.getAttribute("gomme") == "true")
			{
				pacNode.addAttribute("gomme", "false");
				if(pacNode.getAttribute("superGomme") == "true")
				{
					hunted=vitesse*-30;
					pacNode.addAttribute("superGomme", "false");
				}
				score++;
				
			}
			
			if(nextDir<0)
			{
				direction=0;
			}
		}
		else if(direction==0)
		{frame=vitesse;}
		else
			frame++;
		
		if(hunted<0)
			hunted++;
		
	}
	
	public void orienter()
	{
			if(nextDir>0 && ((p.keyCode==PConstants.UP&&direction==PConstants.DOWN )|| (p.keyCode==PConstants.LEFT&&direction==PConstants.RIGHT)||( p.keyCode==PConstants.RIGHT&&direction==PConstants.LEFT)||( p.keyCode==PConstants.DOWN&&direction==PConstants.UP)))
					{
					nextDir=-nextDir;
					}
					
			else
				nextDir=p.keyCode;
	}
	protected void paintoff()
	{		
	p.fill(0);
	p.rect(PAS / 6-(PAS/vitesse), PAS / 6-(PAS/vitesse), 2 * PAS / 3+(PAS/vitesse)*2, 2 * PAS / 3+(PAS/vitesse)*2);
		
	}
	protected void paint()
	{	
		String dir="up";
		int direc = (direction==0)?(nextDir<0)?-nextDir:nextDir:direction;
		switch (direc)
		{
			case PConstants.UP:
				dir="up";
				break;
			case PConstants.DOWN:
				dir="down";
				break;
			case PConstants.LEFT:
				dir="left";
				break;
			case PConstants.RIGHT:
				dir="right";
				break;
		}
		PImage b;
		int truc=(int)Math.abs(frame/(vitesse/4));
		truc=truc%4;
		if (truc==0)
				truc=2;
		b = p.loadImage("../data/pac"+dir+truc+".PNG");
		p.fill(0);
		p.image(b, PAS / 6, PAS / 6, 2 * PAS / 3, 2 * PAS / 3);
	}
	
}
