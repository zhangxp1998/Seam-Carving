import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class DPSeamCarving extends SeamCarver
{
	public DPSeamCarving(Picture pic)
	{
		super(pic);
	}

	private void relax(Point cur, Point next, double[][] dist, Point[][] from, double edgeWeight)
	{
		double curCost = dist[cur.y][cur.x];
		// double curCost = dist.get(cur);
		double oldCost = dist[next.y][next.x];
		// Double oldCost = dist.get(next);
		if (curCost + edgeWeight < oldCost)
		{
			dist[next.y][next.x] = curCost + edgeWeight;
			// dist.put(next, curCost + edgeWeight);
			from[next.y][next.x] = cur;
			// from.put(next, cur);
		}
	}

	public int[] findHorizontalSeam() // sequence of indices for horizontal seam
	{
		double endCost = Double.MAX_VALUE;
		double[][] dist = new double[pic.height()][pic.width()];
		for (double[] table : dist)
			Arrays.fill(table, Double.MAX_VALUE);

		Point endFrom = null;
		Point[][] from = new Point[pic.height()][pic.width()];

		Point start = new Point(-1, -1);
		// Point end = new Point(pic.width(), pic.height());

		for (int i = 0; i < pic.height(); i++)
		{
			Point p = new Point(0, i);
			from[i][0] = start;
			// from.put(p, start);
			dist[i][0] = energy(p.x, p.y);
			// dist.put(p, energy(p.x, p.y));
		}

		double[] lut = new double[pic.height()];
		for (int x = 0; x < pic.width(); x++)
		{
			if (x + 1 < pic.width())
			{
				for (int y = 0; y < lut.length; y++)
				{
					lut[y] = energy(x + 1, y);
				}
			}

			for (int y = 0; y < pic.height(); y++)
			{
				Point cur = new Point(x, y);
				if (x + 1 >= pic.width())
				{
					double curCost = dist[y][x];
					// double curCost = dist.get(cur);
					double oldCost = endCost;
					// Double oldCost = dist.get(end);
					if (curCost < oldCost)
					{
						endCost = curCost;
						// dist.put(end, curCost);
						endFrom = cur;
						// from.put(end, cur);
					}
				} else
				{
					if (isInBound(x + 1, y + 1))
						relax(cur, new Point(cur.x + 1, cur.y + 1), dist, from, lut[y + 1]);
					if (isInBound(x + 1, y))
						relax(cur, new Point(cur.x + 1, cur.y), dist, from, lut[y]);
					if (isInBound(x + 1, y - 1))
						relax(cur, new Point(cur.x + 1, cur.y - 1), dist, from, lut[y - 1]);
				}
			}
		}

		int[] seam = new int[pic.width()];
		Point cur = endFrom;
		for (int i = pic.width() - 1; i >= 0; i--)
		{
			seam[i] = cur.y;
			cur = from[cur.y][cur.x];
			// Point prev = from.get(cur);
			// cur = prev;
		}
		return seam;
	}

	public int[] findVerticalSeam() // sequence of indices for vertical seam
	{
		double endCost = Double.MAX_VALUE;
		double[][] dist = new double[pic.height()][pic.width()];
		for (double[] table : dist)
			Arrays.fill(table, Double.MAX_VALUE);

		Point endFrom = null;
		Point[][] from = new Point[pic.height()][pic.width()];

		Point start = new Point(-1, -1);
		// Point end = new Point(pic.width(), pic.height());

		for (int i = 0; i < pic.width(); i++)
		{
			Point p = new Point(i, 0);
			from[0][i] = start;
			// from.put(p, start);
			dist[0][i] = energy(p.x, p.y);
			// dist.put(p, energy(p.x, p.y));
		}

		double[] lut = new double[pic.width()];
		for (int y = 0; y < pic.height(); y++)
		{
			if (y + 1 < pic.height())
			{
				for (int x = 0; x < lut.length; x++)
				{
					lut[x] = energy(x, y + 1);
				}
			}

			for (int x = 0; x < pic.width(); x++)
			{
				Point cur = new Point(x, y);
				if (y + 1 >= pic.height())
				{
					double curCost = dist[y][x];
					// double curCost = dist.get(cur);
					double oldCost = endCost;
					// Double oldCost = dist.get(end);
					if (curCost < oldCost)
					{
						endCost = curCost;
						// dist.put(end, curCost);
						endFrom = cur;
						// from.put(end, cur);
					}
				} else
				{
					if (isInBound(x - 1, y + 1))
						relax(cur, new Point(cur.x - 1, cur.y + 1), dist, from, lut[x - 1]);
					if (isInBound(x, y + 1))
						relax(cur, new Point(cur.x, cur.y + 1), dist, from, lut[x]);
					if (isInBound(x + 1, y + 1))
						relax(cur, new Point(cur.x + 1, cur.y + 1), dist, from, lut[x + 1]);
				}
			}
		}

		int[] seam = new int[pic.height()];
		Point cur = endFrom;
		for (int i = pic.height() - 1; i >= 0; i--)
		{
			seam[i] = cur.x;
			cur = from[cur.y][cur.x];
		}
		return seam;
	}
}