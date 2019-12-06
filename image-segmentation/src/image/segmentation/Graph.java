package image.segmentation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class Graph implements GraphInterface
{
	protected Set<Integer> nodes;
	protected Set<Edge> edges;
	
	protected Boolean directed;
	protected Boolean weighted;
	
	protected Integer maxflow;
	
	public Graph()
	{
		this(0);
	}
	
	public Graph(Integer n)
	{
		if( n < 0 )
		{
			throw new IllegalArgumentException("number of nodes `n` can not be negative");
		}
		
		this.nodes = new HashSet<Integer>();
		for( int i = 0; i < n; ++i )
		{
			this.nodes.add((i + 1));
		}
		
		this.edges = new HashSet<Edge>();
		
		this.directed = this.weighted = false;
		
		this.maxflow = 0;
	}
	
	public Graph(Graph g)
	{
		this.directed = g.directed;
		this.weighted = g.weighted;
		this.nodes = g.nodes();
		this.edges = g.edges();
		this.maxflow = g.maxflow;
	}
	
	public Set<Integer> nodes()
	{
		Set<Integer> copy = new HashSet<Integer>();
		Iterator<Integer> iter = this.nodes.iterator();
		
		while( iter.hasNext() )
		{
			Integer i = Integer.valueOf(iter.next());
			copy.add(i);
		}
		
		return copy;
	}
	
	public Boolean isDirected()
	{
		return this.directed;
	}
	
	public Boolean isWeighted()
	{
		return this.weighted;
	}
	
	public Integer maxflow()
	{
		return this.maxflow;
	}
}
