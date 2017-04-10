import java.awt.image.BufferedImage;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class DPSeamCarver extends SeamCarver
{
	private int[] seam;

	// distance from source to vertex specified by index
	private int[] dist;

	// a map that traces back the shortest path
	private int[] from;

	private void initBuffers()
	{
		// +1 because we need the artificial end point
		dist = new int[width() * height() + 1];
		from = new int[width() * height() + 1];
		seam = new int[width()];
	}

	public DPSeamCarver(BufferedImage pic)
	{
		super(pic);
		initBuffers();
	}

	public DPSeamCarver(Picture pic)
	{
		super(pic);
		initBuffers();
	}

	public DPSeamCarver(SeamCarver s)
	{
		super(s);
		initBuffers();
	}

	private static void relax(int cur, int next, int[] dist, int[] from, int edgeWeight)
	{
		int curCost = dist[cur];
		int oldCost = dist[next];
		if (curCost + edgeWeight < oldCost)
		{
			dist[next] = curCost + edgeWeight;
			from[next] = cur;
		}
	}

	public int[] findHorizontalSeam() // sequence of indices for horizontal seam
	{
		final int H = height();
		final int W = width();

		// The artificial end point
		final int t = H * W;

		if (dist.length < t + 1)
			dist = new int[t + 1];
		else if (dist.length >> 1 > t + 1)
			dist = new int[t + 1];

		// int[] dist = new int[H * W + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);

		if (from.length < t + 1)
			from = new int[t + 1];
		else if (from.length >> 1 > t + 1)
			from = new int[t + 1];

		// An artificial starting point
		int start = id(-1, -1);

		for (int i = 0; i < H; i++)
		{
			int id = id(0, i);
			from[id] = start;
			dist[id] = energy(0, i);
		}

		int[] lut = new int[H];
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
		assert dist[t] >= 0;
		if (seam.length != W)
			seam = new int[W];
		// int[] seam = new int[W];
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
		final int H = height();
		final int W = width();

		// The artificial end point
		final int t = H * W;

		if (dist.length < t + 1)
			dist = new int[t + 1];
		else if (dist.length >> 1 > t + 1)
			dist = new int[t + 1];

		// int[] dist = new int[H * W + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);

		if (from.length < t + 1)
			from = new int[t + 1];
		else if (from.length >> 1 > t + 1)
			from = new int[t + 1];

		int start = id(-1, -1);

		for (int i = 0; i < W; i++)
		{
			int id = id(i, 0);
			from[id] = start;
			dist[id] = energy(i, 0);
		}

		int[] lut = new int[W];
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
		assert dist[t] >= 0;
		// Reconstruct the path
		if (seam.length != H)
			seam = new int[H];
		// int[] seam = new int[H];
		int cur = t;
		for (int i = H - 1; i >= 0; i--)
		{
			cur = from[cur];
			seam[i] = x(cur);
		}
		return seam;
	}
}