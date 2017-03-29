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

		rgb = new int[pic.getHeight() * pic.getWidth()];
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
		return x * height + y;
	}

	/**
	 * Turn a unique id back to x coordinate
	 * 
	 * @param id
	 * @return
	 */
	protected int x(int id)
	{
		return id / height;
	}

	/**
	 * Turn a unique id back to y coordinate
	 * 
	 * @param id
	 * @return
	 */
	protected int y(int id)
	{
		return id % height;
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

	private double getDelta(int c1, int c2)
	{
		return Math.pow(r(c1) - r(c2), 2) + Math.pow(g(c1) - g(c2), 2) + Math.pow(b(c1) - b(c2), 2);
	}

	public double energy(int x, int y) // energy of pixel at column x and row y
	{
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
			return 1000.0D;

		double dx = getDelta(rgb(x - 1, y), rgb(x + 1, y));
		double dy = getDelta(rgb(x, y - 1), rgb(x, y + 1));
		return Math.sqrt(dx + dy);
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

		final int H = height();
		final int W = width() - 1;
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
				rgb[y * W + x] = rgb(x, y+1);
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
}