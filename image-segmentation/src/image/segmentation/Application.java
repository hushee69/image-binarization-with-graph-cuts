/*
 * Compilation
 * mvn package
 * java -cp target/Image-segmentation-0.0.1-SNAPSHOT.jar image.segmentation.Application
 * files to be read should be in the base directory of the application
 * Execution example
 * enter file to read: infile.txt
 * enter filename to write separated pixels to: outputfile.txt
 * At the end of execution, the application will overwrite existing file or create a new file (if the output file doesn't exist) and write the separated pixels into it
*/

package image.segmentation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Application
{
	public static void main(String[] args)
	{
		String filepath = System.getProperty("user.dir");
		String infile = null;
		
		String[] dims = null;
		
		Graph g = null;
		BufferedReader br = null;
		
		// read in file
		// first line - dimensions of image
		try
		{
			System.out.print("Enter file to read: ");
			br = new BufferedReader(new InputStreamReader(System.in));
			
			infile = br.readLine();
			if( infile.length() == 0 )
			{
				throw new IOException("Input filename can not be blank");
			}
			
			System.out.println("Reading in file: " + infile);
			br = new BufferedReader(new FileReader(filepath + "/" + infile));
			String line = br.readLine();
			dims = line.split(" ");
			Integer vertex_count = 0;
			if( dims.length > 0 )
			{
				vertex_count = Integer.valueOf(dims[0]) * Integer.valueOf(dims[1]);
				vertex_count += 2;
			}
			
			System.out.println("vertex count: " + vertex_count);
			
			// add vertex_count + 2 nodes (2 - for source and sink)
			g = new DirectedGraph(vertex_count, true);
			
			// assume source = 1 and sink = vertex_count
			Integer level = 0;
			Integer node_idx = 2;
			while( ( line = br.readLine() ) != null )
			{
				// each blank line indicates a change
				if( line.length() > 0 )
				{
					switch( level )
					{
						case 1:
						{
							// connect source to all other nodes except sink
							String[] splitted = line.split("\\s+");
							for( int i = 0; i < splitted.length; ++i )
							{
								Integer capacity = Integer.valueOf(splitted[i]);
								g.addEdge(1, node_idx, capacity);
								node_idx++;
							}
							break;
						}
						case 2:
						{
							// connect all nodes except source to sink
							String[] splitted = line.split("\\s+");
							for( int i = 0; i < splitted.length; ++i )
							{
								Integer capacity = Integer.valueOf(splitted[i]);
								g.addEdge(node_idx, vertex_count, capacity);
								node_idx++;
							}
							break;
						}
						case 3:
						{
							// parse horizontal penalties
							String[] splitted = line.split("\\s+");
							Integer cur = node_idx, next = 0;
							for( int i = 0; i < splitted.length; ++i )
							{
								Integer capacity = Integer.valueOf(splitted[i]);
								cur = node_idx + i;
								next = node_idx + i + 1;
								// create horizontal edge between cur and next
								g.addEdge(cur, next, capacity);
								g.addEdge(next, cur, capacity);
							}
							node_idx += splitted.length + 1;
							break;
						}
						case 4:
						{
							// parse vertical penalties
							String[] splitted = line.split("\\s+");
							Integer cur = node_idx, next = 0;
							for( int i = 0; i < splitted.length; ++i )
							{
								Integer capacity = Integer.valueOf(splitted[i]);
								cur = node_idx + i;
								next = node_idx + splitted.length + i;
								//System.out.println("cur, next: " + cur + ", " + next);
								g.addEdge(cur, next, capacity);
								g.addEdge(next, cur, capacity);
							}
							node_idx += splitted.length;
							break;
						}
					}
				}
				else
				{
					node_idx = 2;
					level++;
				}
			}
			
			br.close();
		}
		catch( IOException e )
		{
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
//		System.out.println("g: " + g.toString());
		System.out.println("edge count: " + g.edges().size());
		
		long startTime = System.nanoTime();
		
		Graph gs = g.edmondsKarp(1, g.nodes().size());
		
		Solution mincut = gs.minimumCut(1);
		
		mincut.background.remove(1);
		mincut.foreground.remove(gs.nodes().size());
		
		long endTime = System.nanoTime();
		
		long timeElapsed = endTime - startTime;
		
		System.out.println("time taken to calculate max flow and set A and B: " + (timeElapsed / 1000000) + "ms");
		
		Map<Integer, Character> positions = new HashMap<Integer, Character>();
		Iterator<Integer> bg = mincut.background.iterator();
		Iterator<Integer> fg = mincut.foreground.iterator();
		
		while( bg.hasNext() )
		{
			Integer cur = bg.next();
			positions.put(cur, 'B');
		}
		
		while( fg.hasNext() )
		{
			Integer cur = fg.next();
			positions.put(cur, 'F');
		}
		
		System.out.println("max flow: " + gs.maxflow());
		System.out.println("background: " + mincut.background);
		System.out.println("foreground: " + mincut.foreground);
		
		BufferedWriter writer = null;
		String separated = null;
		
		try
		{
			System.out.print("Enter filename to write separated pixels to: ");
			br = new BufferedReader(new InputStreamReader(System.in));
			
			separated = br.readLine();
			if( separated.length() == 0 )
			{
				throw new IOException("Output filename can not be blank");
			}
			
			System.out.println("Writing separated pixels to: " + separated);
			writer = new BufferedWriter(new FileWriter(filepath + "/" + separated));
			Integer idx = 0;
			Integer nl_idx = Integer.valueOf(dims[1]);
			
			for( Map.Entry<Integer, Character> pos : positions.entrySet() )
			{
				if( idx >= nl_idx && idx % nl_idx == 0 )
				{
					writer.newLine();
				}
				
				writer.write(pos.getValue() + " ");
				
				idx++;
			}
			
			writer.newLine();
			
//			System.out.println(positions.toString());
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				br.close();
				writer.close();
			}
			catch( IOException e )
			{}
		}
	}
}
