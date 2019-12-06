package image.segmentation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

public class DirectedGraph extends Graph
{
	public DirectedGraph()
	{
		this(0, false);
	}
	
	public DirectedGraph(Integer n)
	{
		this(n, false);
	}
	
	public DirectedGraph(Integer n, Boolean weighted)
	{
		super(n);
		this.directed = true;
		this.weighted = weighted;
	}
	
	public DirectedGraph(Graph g)
	{
		super(g);
	}
	
	@Override
	public Graph addNode(Integer node)
	{
		this.nodes.add(node);
		
		return this;
	}
	
	@Override
	public Graph removeNode(Integer node)
	{
		Set<Edge> in = this.incoming(node);
		Set<Edge> out = this.outgoing(node);
		
		this.edges.removeAll(in);
		this.edges.removeAll(out);
		this.nodes.remove(node);
		
		return this;
	}
	
	@Override
	public Graph addEdge(Edge e)
	{
		if( !this.weighted )
		{
			return this.addEdge(e.source(), e.destination());
		}

		return this.addEdge(e.source(), e.destination(), e.flow(), e.capacity());
	}
	
	@Override
	public Graph addEdge(Integer nodeU, Integer nodeV)
	{
		if( this.weighted )
		{
			return this.addEdge(nodeU, nodeV, 0);
		}
		
		if( nodeU.equals(nodeV) )
		{
			throw new IllegalArgumentException("Self loops not allowed in graph");
		}
		
		if( this.nodes.contains(nodeU) && this.nodes.contains(nodeV) )
		{
			this.edges.add(new Edge(nodeU, nodeV));
		}
		else
		{
			throw new IndexOutOfBoundsException("The nodes must be present in the graph");
		}
		
		return this;
	}
	
	@Override
	public Graph addEdge(Integer nodeU, Integer nodeV, Integer capacity)
	{
		return this.addEdge(nodeU, nodeV, 0, capacity);
	}
	
	@Override
	public Graph addEdge(Integer nodeU, Integer nodeV, Integer flow, Integer capacity)
	{
		if( !this.weighted )
		{
			throw new IllegalArgumentException("Graph must be weighted to add capacity");
		}
		
		if( this.nodes.contains(nodeU) && this.nodes.contains(nodeV) )
		{
			this.edges.add(new Edge(nodeU, nodeV, flow, capacity));
		}
		else
		{
			throw new IndexOutOfBoundsException("The nodes must be present in the graph");
		}
		
		return this;
	}
	
	@Override
	public Graph removeEdge(Integer nodeU, Integer nodeV)
	{
		Edge e = new Edge(nodeU, nodeV);
		
		this.edges.remove(e);
		
		return this;
	}
	
	@Override
	public Graph setEdge(Integer nodeU, Integer nodeV, Integer weight)
	{
		return this.setEdge(nodeU, nodeV, 0, weight);
	}
	
	@Override
	public Set<Edge> edges()
	{
		Set<Edge> copy = new HashSet<Edge>();
		Iterator<Edge> iter = this.edges.iterator();
		
		while( iter.hasNext() )
		{
			Edge e = new Edge(iter.next());
			copy.add(e);
		}
		
		return copy;
	}
	
	@Override
	public Set<Edge> incoming(Integer node)
	{
		Set<Edge> ret = new HashSet<Edge>();
		Iterator<Edge> iter = this.edges.iterator();
		
		while( iter.hasNext() )
		{
			Edge e = iter.next();
			
			if( e.destination().equals(node) )
			{
				ret.add(new Edge(e));
			}
		}
		
		return ret;
	}
	
	@Override
	public Set<Edge> outgoing(Integer node)
	{
		Set<Edge> ret = new HashSet<Edge>();
		Iterator<Edge> iter = this.edges.iterator();
		
		while( iter.hasNext() )
		{
			Edge e = iter.next();
			
			if( e.source().equals(node) )
			{
				ret.add(new Edge(e));
			}
		}
		
		return ret;
	}
	
	public Iterator<Integer> nodeIterator()
	{
		return this.nodes().iterator();
	}
	
