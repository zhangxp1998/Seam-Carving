import java.awt.image.BufferedImage;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class DPSeamCarving extends SeamCarver
{
	public DPSeamCarving(BufferedImage pic)
	{
		super(pic);
	}

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
		final int H = pic.getHeight();
		final int W = pic.getWidth();

		double[] dist = new double[H * W + 1];
		Arrays.fill(dist, Double.MAX_VALUE);

		// The artificial end point
		int t = H * W;

		int[] from = new int[H * W + 1];

		// An artificial starting point
		int start = id(-1, -1);

		for (int i = 0; i < H; i++)
		{
			int id = id(0, i);
			from[id] = start;
			dist[id] = energy(0, i);
		}

		double[] lut = new double[H];
		for (int x = 0; x < W; x++)
		{
			if (x + 1 < W)
			{
				// Initialize the energy look up table
				for (int y = 0; y < lut.length; y++)
				{
					lut[y] = energy(x + 1, y);
				}
			}

			for (int y = 0; y < H; y++)
			{
				int cur = id(x, y);
				if (x + 1 >= W)
				{
					relax(cur, t, dist, from, 0);
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

		int[] seam = new int[W];
		int cur = t;
		for (int i = W - 1; i >= 0; i--)
		{
			cur = from[cur];
			seam[i] = y(cur);
		}
		return seam;
	}

	public int[] findVerticalSeam() // sequence of indices for vertical seam
	{
		final int H = pic.getHeight();
		final int W = pic.getWidth();

		double[] dist = new double[H * W + 1];
		Arrays.fill(dist, Double.MAX_VALUE);

		// The artificial end point
		int t = H * W;

		int[] from = new int[H * W + 1];

		int start = id(-1, -1);

		for (int i = 0; i < W; i++)
		{
			int id = id(i, 0);
			from[id] = start;
			dist[id] = energy(i, 0);
		}

		double[] lut = new double[W];
		for (int y = 0; y < H; y++)
		{
			if (y + 1 < H)
			{
				for (int x = 0; x < lut.length; x++)
				{
					lut[x] = energy(x, y + 1);
				}
			}

			for (int x = 0; x < W; x++)
			{
				int cur = id(x, y);
				if (y + 1 >= H)
				{
					relax(cur, t, dist, from, 0);
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

		// Reconstruct the path
		int[] seam = new int[H];
		int cur = t;
		for (int i = H - 1; i >= 0; i--)
		{
			cur = from[cur];
			seam[i] = x(cur);
		}
		return seam;
	}
}