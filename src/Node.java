//Node including map and heuristic value
public class Node {
	
	int[][] map; //including map or current condition of map
	double heuristicValue;	
	
	public Node(int[][] map){		
		this.map = new int[map.length][map.length];
		copyMap(map);
	}
	
	public int[][] getMap()
	{
		
		return map;
	}
	
	public void copyMap(int[][] map)
	{
		for(int i =0;i<map.length;i++)
		{
			for(int j =0;j<map.length;j++)
			{		
				this.map[i][j] = map[i][j];				
			}
		}
	}	
	
	public void setHeuristicValue(double hValue)
	{
		heuristicValue= hValue;
	}
	
	public double getHeuristicValue()
	{
		return heuristicValue;
	}	
	
}
