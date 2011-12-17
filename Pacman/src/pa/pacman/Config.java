package pa.pacman;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.PacmanLevel;

public final class Config
{

	public static final int	N		= 15;
	public static final int	PAS	= 20;
	public static final int	MARGE	= 20;
	public static Graph		laby	= new PacmanLevel("labyrinthe", N);

}
