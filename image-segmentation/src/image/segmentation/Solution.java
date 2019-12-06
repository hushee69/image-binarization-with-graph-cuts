package image.segmentation;

import java.util.Set;

public class Solution
{
	public Set<Edge> edges;
	public Integer minflow;
	
	public Set<Integer> background, foreground;
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		if( this.minflow != null && this.edges != null )
		{
			sb.append("minflow: " + this.minflow + ", path: " + this.edges.toString());
		}
		if( this.background != null && this.foreground != null )
		{
			sb.append("background: " + background.toString());
			sb.append("\nforeground: " + foreground.toString());
		}
		
		return sb.toString();
	}
}
