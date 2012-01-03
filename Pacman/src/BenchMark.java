import org.graphstream.algorithm.AStar;
import org.graphstream.algorithm.AStarPa;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.LabyGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import static org.graphstream.algorithm.ToolkitPa.*;

public class BenchMark
{
	
	public static void main(String[] args)
	{
		class PacManCosts implements AStar.Costs
		{
			public double heuristic(Node node, Node target)
			{
				return distMan(node, target);
			}
			
			public double cost(Node parent, Edge edge, Node next)
			{
				return 1;
			}
		}
		
		int j = 500 ;
		int taille = 15;
		Graph grille = rectGridGenerator(taille, taille);
		LabyGenerator gen = new LabyGenerator();
		gen.setRemoveDeadEnd(1);
		gen.setRemove4Cycle(true);//
		gen.setPruning(true);
		gen.setRemoveBottleNeck(true);
		Node start = null;
		Node finish = null;
		AStarPa astarpa = new AStarPa();
		AStar astar = new AStar();
		astar.setCosts(new PacManCosts());
		Dijkstra dijkstra = new Dijkstra();
		
		for (int i = 0; i < j; i++)
		{
			Graph laby = weight(grille);
			gen.init(laby);
			gen.compute();
			start = laby.getNode("0_0");
			finish = laby.getNode(nodeName(taille - 1, taille - 1));
			astarpa.reset();
			astarpa.init(laby);
			astarpa.setStart(start);
			astarpa.setFinish(finish);
			astarpa.compute();
			int c = astarpa.getPath().size();
			astar.init(laby);
			astar.compute(start.getId(), finish.getId());
			int d = astar.getShortestPath().size();
			dijkstra.init(laby);
			dijkstra.setSource(start);
			dijkstra.compute();
			int e = dijkstra.getPath(finish).size();
			System.out.println(c+"-"+d+"-"+e);

		}
	
//	while(true)
//		{}
		int truc= 1;
	}
	
}
