import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public abstract class SeamCarver
{
	protected Picture pic;

	public SeamCarver(Picture pic)
	{
		if (pic == null)
			throw new NullPointerException();
		this.pic = pic;
	}

	/**
	 * Turn Vertex into a unique id
	 * @param x x coordinate of the Vertex
	 * @param y y coordinate of the Vertex
	 * @return
	 */
	protected int id(int x, int y)
	{
		return x * pic.height() + y;
	}
	
	/**
	 * Turn a unique id back to x coordinate
	 * @param id
	 * @return
	 */
	protected int x(int id)
	{
		return id / pic.height();
	}

	/**
	 * Turn a unique id back to y coordinate
	 * @param id
	 * @return
	 */
	protected int y(int id)
	{
		return id % pic.height();
	}

	public Picture picture()
	{
		return pic;
	}

	public int width()
	{
		return pic.width();
	}

	public int height()
	{
		return pic.height();
	}

	protected boolean isInBound(int x, int y)
	{
		return x >= 0 && x < pic.width() && y >= 0 && y < pic.height();
	}

	private double getDelta(Color c1, Color c2)
	{
		return Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2);
	}

	public double energy(int x, int y) // energy of pixel at column x and row y
	{
		if (!isInBound(x, y))
			throw new IndexOutOfBoundsException();
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
			return 1000.0D;

		double dx = getDelta(pic.get(x - 1, y), pic.get(x + 1, y));
		double dy = getDelta(pic.get(x, y - 1), pic.get(x, y + 1));
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
		if (seam.length != pic.width())
			throw new IllegalArgumentException();

		Picture p = new Picture(pic.width(), pic.height() - 1);
		for (int x = 0; x < p.width(); x++)
		{
			int pivot = seam[x];
			if (x > 0 && Math.abs(seam[x - 1] - pivot) >= 2)
				throw new IllegalArgumentException();
			if (pivot < 0 || pivot >= pic.height())
				throw new IllegalArgumentException();

			for (int y = 0; y < pivot; y++)
				p.set(x, y, pic.get(x, y));

			for (int y = pivot; y < p.height(); y++)
				p.set(x, y, pic.get(x, y + 1));
		}
		pic = p;
	}

	public void removeVerticalSeam(int[] seam) // remove vertical seam from
	// current picture
	{
		if (seam.length != pic.height())
			throw new IllegalArgumentException();

		Picture p = new Picture(pic.width() - 1, pic.height());
		for (int y = 0; y < p.height(); y++)
		{
			int pivot = seam[y];
			if (y > 0 && Math.abs(seam[y - 1] - pivot) >= 2)
				throw new IllegalArgumentException();
			if (pivot < 0 || pivot >= pic.width())
				throw new IllegalArgumentException();

			for (int x = 0; x < pivot; x++)
				p.set(x, y, pic.get(x, y));

			for (int x = pivot; x < p.width(); x++)
				p.set(x, y, pic.get(x + 1, y));
		}
		pic = p;
	}
}