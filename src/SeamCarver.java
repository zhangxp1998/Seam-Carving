import java.awt.image.BufferedImage;

import edu.princeton.cs.algs4.Picture;

public abstract class SeamCarver
{
	private int height;
	private int width;
	int[] rgb;

	public SeamCarver(BufferedImage pic)
	{
		if (pic == null)
			throw new NullPointerException();
		height = pic.getHeight();
		width = pic.getWidth();

		rgb = new int[pic.getHeight() * pic.getWidth() + 1];// +1
		pic.getRGB(0, 0, pic.getWidth(), pic.getHeight(), rgb, 0, pic.getWidth());
	}

	public SeamCarver(Picture pic)
	{
		this(pic.getBufferedImage());
	}

	protected int rgb(int x, int y)
	{
		return rgb[y * width + x];
	}

	/**
	 * Turn Vertex into a unique id
	 * 
	 * @param x
	 *            x coordinate of the Vertex
	 * @param y
	 *            y coordinate of the Vertex
	 * @return
	 */
	protected int id(int x, int y)
	{
		return y * width + x;
	}

	/**
	 * Turn a unique id back to x coordinate
	 * 
	 * @param id
	 * @return
	 */
	protected int x(int id)
	{
		return id % width;
	}

	/**
	 * Turn a unique id back to y coordinate
	 * 
	 * @param id
	 * @return
	 */
	protected int y(int id)
	{
		return id / width;
	}

	public Picture picture()
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, width, height, rgb, 0, width);
		return new Picture(bi);
	}

	public int width()
	{
		return width;
	}

	public int height()
	{
		return height;
	}

	protected boolean isInBound(int x, int y)
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	/**
	 * Return the red value of combined RGB
	 * 
	 * @param rgb
	 * @return
	 */
	public static int r(int rgb)
	{
		return (rgb >> 16) & 0xFF;
	}

	/**
	 * Return the green value of combined RGB
	 * 
	 * @param rgb
	 * @return
	 */
	public static int g(int rgb)
	{
		return (rgb >> 8) & 0xFF;
	}

	/**
	 * Return the blue value of combined RGB
	 * 
	 * @param rgb
	 * @return
	 */
	public static int b(int rgb)
	{
		return (rgb >> 0) & 0xFF;
	}

	public static int rgb(int r, int g, int b)
	{
		return (r << 16) | (g << 8) | b;
	}

	private static int pow(int base, int exp)
	{
		if (exp == 0)
			return 1;
		else if (exp == 1)
			return base;

		int p = base;
		int r = 1;
		while (exp > 1)
		{
			if ((exp & 1) == 1)
				r *= p;
			p *= p;
			exp >>= 1;
		}
		return p * r;
	}

	private static int getDelta(int c1, int c2)
	{
		return pow(r(c1) - r(c2), 2) + pow(g(c1) - g(c2), 2) + pow(b(c1) - b(c2), 2);
	}

	public int energy(int x, int y) // energy of pixel at column x and row y
	{
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
			return 1_000_000;

		int dx = getDelta(rgb(x - 1, y), rgb(x + 1, y));
		int dy = getDelta(rgb(x, y - 1), rgb(x, y + 1));
		return dx + dy;
	}

	public abstract int[] findHorizontalSeam(); // sequence of indices for
												// horizontal
	// seam

	public abstract int[] findVerticalSeam(); // sequence of indices for
												// vertical seam

	public void removeHorizontalSeam(int[] seam) // remove horizontal seam from
	// current picture
	{
		if (seam.length != width())
			throw new IllegalArgumentException();

		final int H = height() - 1;
		final int W = width();
		for (int x = 0; x < W; x++)
		{
			int pivot = seam[x];
			// if (x > 0 && Math.abs(seam[x - 1] - pivot) >= 2)
			// throw new IllegalArgumentException();
			// if (pivot < 0 || pivot >= height())
			// throw new IllegalArgumentException();

			for (int y = 0; y < pivot; y++)
				rgb[y * W + x] = rgb(x, y);

			for (int y = pivot; y < H; y++)
				rgb[y * W + x] = rgb(x, y + 1);
		}
		width = W;
		height = H;
	}

	public void removeVerticalSeam(int[] seam) // remove vertical seam from
	// current picture
	{
		if (seam.length != height())
			throw new IllegalArgumentException();

		final int H = height();
		final int W = width() - 1;
		for (int y = 0; y < H; y++)
		{
			int pivot = seam[y];
			// if (y > 0 && Math.abs(seam[y - 1] - pivot) >= 2)
			// throw new IllegalArgumentException();
			// if (pivot < 0 || pivot >= width())
			// throw new IllegalArgumentException();

			for (int x = 0; x < pivot; x++)
				rgb[y * W + x] = rgb(x, y);

			for (int x = pivot; x < W; x++)
				rgb[y * W + x] = rgb(x + 1, y);
		}
		width = W;
		height = H;
	}

	public int average(int x, int y)
	{
		int r = 0, g = 0, b = 0;
		int count = 0;
		for (int dx = -1; dx <= 1; dx++)
		{
			for (int dy = -1; dy <= 1; dy++)
			{
				// if (dx == 0 && dy == 0)
				// continue;
				if (!isInBound(x + dx, y + dy))
					continue;
				int color = rgb(x + dx, y + dy);
				r += r(color);
				g += g(color);
				b += b(color);
				count++;
			}
		}
		r = Math.min(r / count, 255);
		g = Math.min(b / count, 255);
		b = Math.min(g / count, 255);
		assert r >= 0;
		assert g >= 0;
		assert b >= 0;
		return rgb(r, g, b);
	}

	public void insertHorizontalSeam(int[] seam) // insert horizontal seam from
													// current picture
	{
		final int H = height() + 1;
		final int W = width();
		int[] tmp = rgb;
		// if (rgb.length < H * W)
		tmp = new int[H * W];

		for (int x = W - 1; x >= 0; x--)
		{
			int pivot = seam[x];
			// if (x > 0 && Math.abs(seam[x - 1] - pivot) >= 2)
			// throw new IllegalArgumentException();
			// if (pivot < 0 || pivot >= height())
			// throw new IllegalArgumentException();
			for (int y = H; y > pivot; y--)
				tmp[y * W + x] = rgb(x, y - 1);

			tmp[pivot * W + x] = average(x, pivot);

			for (int y = pivot - 1; y >= 0; y--)
				tmp[y * W + x] = rgb(x, y);
		}
		rgb = tmp;
		width = W;
		height = H;
	}

	public void insertVerticalSeam(int[] seam) // remove vertical seam from
												// current picture
	{
		final int H = height();
		final int W = width() + 1;
		int[] tmp = rgb;
		// if (rgb.length < H * W)
		tmp = new int[H * W];

		for (int y = H - 1; y >= 0; y--)
		{
			int pivot = seam[y];
			// if (x > 0 && Math.abs(seam[x - 1] - pivot) >= 2)
			// throw new IllegalArgumentException();
			// if (pivot < 0 || pivot >= height())
			// throw new IllegalArgumentException();
			for (int x = W - 1; x > pivot; x--)
				tmp[y * W + x] = rgb(x - 1, y);

			tmp[y * W + pivot] = average(pivot, y);

			for (int x = pivot - 1; x >= 0; x--)
				tmp[y * W + x] = rgb(x, y);
		}
		rgb = tmp;
		width = W;
		height = H;
	}
}