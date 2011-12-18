
import org.graphstream.graph.*;
import org.graphstream.algorithm.*;

import static pa.pacman.Config.*;
import processing.core.*;
import static org.graphstream.algorithm.Toolkit.*;


public class Monstre extends Perso
{	
	Path c;
	int compteur;
	
public double distMan(Node node, Node target) 
{	  double xy1[] = nodePosition(node);
	  double xy2[] = nodePosition(target);

	  double dx = Math.abs(xy2[0] - xy1[0]);
	  double dy = Math.abs(xy2[1] - xy1[1]);

	  return dx+dy;
}

	class PacManCosts implements AStar.Costs 
{   public double heuristic(Node node, Node target) 
	  {	  return distMan(node,target);	  }

	  public double cost(Node parent, Edge edge, Node next) 
	  {	  return (next.getAttribute("Monstre") != "true") ? 1 : H*W; 
	  }
	}
	
	
  Monstre(PApplet parent)
   { 
	  super(parent);
	  p=parent;
	  x= (int)p.random(W-1);
     y= (int)p.random(H-1);
     c=new Path();
     c.setRoot(getNode());
     couleur=p.color(255,0,0);
     compteur=0;
  } 
  //--------------------

  public void deplacer()
  {aleapath();}
  
  public void deplacer(int type,Node pacman)
  {  enlever();
    switch(type)
	 {  case 0:  aleagraph();break;
	 	case 1:  atable(pacman);break;
	 				
	 	default: aleabasic();break;
  	 	
	 }
    afficher();
  }
  private void atable(Node pacman)
  {AStar astar = new AStar(laby);
  astar.setCosts(new PacManCosts());

	if(c.size()<=1 || distMan(getNode(),pacman)< distMan(c.peekNode(),pacman ))
	{
  	astar.compute(pacman.getId(),x+"_"+y); // with A and Z node identifiers in the graph. 
 	c = astar.getShortestPath();
 	c.popNode();
	}
	node2coord(c.popNode());
}
  
  
 
private void aleapath()
  { AStar astar = new AStar(laby);
   astar.setCosts(new PacManCosts());

	if(c.size()==1)
	{
   	astar.compute(randomNode(laby).getId(),getId()); // with A and Z node identifiers in the graph. 
  	c = astar.getShortestPath();
  	c.popNode();
	}
	node2coord(c.popNode());
 }
  
  
  private void aleagraph() 
  {  Node node= getNode();
  	 int i = node.getInDegree();
  	 int dep=(int)p.random(i);
  	 node2coord(node.getEdge(dep).getOpposite(node));

  }
  

private void aleabasic()
  {	int dep=(int)p.random(4);
	switch(dep)
    {  case 0:  haut();break;
       case 1:  bas();break;
       case 2:  gauche();break;
       case 3:  droite();break;
    }
  }

@Override
protected void paint()
{
	// TODO Auto-generated method stub
	
}
  
}