package image.segmentation;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public interface GraphInterface
{
	public Set<Edge> edges();
	
	public Set<Edge> incoming(Integer node);
	
	public Set<Edge> outgoing(Integer node);
	
	public Graph addNode(Integer node);
	
	public Graph removeNode(Integer node);
	
	public Graph addEdge(Edge e);
	
	public Graph addEdge(Integer nodeU, Integer nodeV);
	
	public Graph addEdge(Integer nodeU, Integer nodeV, Integer capacity);

	public Graph addEdge(Integer nodeU, Integer nodeV, Integer flow, Integer capacity);
	
	public Edge getEdge(Edge e);
	
	public Graph setEdge(Integer nodeU, Integer nodeV, Integer flow, Integer capacity);
	
	public Graph setEdge(Integer nodeU, Integer nodeV, Integer capacity);
	
	public Graph setEdge(Edge e);
	
	public Graph removeEdge(Edge e);
	
	public Graph removeEdge(Integer nodeU, Integer nodeV);

	public Map<Integer, Edge> BFS_(Integer nodeU, Integer nodeV);
	
	public Iterator<Integer> nodeIterator();
	
	public Iterator<Edge> edgeIterator();
	
	public Edge getEdge(Integer nodeU, Integer nodeV);
	
	public Graph createNetwork();

	public Graph updateNetwork(Set<Edge> path, Integer flow);
	
	public Graph residualGraph();
	
	public Graph edmondsKarp(Integer nodeU, Integer nodeV);

	public Solution makePath(Map<Integer, Edge> in, Integer nodeU, Integer nodeV);

	public Solution minimumCut(Integer nodeU);
}
