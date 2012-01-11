package pa.pacman;

import java.util.Random;

import org.graphstream.graph.*;

public final class Config
{

	public static final int	H		= 15;
	public static final int W		= 10;
	public static final int TUNNELHORI = 2;
	public static final int TUNNELVERT = 1;
	public static final int	PAS	= 40;
	public static final int	MARGE	= 50;
	public static Graph laby = null;
	public static Graph smallLaby=null;
	public static int score =0;
	public static Node pacNode=null;
	public static int trackingDistance=10;
	public static Random random = new Random(); 
	public static int hunted = 0;
	public static int deadTime=20;

}
