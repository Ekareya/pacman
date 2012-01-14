import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

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
		ThreadMXBean mx = ManagementFactory.getThreadMXBean();
		
		int j = 1;
		
		
		LabyGenerator gen = new LabyGenerator();
		gen.setRemoveDeadEnd(1);
		gen.setRemove4Cycle(true);//
		gen.setPruning(true);
		gen.setRemoveBottleNeck(true);
		
		for(int k=1;k<=10;k++)
		{
		int taille = 15*k;
		
		Graph grille = rectGridGenerator(taille, taille);

		
		Node start = null;
		Node finish = null;
	
		AStarPa astarpa = new AStarPa();
		AStar astar = new AStar();
		astar.setCosts(new PacManCosts());
		Dijkstra dijkstra = new Dijkstra();
		
		int paSize=0;
		int asSize=0;
		int diSize=0;
		
		long paTime=0;
		long asTime=0;
		long diTime=0;
		
		for (int i = 0; i < j; i++)
		{
			Graph laby = weight(grille);
			gen.init(laby);
			gen.compute();
			
			start = laby.getNode("0_0");
			finish = laby.getNode(nodeName(taille - 1, taille - 1));
			
			paTime-=mx.getCurrentThreadCpuTime();
			astarpa.reset();
			astarpa.init(laby);
			astarpa.setStart(start);
			astarpa.setFinish(finish);
			astarpa.compute();
			paSize += astarpa.getPath().size();
			paTime+=mx.getCurrentThreadCpuTime();
			
			asTime-=mx.getCurrentThreadCpuTime();
			astar.init(laby);
			astar.compute(start.getId(), finish.getId());
			asSize += astar.getShortestPath().size();
			asTime+=mx.getCurrentThreadCpuTime();
			
			diTime-=mx.getCurrentThreadCpuTime();
			dijkstra.init(laby);
			dijkstra.setSource(start);
			dijkstra.compute();
			diSize += dijkstra.getPath(finish).size();
			diTime+=mx.getCurrentThreadCpuTime();

		}
		
		System.out.println("taille: "+taille);
		System.out.println("astarPa: "+(paTime/j)+"ns - "+(paSize/j)+"cases");
		System.out.println("astar: "+(asTime/j)+"ns - "+(asSize/j)+"cases");
		System.out.println("dijkstra: "+(diTime/j)+"ns - "+(diSize/j)+"cases");
		System.out.println();
		}
	
	}
	
}
