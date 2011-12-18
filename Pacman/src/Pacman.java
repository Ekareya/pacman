import static pa.pacman.Config.*;
import processing.core.*;

public class Pacman extends Perso
{
		

	Pacman(PApplet parent)
	{
		super(parent);
		couleur = p.color(240, 195, 0);
	}
	
	// ---------------
	void deplacer()
	{
		enlever();
		switch (p.keyCode)
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
		if (getNode().getAttribute("gomme") == "true")
		{
			getNode().addAttribute("gomme", "false");
			score++;
		}
		afficher();
	}
	
	protected void paint()
	{
		p.fill(couleur);
		p.ellipse(PAS/2,PAS/2,PAS/2,PAS/2);

		
	}
	
}
