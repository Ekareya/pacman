import static org.graphstream.algorithm.ToolkitPa.*;
import org.graphstream.algorithm.LabyGenerator;
import org.graphstream.graph.Graph;

public class Tp2
{	
	public static void main(String[] args)
	{
		Graph g = weight(rectGridGenerator(20,20,0));
		LabyGenerator laby = new LabyGenerator();
		laby.init(g);
		laby.setRemoveDeadEnd(1);
		laby.setRemove4Cycle(true);
		laby.setPruning(true);
		laby.compute();
		
		edgeStyle(g,"mur","false","fill-color:blue;size:5px;");
		
		g.display(false);
		
	}
	
}
