import java.util.Random;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Prim;
import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;


@SuppressWarnings("unused")
public class OldTp2 {
		

 
       public static void main(String[] args) {
           BaseGenerator gen= new GridGenerator();
           
  	   	   Graph chemin = new SingleGraph("chemin");
  	   	   Graph laby = new SingleGraph("laby");
  			chemin.setStrict(false);
  			chemin.setAutoCreate(true);
  			laby.setStrict(false);
  			laby.setAutoCreate(true);
  		  			
  	   	   String css ="node {fill-color : blue; size : 3px; text-alignment : at-right; } edge{size:1px;} ";
           
  	   	   laby.addAttribute("ui.quality");
   		   laby.addAttribute("ui.antialias");		
   		   laby.addAttribute("ui.stylesheet", css);
   		   
           chemin.addAttribute("ui.quality");
   		   chemin.addAttribute("ui.antialias");		
   		   chemin.addAttribute("ui.stylesheet", css);
  	   	   
    	   int n = 5;
  	   	  	
   	       gen.addSink(chemin);
   	       gen.addSink(laby);
    	   gen.begin();   	    	   	   
    	   gen.addEdgeAttribute("weight");
           gen.setEdgeAttributesRange(1, n);
           
    	   for(int i=0; i<n; i++) {
    	                  gen.nextEvents();
    	   }
    	   gen.removeSink(chemin);
    	   gen.nextEvents();
    	   gen.end();
    	//fin de la generation des grilles
    	   
    	   

   		  
   		   
           //calcul de l'arbre couvrant
           Prim prim = new Prim("ui.class", "intree", "notintree");

           prim.init(chemin);
           prim.compute();
           
           
           //rajouter des arretes pour creer des cycles
           
           
           //fabrication du labyrinthe
           
           
           for (int i = 0 ; i < 2*n*(n+1) ; i++)          
           {	Edge truc = chemin.getEdge(i+"");
        	   if( truc.getAttribute("ui.class")=="intree" )	
           		{	
        	   
        	   		String node0=truc.getNode0().getId();
           			String[] truc0=node0.split("_");
           			int x0 = Integer.parseInt(truc0[0]);
           			int y0 = Integer.parseInt(truc0[1]);
           			
        	   		String node1=truc.getNode1().getId();
           			String[] truc1=node1.split("_");
           			int x1 = Integer.parseInt(truc1[0]);
           			int y1 = Integer.parseInt(truc1[1]);
           			
           			if(Math.abs(x0+1-x1)+Math.abs(y0-y1-1)==1)
        			{laby.removeEdge((x0+1)+"_"+y0,x1+"_"+(y1+1));}
        			else
        			{laby.removeEdge(x0+"_"+(y0+1),(x1+1)+"_"+y1);}
           			
           			
           		 	   
           	     
           		}
        	   
        	   else
        	   {chemin.removeEdge(truc);}//enleve les arretes inutiles pour le dijkstra
           		
           }
           
           //chemin.display(false);
           
           //determiné l'entrée:
           int debut = (int)(Math.random()*n);
           int fin=(int)(Math.random()*n);
           System.out.println(debut+"--"+fin);
           laby.removeEdge(debut+"_0",(debut+1)+"_0");
           laby.removeEdge(fin+"_"+(n+1),(fin+1)+"_"+(n+1));
           Node entree = chemin.getNode(debut+"_0");
           Node sortie = chemin.getNode(fin+"_"+n);
         

           

           Dijkstra dijkstra = new Dijkstra();
                  
           // Compute the shortest paths in g from A to all nodes
           dijkstra.init(chemin);
           dijkstra.setSource(entree);
           dijkstra.compute();
                 
           int i=1;
                            //getPathEdges
           // Color in blue all the nodes on the shortest path form A to B
           for (Edge edge : dijkstra.getPathEdges(sortie))
               	{	edge.addAttribute("ui.style", "fill-color: red;size:2px;");
               
               		  
                 	
        	   		String node0=edge.getNode0().getId();
        	   		String node1=edge.getNode1().getId();
					String[] truc0=node0.split("_");
           			
           			int x0 = Integer.parseInt(truc0[0]);
           			int y0 = Integer.parseInt(truc0[1]);
           			
           			String[] truc1=node1.split("_");
           			int x1 = Integer.parseInt(truc1[0]);
           			int y1 = Integer.parseInt(truc1[1]);
           			
           			
           			System.out.println();
           			
           			laby.addEdge("chemin_"+i,(x0+0.5)+"_"+(y0+0.5),(x1+0.5)+"_"+(y1+0.5));
           			laby.getEdge("chemin_"+i).addAttribute("ui.style","fill-color:red;");
    				laby.getNode((x0+0.5)+"_"+(y0+0.5)).setAttribute("xy", x0+0.5, y0+0.5);
    				laby.getNode((x1+0.5)+"_"+(y1+0.5)).setAttribute("xy", x1+0.5, y1+0.5);
           			i++;
           	     
           		}
           		
          
                      
                     
                      
           
           laby.display(false);
           chemin.display(false);
           
           try {
   			chemin.write("exemple.dgs");
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
    	   

 }
}