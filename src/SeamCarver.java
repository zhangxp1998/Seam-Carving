import java.awt.image.BufferedImage;

import edu.princeton.cs.algs4.Picture;

public abstract class SeamCarver
{
	protected BufferedImage pic;

	public SeamCarver(BufferedImage pic)
	{
		if (pic == null)
			throw new NullPointerException();
		this.pic = pic;
	}
	
	public SeamCarver(Picture pic)
	{
		if (pic == null)
			throw new NullPointerException();
		this.pic = pic.getBufferedImage();
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
		return x * pic.getHeight() + y;
	}

	/**
	 * Turn a unique id back to x coordinate
	 * 
	 * @param id
	 * @return
	 */
	protected int x(int id)
	{
		return id / pic.getHeight();
	}

	/**
	 * Turn a unique id back to y coordinate
	 * 
	 * @param id
	 * @return
	 */
	protected int y(int id)
	{
		return id % pic.getHeight();
	}

	public Picture picture()
	{
		return new Picture(pic);
	}

	public int width()
	{
		return pic.getWidth();
	}

	public int height()
	{
		return pic.getHeight();
	}

	protected boolean isInBound(int x, int y)
	{
		return x >= 0 && x < pic.getWidth() && y >= 0 && y < pic.getHeight();
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

		double dx = getDelta(pic.getRGB(x - 1, y), pic.getRGB(x + 1, y));
		double dy = getDelta(pic.getRGB(x, y - 1), pic.getRGB(x, y + 1));
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
		if (seam.length != pic.getWidth())
			throw new IllegalArgumentException();

		BufferedImage p = new BufferedImage(pic.getWidth() - 1, pic.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < p.getWidth(); x++)
		{
			int pivot = seam[x];
			if (x > 0 && Math.abs(seam[x - 1] - pivot) >= 2)
				throw new IllegalArgumentException();
			if (pivot < 0 || pivot >= pic.getHeight())
				throw new IllegalArgumentException();

			for (int y = 0; y < pivot; y++)
				p.setRGB(x, y, pic.getRGB(x, y));

			for (int y = pivot; y < p.getHeight(); y++)
				p.setRGB(x, y, pic.getRGB(x, y + 1));
		}
		pic = p;
	}

	public void removeVerticalSeam(int[] seam) // remove vertical seam from
	// current picture
	{
		if (seam.length != pic.getHeight())
			throw new IllegalArgumentException();

		BufferedImage p = new BufferedImage(pic.getWidth() - 1, pic.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < p.getHeight(); y++)
		{
			int pivot = seam[y];
			if (y > 0 && Math.abs(seam[y - 1] - pivot) >= 2)
				throw new IllegalArgumentException();
			if (pivot < 0 || pivot >= pic.getWidth())
				throw new IllegalArgumentException();

			for (int x = 0; x < pivot; x++)
				p.setRGB(x, y, pic.getRGB(x, y));

			for (int x = pivot; x < p.getWidth(); x++)
				p.setRGB(x, y, pic.getRGB(x + 1, y));
		}
		pic = p;
	}
}