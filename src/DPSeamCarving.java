import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class DPSeamCarving extends SeamCarver
{
	public DPSeamCarving(Picture pic)
	{
		super(pic);
	}

	private void relax(int cur, int next, double[] dist, int[] from, double edgeWeight)
	{
		double curCost = dist[cur];
		double oldCost = dist[next];
		if (curCost + edgeWeight < oldCost)
		{
			dist[next] = curCost + edgeWeight;
			from[next] = cur;
		}
	}

	public int[] findHorizontalSeam() // sequence of indices for horizontal seam
	{
		double endCost = Double.MAX_VALUE;
		double[] dist = new double[pic.height() * pic.width()];
		Arrays.fill(dist, Double.MAX_VALUE);

		int endFrom = 0;
		int[] from = new int[pic.height() * pic.width()];

		int start = id(-1, -1);
		// Point end = new Point(pic.width(), pic.height());

		for (int i = 0; i < pic.height(); i++)
		{
			int id = id(0, i);
			from[id] = start;
			dist[id] = energy(0, i);
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
				int cur = id(x, y);
				if (x + 1 >= pic.width())
				{
					double curCost = dist[cur];
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
						relax(cur, id(x + 1, y + 1), dist, from, lut[y + 1]);
					if (isInBound(x + 1, y))
						relax(cur, id(x + 1, y), dist, from, lut[y]);
					if (isInBound(x + 1, y - 1))
						relax(cur, id(x + 1, y - 1), dist, from, lut[y - 1]);
				}
			}
		}

		int[] seam = new int[pic.width()];
		int cur = endFrom;
		for (int i = pic.width() - 1; i >= 0; i--)
		{
			seam[i] = y(cur);
			cur = from[cur];
		}
		return seam;
	}

	public int[] findVerticalSeam() // sequence of indices for vertical seam
	{
		double endCost = Double.MAX_VALUE;
		double[] dist = new double[pic.height() * pic.width()];
		Arrays.fill(dist, Double.MAX_VALUE);

		int endFrom = 0;
		int[] from = new int[pic.height() * pic.width()];

		int start = id(-1, -1);
		// Point end = new Point(pic.width(), pic.height());

		for (int i = 0; i < pic.width(); i++)
		{
			int id = id(i, 0);
			from[id] = start;
			dist[id] = energy(i, 0);
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
				int cur = id(x, y);
				if (y + 1 >= pic.height())
				{
					double curCost = dist[cur];
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
						relax(cur, id(x + 1, y + 1), dist, from, lut[x + 1]);

					if (isInBound(x, y + 1))
						relax(cur, id(x, y + 1), dist, from, lut[x]);

					if (isInBound(x - 1, y + 1))
						relax(cur, id(x - 1, y + 1), dist, from, lut[x - 1]);
				}
			}
		}

		int[] seam = new int[pic.height()];
		int cur = endFrom;
		for (int i = pic.height() - 1; i >= 0; i--)
		{
			seam[i] = x(cur);
			cur = from[cur];
		}
		return seam;
	}
}