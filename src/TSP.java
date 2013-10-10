//Byung Lee
//012522664
//Assignment3 TSP Sequential solution.

import java.util.PriorityQueue; 
import java.util.Comparator;

public class TSP {
	
	
	Node map; // Map containing path values
	
	Node rootNode;	//Node containing root.
	
	PriorityQueue<Node> pq; //priority queue to contain nodes.
	
	double lowestHVal = 9999999; //heuristic value for lowest value. Initialized as a big number.
	Node lowestNode; //Node containing lowest path.
	
	int ran= 0;
	
	//initialization
	public TSP(Node map){		
		
		//initialization 
		this.map = map;
		
		int[][] temp = new int[map.getMap().length][map.getMap().length];
		for(int i=0;i<temp.length;i++)
		{
			for(int j=0;j<temp.length;j++)
			{
				if(i == j)
					temp[i][j] = -1; // cannot visit 				
				else
					temp[i][j] = 0; // not visited
			}
		}
		
		rootNode = new Node(temp);
		
		Comparator<Node> comparator = new NodeComparator();
		pq = new PriorityQueue<Node>(10, comparator);
		
	}	
	
	
	public void start()
	{
		//to set up initial node.
		//calculate rootNode of heuristic value
		rootNode.setHeuristicValue(heuristicFunction(rootNode));
		//add into priority queue
		pq.add(rootNode);
		//start finding path
		findPath();
		
		
		//print the result		
		System.out.println("---------FINAL----------");		
		int lMap[][] = lowestNode.getMap();		
		
		for(int c=0;c<lMap.length;c++)
		{
			for(int l=0;l<lMap.length;l++)
			{				
				System.out.print(lMap[c][l] + " ");				
			}
			System.out.println();
		}	
		
		int numberOfRows = lMap.length;		
		int previousRow = 0;
		int currentRow =0;
		String result = "1 ";
		while(numberOfRows != 0)
		{
			for(int g=0;g<lMap.length;g++)
			{
				if(lMap[currentRow][g] == 1 && previousRow != g)
				{
					result = result + (g+1) + " ";
					previousRow=currentRow;
					currentRow=g;
					numberOfRows--;
					break;
				}
			}
			
		}
		System.out.println(result);		
		
	}
	
	
	//Check the map is in the final state.
	public boolean isFinal(int[][] map)
	{		
		//if one of the cell is 0, return false.(this means it's not in the final state)
		for(int i=0;i<map.length;i++)
		{
			for(int j =0;j<map.length;j++)
			{
				if(map[i][j]==0)
				{
					return false;
				}
			}
		}
		
		return true;
	}