	@Override
	public String toString()
	{
		return this.edges.toString();
	}
	
	@Override
	public Map<Integer, Edge> BFS_(Integer nodeU, Integer nodeV)
	{
		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		Map<Integer, Edge> path = new HashMap<Integer, Edge>();
		
		Queue<Integer> queue = new LinkedList<Integer>();
		
		queue.add(nodeU);
		visited.putIfAbsent(nodeU, true);
		path.putIfAbsent(nodeU, null);
		
		while( !queue.isEmpty() )
		{
			Integer cur = queue.poll();
			Set<Edge> neighbors = this.outgoing(cur);
			Iterator<Edge> iter = neighbors.iterator();
			
			while( iter.hasNext() )
			{
				Edge e = iter.next();
				// if not null
				if( visited.get(e.destination()) == null )
				{
					visited.putIfAbsent(e.destination(), true);
					path.put(e.destination(), e);
					queue.add(e.destination());
				}
			}
		}
		
		return path;
	}
	
	/*
	 * get edge connecting two nodes
	 */
	@Override
	public Edge getEdge(Integer nodeU, Integer nodeV)
	{
		Edge e = new Edge(nodeU, nodeV);
		Iterator<Edge> iter = this.edges.iterator();
		
		while( iter.hasNext() )
		{
			Edge cur = iter.next();
			if( cur.equals(e) )
			{
				return cur;
			}
		}
		
		return null;
	}
	
	@Override
	public Edge getEdge(Edge e)
	{
		return this.getEdge(e.source(), e.destination());
	}
	
	@Override
	public Iterator<Edge> edgeIterator()
	{
		return this.edges().iterator();
	}
	
	/*
	 * create network with initial flows of zero
	 */
	@Override
	public Graph createNetwork()
	{
		Iterator<Edge> iter = this.edges().iterator();
		
		while( iter.hasNext() )
		{
			Edge e = iter.next();
			
			e.flow(0);
		}
		
		return this;
	}
	
	@Override
	public Graph updateNetwork(Set<Edge> path, Integer flow)
	{
		// get edges from the graph
		Set<Edge> orig = this.edges();
		
		// intersect with the path
		orig.retainAll(path);
		
		// update the edges in the original graph using the path found
		Iterator<Edge> iter = orig.iterator();
		
		while( iter.hasNext() )
		{
			Edge cur = iter.next();
			Integer augment = cur.flow() + flow;
			
			cur.flow(augment);
			
			this.setEdge(cur);
		}
		
		return this;
	}
	 
	 /*
	  * create residual graph
	  */
	 @Override
	 public Graph residualGraph()
	 {
	 	Iterator<Edge> iter = this.edges.iterator();
	 	Set<Edge> back_edges = new HashSet<Edge>();
	 	Set<Edge> to_remove = new HashSet<Edge>();
	 	Integer forward_flow, backward_flow;
	 	
	 	while( iter.hasNext() )
	 	{
	 		Edge cur = iter.next();
	 		Edge backward_edge = new Edge(cur).reverse();
	 		// backward edge = f(e) > 0
	 		backward_flow = cur.flow();
	 		if( backward_flow > 0 )
	 		{
		 		// add forward and backward edges
		 		//this.addEdge(backward_edge);
	 			backward_edge.flow(backward_flow);
		 		back_edges.add(backward_edge);
	 		}
	 		// forward flow = c(e) - f(e) > 0
	 		forward_flow = cur.capacity() - cur.flow();
	 		// if forward flow is zero, remove edge
	 		if( forward_flow <= 0 )
	 		{
	 			//this.removeEdge(cur);
	 			to_remove.add(cur);
	 		}
	 		else
	 		{
		 		// modify forward edge in residual graph to contain flow it can send
		 		cur.flow(forward_flow);
	 		}
	 	}
		
		this.edges.removeAll(to_remove);
		this.edges.addAll(back_edges);
		
		return this;
	}
	
	@Override
	public Graph removeEdge(Edge e)
	{
		return this.removeEdge(e.source(), e.destination());
	}
	
