import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MCSeamCarver extends SeamCarver
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

	public MCSeamCarver(BufferedImage pic)
	{
		super(pic);
		initBuffers();
	}

	@Override
	public int[] findHorizontalSeam()
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
				// Initialize the energy look up table for the next row
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

	@Override
	public int[] findVerticalSeam()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
