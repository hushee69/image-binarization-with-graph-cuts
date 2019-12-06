package image.segmentation;

public class Edge
{
	private Integer source, destination;
	private Integer flow, capacity;
	
	public Edge(Integer nodeU, Integer nodeV)
	{
		this.source = nodeU;
		this.destination = nodeV;
	}
	
	public Edge(Integer nodeU, Integer nodeV, Integer capacity)
	{
		this(nodeU, nodeV, 0, capacity);
	}
	
	public Edge(Integer nodeU, Integer nodeV, Integer flow, Integer capacity)
	{
		this(nodeU, nodeV);
		if( flow > capacity )
		{
			throw new IllegalArgumentException("Flow can not exceed capacity");
		}
		this.flow = flow;
		this.capacity = capacity;
	}
	
	public Edge(Edge e)
	{
		this.source = e.source;
		this.destination = e.destination;
		this.flow = e.flow;
		this.capacity = e.capacity;
	}
	
	public Integer source()
	{
		return this.source;
	}
	
	public Integer destination()
	{
		return this.destination;
	}
	
	public Integer capacity()
	{
		return this.capacity;
	}
	
	public void capacity(Integer w)
	{
		this.capacity = w;
	}

	public Integer flow()
	{
		return this.flow;
	}
	
	public void flow(Integer f)
	{
		if( f > this.capacity() )
		{
			throw new IllegalArgumentException("Flow can not exceed capacity: " + f + " > " + this.capacity);
		}
		
		this.flow = f;
	}
	
	/*
	 * (source, destination, flow, capacity)
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("(" + source + ", " + destination);
		if( this.flow != null )
		{
			sb.append(", " + this.flow);
		}
		if( this.capacity != null )
		{
			sb.append(", " + this.capacity);
		}
		sb.append(")");
		
		return sb.toString();
	}
	
	public Edge reverse()
	{
		Integer temp = this.source;
		this.source = this.destination;
		this.destination = temp;
		
		return this;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if( o == this )
		{
			return true;
		}
		
		if( !( o instanceof Edge ) )
		{
			return false;
		}
		
		Edge e = (Edge) o;
		
		return e.source().equals(this.source) && e.destination().equals(this.destination);
	}
	
	/*
	 * useful so as to not add the same edge twice even if the edge has different weights
	 */
	@Override
	public int hashCode()
	{
		int result = 0;
		
		result = this.source.hashCode() * this.destination.hashCode();
		
		return result;
	}
}
