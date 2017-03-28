import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;

public class DijkstraSeamCarver extends SeamCarver implements Comparator<Integer>
{

	public DijkstraSeamCarver(Picture pic)
	{
		super(pic);
	}

	// turn coordinate into unique id
	private int id(int x, int y)
	{
		return x * pic.height() + y;
	}

	private int x(int id)
	{
		return id / pic.height();
	}

	private int y(int id)
	{
		return id % pic.height();
	}

	private double energy(int id)
	{
		return energy(x(id), y(id));
	}

	private void relax(int cur, int next, double[][] dist, int[][] from, double edgeWeight)
	{
		double curCost = dist[y(cur)][x(cur)];
		double oldCost = dist[y(next)][x(next)];

		if (curCost + edgeWeight < oldCost)
		{
			dist[y(next)][x(next)] = curCost + edgeWeight;
			// dist.put(next, curCost + edgeWeight);
			from[y(next)][x(next)] = cur;
			// from.put(next, cur);
		}
	}

	private DirectedEdge[] dijkstra(EdgeWeightedDigraph G, int s, int t)
	{
		double[] distTo = new double[G.V()]; // distTo[v] = distance of shortest
												// s->v path
		DirectedEdge[] edgeTo = new DirectedEdge[G.V()]; // edgeTo[v] = last
															// edge on shortest
															// s->v
		// path
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(G.V()); // priority queue
																// of vertices
		Arrays.fill(distTo, Double.POSITIVE_INFINITY);
		distTo[s] = 0.0;

		pq.insert(s, distTo[s]);
		while (!pq.isEmpty())
		{
			int v = pq.delMin();

			// early termination
			// if (v == t)
			// break;

			for (DirectedEdge e : G.adj(v))
			{
				int from = e.from(), to = e.to();
				if (distTo[to] > distTo[from] + e.weight())
				{
					distTo[to] = distTo[from] + e.weight();
					edgeTo[to] = e;
					if (pq.contains(to))
						pq.decreaseKey(to, distTo[to]);
					else
						pq.insert(to, distTo[to]);
				}
			}
		}

		return edgeTo;
	}

	@Override
	public int[] findHorizontalSeam()
	{
		final int W = width();
		final int H = height();

		EdgeWeightedDigraph g = new EdgeWeightedDigraph(W * H + 2);

		int s = W * H;
		int t = W * H + 1;

		for (int i = 0; i < H; i++)
		{
			g.addEdge(new DirectedEdge(s, id(0, i), energy(0, i)));
			g.addEdge(new DirectedEdge(id(W - 1, i), t, 0));
		}

		for (int i = 0; i < H; i++)
		{
			for (int j = 0; j < W; j++)
			{
				int id = id(j, i);
				if (isInBound(j + 1, i + 1))
					g.addEdge(new DirectedEdge(id, id(j + 1, i + 1), energy(j + 1, i + 1)));
				if (isInBound(j + 1, i))
					g.addEdge(new DirectedEdge(id, id(j + 1, i), energy(j + 1, i)));
				if (isInBound(j + 1, i - 1))
					g.addEdge(new DirectedEdge(id, id(j + 1, i - 1), energy(j + 1, i - 1)));
			}
		}

		DirectedEdge[] from = dijkstra(g, s, t);

		int cur = t;

		int[] seam = new int[W];
		for (int i = seam.length - 1; i >= 0; i--)
		{
			cur = from[cur].from();
			seam[i] = y(cur);
		}

		return seam;
	}

	@Override
	public int[] findVerticalSeam()
	{
		// Width and height of current picture, for convenience
		final int W = width();
		final int H = height();

		// distance look up table
		// stores the distance from vertex [i, j] to the source
		double[][] dist = new double[H][W];
		for (double[] table : dist)
			Arrays.fill(table, Double.MAX_VALUE);

		// Use custom comparing method
		MinPQ<Integer> que = new MinPQ<Integer>(this);
		for (int j = 0; j < W; j++)
		{
			que.insert(id(j, 0));
			dist[0][j] = 0;
		}

		// a from table
		int[][] from = new int[H][W];

		// target we stopped at
		int t = 0;

		while (!que.isEmpty())
		{
			int id = que.delMin();
			int x = x(id);
			int y = y(id);
			if (y == H - 1)
			{
				t = id;
				break;
			}
			if (isInBound(x + 1, y + 1))
				relax(id, id(x + 1, y + 1), dist, from, energy(x, y + 1));
			if (isInBound(x + 1, y))
				relax(id, id(x + 1, y), dist, from, energy(x, y));
			if (isInBound(x + 1, y - 1))
				relax(id, id(x + 1, y - 1), dist, from, energy(x, y - 1));
		}

		int[] seam = new int[H];
		int cur = t;
		for (int i = H - 1; i >= 0; i--)
		{
			seam[i] = x(cur);
			cur = from[y(cur)][x(cur)];
		}
		return seam;
	}

	@Override
	public int compare(Integer o1, Integer o2)
	{
		return Double.compare(energy(o1), energy(o2));
	}
}
