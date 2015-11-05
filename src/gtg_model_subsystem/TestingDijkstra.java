package gtg_model_subsystem;
import java.awt.Point;
import gtg_model_subsystem.Node;
import gtg_model_subsystem.CoordinateGraph;
import gtg_model_subsystem.Edge;
import gtg_model_subsystem.Dijkstra;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TestingDijkstra {
	private List<Node> nodes;
	private List<Edge> edges;
	private Path path;
	private CoordinateGraph graph;
	private List<Admin> admins;
	private FileProcessing fileProcessing;
	private Map testMap;
	public void testDij(int start,int end){
		
		edges = new ArrayList<Edge>();
		nodes = new ArrayList<Node>();
		admins = new ArrayList<Admin>();
		fileProcessing = new FileProcessing();
		//testing for loading admins
		try{
			loadAdmin();
			printAdmins();
		}
		catch(IOException e){
			System.out.println(e.toString());
		}
		//testing for loading of nodes/edges
		try {
			loadNodes();
			loadEdges();
			graph = new CoordinateGraph(nodes, edges);
			testMap = new Map("testMap", graph, null);
			runDijkstra(start, end);
			runJDijkstra(start, end);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}//END CATCH loadNodes/Edges
		
		//testing for saving nodes and edges
		//try {
			//saveNodes();
			//saveEdges();
		//}
		//catch (IOException e) {
				// TODO Auto-generated catch block
				//System.out.println(e.toString());
		//}//END CATCH saveNodes/Edges
		
	}
	/** Temporary java dijkstra algorithim implemented by Joshua until he speaks with Libin about
	 *  fixing his up. First set the current maps graph into the algorithim. Next cycle through the nodes
	 *  and pluck out 
	 * @param start node ID
	 * @param end node ID
	 */
	public void runJDijkstra(int start, int end){
		//Create object instance with temporary dijkstra algorithim
		JDijkstra dijkstra = new JDijkstra(testMap.getGraph());
		//Values to hold the start point and end point for the path
		Node startPoint = null;
		Node endPoint = null;
	
		//Search though the node elements
		for(Node node: nodes){
			//IF current node ID equals start integer value THEN
			if(node.getID() == start)
				//SET start node as current node
				startPoint = node;
			//ELSE IF current node ID equals end integer value THEN
			else if(node.getID() == end)
				//SET end node as current node
				endPoint = node;
		}
		//Start the execution with the first starting node
		dijkstra.execute(startPoint);
		
		//Store the wayPoints for the map's graph
		LinkedList<Node> wayPoints = dijkstra.getPath(endPoint);
		
		//Create the new path with the start point, end point, and way points
		path = new Path(startPoint, endPoint, wayPoints);
		
		//Store it into the map
		testMap.setPath(path);
	}
	public void runDijkstra(int start, int end){
		int nodeId1;
		int nodeId2;
		int edgeLength;
		int count = 0;
		Edge e;
		Dijkstra d;
		int matrix[][]=new int[28][28];
		for(int i=0;i<=27;i++)
		  for(int j=0;j<=27;j++)
			matrix[i][j]=10000;  
		
		Iterator<Edge> it=edges.iterator();
		while(it.hasNext())
		{
		     e=(Edge)it.next();
		     nodeId1=e.getSource().getID();
		     nodeId2=e.getDestination().getID();
		     edgeLength=(int)e.getEdgeLength();
		     //System.out.println(nodeId1+" "+nodeId2+" "+edgeLength);
		     count++;
		     //System.out.println(count);
		     matrix[nodeId1][nodeId2]=edgeLength;
		}
		
//		for(int i=0;i<=27;i++)
//		{
//		  for(int j=0;j<=27;j++)
//			System.out.print(matrix[i][j]+" ");
//		  System.out.println();
//		}
		d=new Dijkstra(matrix,start,end);
		d.find();
		d.printShortestDistance();
		d.printPath();
	}
	
	/**
	 * isValidAdmin method validates if the user that has choosen to login as admin is an admin.
	 * @param userName the string representation of the login username
	 * @param password the string representation of the login password
	 * @return true if user is admin; otherwise false.
	 */
	public boolean isValidAdmin(String userName, String password){
		//Assume user is not an admin
		boolean isAdmin = false;
		//FOR EACH admin in admin list
		for(Admin admin: admins){
			//IF current admin user name EQUALS param username AND admin password EQUALS param password
			if((admin.getUsername() == userName) && (admin.getPassword() == password))
				//SET isAdmin as valid
				isAdmin =true;
		}
		return isAdmin;
	}
	/**
	 * Validates the point for the view. By returning a Node for the controller to extract a point.
	 * Intake x and y values for user chosen node points, then cycle through each node on current coordinate graph node list.
	 * Then keep track of current difference in x and y value. If the current difference is less then the previous difference;
	 * of both x and y points then set the new validated point as the new closest point.
	 * @param x the x value for the given point
	 * @param y the y value for the given point
	 * @return the more accurate validated point
	 */
	public Node validatePoint(int x, int y){
		Node validatedPoint = null;
		int currentDiffX = 0;
		int currentDiffY = 0;
		int prevDiffX = 0;
		int prevDiffY = 0;
		for(Node node: testMap.getGraph().getNodes()){
			currentDiffX = node.getX() - x;
			currentDiffY = node.getY() - y;
			if((currentDiffX < prevDiffX) && (currentDiffY < prevDiffY)){
				validatedPoint = node;
			}
				
		}
		return validatedPoint;
	}
	public void loadAdmin() throws IOException{
		fileProcessing.readAdmin(admins, "ModelFiles\\adminFile.txt");
	}
	public void loadNodes() throws IOException{
		fileProcessing.readNodesFile(nodes, MapNodeURLS.TEST_MAP_NODES);
		
	}
	public void loadEdges() throws IOException{
		fileProcessing.readEdgesFile(nodes, edges, MapEdgeURLS.TEST_MAP_EDGES);
	}
	public void saveNodes() throws IOException{
		fileProcessing.saveNodesFile(nodes, MapNodeURLS.TEST_MAP_NODES);
	}
	public void saveEdges() throws IOException{
		fileProcessing.saveEdgesFile(edges, MapEdgeURLS.TEST_MAP_EDGES);
	}
	public void printNodes(){
		int count = 0;
		for(Node node: nodes){
			count++;
			System.out.print(node.getID() + " " + node.getX() + " " + node.getY());
			System.out.println();
		}
		System.out.println(count);
		System.out.println("END PRINT NODES");
	}
	public void printEdges(){
		for(Edge edge: edges){
			System.out.print(edge.getEdgeID() + " " + edge.getSource().getID() + " " + edge.getDestination().getID() + " " + edge.getEdgeLength());
			System.out.println();
		}
		System.out.println("END PRINT EDGES");
	}
	public void printAdmins(){
		for(Admin admin: admins){
			System.out.print(admin.getUsername()+ " " + admin.getPassword());
			System.out.println();
		}
		System.out.println("END OF PRINT ADMIN");
	}
}