	@Override
	public Graph setEdge(Integer nodeU, Integer nodeV, Integer flow, Integer capacity)
	{
		this.removeEdge(nodeU, nodeV);
		
		if( this.weighted )
		{
			this.addEdge(nodeU, nodeV, flow, capacity);
		}
		else
		{
			this.addEdge(nodeU, nodeV);
		}
		
		return this;
	}
	
	@Override
	public Graph setEdge(Edge e)
	{
		return this.setEdge(e.source(), e.destination(), e.flow(), e.capacity());
	}
	
	@Override
	public Solution makePath(Map<Integer, Edge> in, Integer nodeU, Integer nodeV)
	{
		Solution path = null;
		Integer parent = nodeV;
		
		path = new Solution();
		
		path.edges = new HashSet<Edge>();
		path.minflow = Integer.MAX_VALUE;
		
		Edge e = null;
		while( ( e = in.get(parent) ) != null )
		{
			if( e.flow() > 0 && e.flow() < path.minflow )
			{
				path.minflow = e.flow();
			}
			path.edges.add(e);
			parent = e.source();
		}
		
		if( path.edges.size() == 0 )
		{
			return null;
		}
		
		return path;
	}
	
	@Override
	public Graph edmondsKarp(Integer nodeU, Integer nodeV)
	{
		// create residual graph
		Graph h = new DirectedGraph(this);
		Graph r = new DirectedGraph(h);
		
		r.residualGraph();
		
		// run bfs on residual graph and while a path exists
		// keep augmenting the flow through the path
		Map<Integer, Edge> bfs = r.BFS_(nodeU, nodeV);
		
		Solution path = null;
		
		while( ( path = this.makePath(bfs, nodeU, nodeV) ) != null )
		{
			if( path.minflow > 0 )
			{
				h.updateNetwork(path.edges, path.minflow);
			}
			
			r = new DirectedGraph(h);
			r.residualGraph();
			bfs = r.BFS_(nodeU, nodeV);
		}
		
		// found max flow here
		// sum of outgoing flow from source = sum of incoming flow in sink
		Integer sumU = 0, sumV = 0;
		Iterator<Edge> s_edges = h.outgoing(nodeU).iterator();
		Iterator<Edge> t_edges = h.incoming(nodeV).iterator();
		
		while( s_edges.hasNext() )
		{
			Edge e = s_edges.next();
			
			sumU += e.flow();
		}
		
		while( t_edges.hasNext() )
		{
			Edge e = t_edges.next();
			
			sumV += e.flow();
		}
		
		System.out.println("t_edges: " + h.incoming(nodeV) + ", " + h.incoming(nodeV).size());
		
		if( !sumU.equals(sumV) )
		{
			Logger logger = Logger.getLogger(this.getClass().getName());
			
			logger.warning("flow in " + sumU + " not equal to flow out " + sumV);
		}
		
		h.maxflow = sumU;
		
		return h;
	}
	
	@Override
	public Solution minimumCut(Integer nodeU)
	{
		Graph residual = new DirectedGraph(this);
		
		residual.residualGraph();
		Solution mincut = new Solution();
		
		mincut.background = new HashSet<Integer>();
		mincut.foreground = this.nodes();
		
		Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
		Queue<Integer> queue = new LinkedList<Integer>();
		
		queue.add(nodeU);
		visited.putIfAbsent(nodeU, true);
		
		while( !queue.isEmpty() )
		{
			Integer cur = queue.poll();
			Set<Edge> neighbors = residual.outgoing(cur);
			Iterator<Edge> iter = neighbors.iterator();
			
			while( iter.hasNext() )
			{
				Edge e = iter.next();
				// if not null
				if( visited.get(e.destination()) == null || visited.get(e.destination()) == false )
				{
					visited.putIfAbsent(e.destination(), true);
					queue.add(e.destination());
				}
			}
		}
		
		mincut.background.addAll(visited.keySet());
		mincut.foreground.removeAll(visited.keySet());
		
		return mincut;
	}
}
