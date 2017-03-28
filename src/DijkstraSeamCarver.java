import java.util.Arrays;
import java.util.Comparator;

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

	@Override
	public int[] findHorizontalSeam()
	{
		// Width and height of current picture, for convenience
		final int W = width();
		final int H = height();

		// Use custom comparing method
		MinPQ<Integer> que = new MinPQ<Integer>(this);
		for (int i = 0; i < H; i++)
			que.insert(id(0, i));

		// distance look up table
		// stores the distance from vertex [i, j] to the source
		double[][] dist = new double[H][W];
		for (double[] table : dist)
			Arrays.fill(table, Double.MAX_VALUE);

		int[][] from = new int[H][W];

		while (!que.isEmpty())
		{
			int id = que.delMin();
			int x = x(id);
			int y = y(id);
			
			if (isInBound(x + 1, y + 1))
				relax(id, id(x + 1, y + 1), dist, from, energy(x, y + 1));
			if (isInBound(x + 1, y))
				relax(id, id(x + 1, y), dist, from, energy(x, y));
			if (isInBound(x + 1, y - 1))
				relax(id, id(x + 1, y - 1), dist, from, energy(x, y - 1));
		}
		return null;
	}

	@Override
	public int[] findVerticalSeam()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compare(Integer o1, Integer o2)
	{
		return Double.compare(energy(o1), energy(o2));
	}
}
