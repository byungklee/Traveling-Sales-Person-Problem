//Comparator for priority queue. lowest value always go to head to be popped.
import java.util.Comparator;
public class NodeComparator implements Comparator<Node>{
	
	public int compare(Node n1, Node n2)
	{
		if (n1.getHeuristicValue() < n2.getHeuristicValue())
        {
            return -1;
        }
        if (n1.getHeuristicValue() > n2.getHeuristicValue())
        {
            return 1;
        }
		return 0;
	}

}