	//finding path
	public void findPath()
	{
		//if priority queue size is not empty, keep running
		while(pq.size() != 0)
		{	
			
			System.out.println("RUN " + ran + " "+ pq.size());
			
			//System.out.println("lowestHval "+ lowestHVal);
			ran++;
			
			//Pop node			
			Node currentNode = pq.remove();			
			
			//load current map condition
			int[][] currentMap = currentNode.getMap();			
						
			//If current node is lower than lowest heuristic value, generate below, else prune. 
			if(currentNode.getHeuristicValue()<lowestHVal)
			{
				//create new nodes for left and right
				Node leftNode = new Node(currentNode.getMap());
				Node rightNode = new Node(currentNode.getMap());				
				
				//load their map conditions
				int[][] leftMap = leftNode.getMap();
				int[][] rightMap = rightNode.getMap();
				//verify(currentMap);
				
				int saveRow = 0;				
								
				//check if the node is a last node.
				if(isFinal(currentMap))
				{					
					double temp = currentNode.getHeuristicValue();
					if(temp < lowestHVal)
					{
						lowestHVal = temp;
						lowestNode = currentNode;
					}
				}
				else
				{
					//Create two node with constraints
					
					
					boolean changeMade = false; //to check any changes have made.
					int numberOfOne=0; //to check how many 1 in a row.
					//find the current map has a row with one 1.
					
					//Checking any row consists only 1 one. and save the index of that row.
					//this is to not make a cycle consists not all the cities. 
					for(int i=0;i<currentMap.length;i++)
					{
						numberOfOne=0;
						for(int j=0;j<currentMap.length;j++)
						{
							if(numberOfOne == 2)
							{
								break;
							}							
							if(currentMap[i][j]==1)
							{
								numberOfOne++;								
							}
						}
						
						if(numberOfOne == 1)
						{
							saveRow = i;							
							break;
						}						
					}
					
					//if any row consists only 1 one, only generates the index of the row that consists only 1 one.
					if(numberOfOne == 1)
					{							
						for(int i=0;i<currentMap.length;i++)
						{
							//finds the first 0.
							if(currentMap[saveRow][i]==0)
							{
								//set the constraint of left child
								leftMap[saveRow][i]=1;
								leftMap[i][saveRow]=1;
								
								//set the constraint of right child
								rightMap[saveRow][i]=-1;
								rightMap[i][saveRow]=-1;
								
								//verify left child and right child
								verify(leftMap);
								verify(rightMap);
								
								//calculate the heuristic value of left child and right child
								leftNode.setHeuristicValue(heuristicFunction(leftNode));
								rightNode.setHeuristicValue(heuristicFunction(rightNode));

								//put them into the priority queue
								pq.add(leftNode);
								pq.add(rightNode);
								break;
							}
										
						}
					}
					else
					{
						//Case where the current condition has no row with only 1 one.
						for(int i = 0;i<leftMap.length;i++)
						{		
							numberOfOne = 0;
							for(int j = 0;i<leftMap.length;j++)
							{	
								//if the current row has 2 ones, then skip to the next row. 
								if(currentMap[i][j]==1)
								{
									numberOfOne++;
									if(numberOfOne == 2)
									{
										numberOfOne = 0;
										break;
									}
								}							
								else if(currentMap[i][j]==0) //if finds 0(first available slot)
								{						
									
									changeMade=true;
																		
									//set the constraint of left child.
									leftMap[i][j] = 1;
									leftMap[j][i] = 1;
									
									
									//set the constraint of right child
									rightMap[i][j] = -1;
									rightMap[j][i] = -1;		
									
									//verify them
									verify(leftMap);
									verify(rightMap);
									
									//set heuristic value for them
									leftNode.setHeuristicValue(heuristicFunction(leftNode));								
									rightNode.setHeuristicValue(heuristicFunction(rightNode));
									
									//add them into priority queue
									pq.add(leftNode);
									pq.add(rightNode);								
									
									break;						
								}									
							}						
							
							//if change has made, break and go pop the next node
							if(changeMade)
								break;
							
						}		
					
					}
					
				}		
			}
			else
			{
				//System.out.println("Pruned");
			}
		}
	}	
	
	
	//This checks the current map condition is consistent
	//meaning if one row has two 1(used edges), then other cells have to -1(unable edges)
	//also if the number of available edges are the same as the number of edges can be assigned,
	//then this cell must be assigned.
	//If any changes have made of this process, repeat this process again to make sure it is consistent.
	public void verify(int[][] map)
	{	
		
		boolean changeMade=true;
		while(changeMade==true)
		{
			//set nothing has changed
			changeMade = false;
			
			
			int numberOfCompletedRow = 0; //count the number of completed rows
			int[] savedRowWithOne1 = new int[2]; //find the row with the number with one one 
			int counter = 0;
			for(int i=0;i<map.length;i++)
			{
				
				int availableOne = 2; // number of available ones
				int numberOfZero = 0; // number of zeros
				
				for(int j=0;j<map.length;j++)
				{	
					//if see 1, decrement availableOne
					if(map[i][j]==1)
					{
						availableOne--;						
						
					}
					//if see 0, increment numberOfZero
					if(map[i][j]==0)
					{
						numberOfZero++;
					}				
				
				}				
				
				//if two edges are already selected, set the rest as unavailable.
				if(availableOne == 0)
				{
					numberOfCompletedRow++; //increment the completed row
					
					//set the rest as unavailable.
					for(int j=0;j<map.length;j++)
					{
						if(map[i][j]==0)
						{
							map[i][j]=-1;
							map[j][i]=-1;
							changeMade=true; //set change has made
							
						}
					}
					
				}				
				else if(availableOne == numberOfZero) //if available edges are the same as edges that can be assigned
				{
					//make all the available edges to be selected.
					for(int j=0;j<map.length;j++)
					{
						if(map[i][j]==0)
						{
							map[i][j]=1;
							map[j][i]=1;
							availableOne--;
							changeMade=true;//set change has made
						}
					}				
					
				}
				/*
				System.out.println("verify---------------------");
				for(int t=0;t<map.length;t++)
				{
					for(int s=0;s<map.length;s++)
					{
						System.out.print(map[t][s]+ " ");
					}
					System.out.println();
				}
				
				System.out.println("counter: "  + counter + " row: " + i + " saverow[0]" + savedRowWithOne1[0]);				
				*/
				
				
				//if the row with the number of one is 1, save and increment counter
				if(availableOne == 1)
				{
					savedRowWithOne1[counter] = i;
					counter++;
				}
			}
			
			//This makes sure there cannot be a cycle if the completed row is bigger than 1
			//and less then cities - 1.
			//meaning that if there are 6 Cities: A,B,C,D,E,F,
			//then if we have C A B D C, this doesn't cover all the cities. This shouldn't happen.
			if(numberOfCompletedRow > 0 && numberOfCompletedRow<map.length-1)
			{
		
				//so from the above condition,
				//if C A B D, then C and D shouldn't connect each other.
				if(map[savedRowWithOne1[0]][savedRowWithOne1[1]] == 0)
				{
					map[savedRowWithOne1[0]][savedRowWithOne1[1]] = -1;
					changeMade = true;
				}
				
				if(map[savedRowWithOne1[1]][savedRowWithOne1[0]] == 0)
				{
					map[savedRowWithOne1[1]][savedRowWithOne1[0]] = -1;
					changeMade = true;
				}
				
			}
			
		} // end While, if change has made check the current map condition is consistent again.
	}
	
	
	//This method calculates heuristic value by finding two minimum edges in each cities.
	public double heuristicFunction(Node node)
	{			
		double sum = 0;
		int[][] loadMap = map.getMap(); //Load the original map containing costs.
		int[][] temp = node.getMap(); // Load the current condition of the map.
		int min1;
		int min2;
		int numberOfOne;		
		
		for(int i=0;i<temp.length;i++)		
		{
			min1=9999999;
			min2=9999999;
			numberOfOne = 0;
			
			//in each row, get the minimum values. If the cell has 1, this has to be the minimum.
			for(int j=0;j<temp.length;j++)
			{							
				//if 1 is found in a row
				if(temp[i][j] == 1)
				{
					if(numberOfOne == 0)
					{
						min1 = loadMap[i][j]; //first 1 is saved into min1
					}
					else if(numberOfOne == 1)
					{
						min2 = loadMap[i][j]; //second 1 is saved into min2
					}
					numberOfOne++;	//increment when it sees the 1
				}
				else if(temp[i][j] == 0)
				{
					//if numberOf1 is 0 in a current row
					if(numberOfOne == 0)
					{						
						//put the min values
						if(min1 > loadMap[i][j])
						{
							min1 = loadMap[i][j];
						}
						else if(min2 > loadMap[i][j])
						{
							min2 = loadMap[i][j];
						}
						
						//if min1 has lower value, swap the min1 and min2.						
						if(min1 < min2)
						{
							int tempo = min2;
							min2 = min1;
							min1 = tempo;
						}
						
					}
					//if numberOf1 is 1 in a current row, save only into min2
					else if(numberOfOne == 1)
					{
						if(min2 > loadMap[i][j])
						{
							min2 = loadMap[i][j];
						}
					}
					
				}				
			}
			//after finding the minimum of each row
			
			//add them into sum
			sum = sum+min1+min2;			
		}		
		
		//return sum/2
		return sum/2;
	
	}		
	
	public static void main(String args[])
	{		
		//the map with costs
		int[][] map = new int[][]{{-1,4,5,1,2,7,8,9},
					  {4,-1,9,5,3,4,7,10},
					  {5,9,-1,1,9,1,1,9},
					  {1,5,8,-1,5,5,8,4},
					  {2,3,9,5,-1,4,9,9},
					  {7,4,1,5,4,-1,9,1},
					  {8,7,1,8,9,9,-1,2},
					  {9,10,9,4,9,1,2,-1}};
		//create root node 
		Node rootNode = new Node(map);
		
		TSP tsp = new TSP(rootNode);		
		tsp.start();
	}
	
}
