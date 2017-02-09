import java.awt.Color;

import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver
{
	private Picture pic;

	public SeamCarver(Picture picture) // create a seam carver object based on
										// the given picture
	{
		this.pic = picture;
	}

	public Picture picture() // current picture
	{
		return pic;
	}

	public int width() // width of current picture
	{
		return pic.width();
	}

	public int height() // height of current picture
	{
		return pic.height();
	}

	private double getDelta(Color c1, Color c2)
	{
		return Math.pow(c1.getRed() - c2.getRed(), 2) + Math.pow(c1.getBlue() - c2.getBlue(), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2);
	}

	private boolean isInBound(int x, int y)
	{
		return x >= 0 && x < pic.width() && y >= 0 && y < pic.height();
	}

	private boolean isInBound(Point p)
	{
		return isInBound(p.x, p.y);
	}

	public double energy(int x, int y) // energy of pixel at column x and row y
	{
		if (!isInBound(x, y))
		{
			throw new IndexOutOfBoundsException();
		}
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
		{
			return 1000.0D;
		}
		double dx = getDelta(pic.get(x - 1, y), pic.get(x + 1, y));
		double dy = getDelta(pic.get(x, y - 1), pic.get(x, y + 1));
		return Math.sqrt(dx + dy);
	}

	private void relax(Point cur, Point next, LinearProbingHashST<Point, Double> dist,
			LinearProbingHashST<Point, Point> from, double edgeWeight)
	{
		double curCost = dist.get(cur);
		Double oldCost = dist.get(next);
		if (oldCost == null || curCost + edgeWeight < oldCost)
		{
			dist.put(next, curCost + edgeWeight);
			from.put(next, cur);
		}
	}

	public int[] findHorizontalSeam() // sequence of indices for horizontal seam
	{
		LinearProbingHashST<Point, Double> dist = new LinearProbingHashST<Point, Double>();
		LinearProbingHashST<Point, Point> from = new LinearProbingHashST<Point, Point>();

		Point start = new Point(-1, -1);
		Point end = new Point(pic.width(), pic.height());

		for (int i = 0; i < pic.height(); i++)
		{
			Point p = new Point(0, i);
			from.put(p, start);
			dist.put(p, energy(p.x, p.y));
		}

		for (int x = 0; x < pic.width(); x++)
		{
			double[] lut = null;
			if (x + 1 < pic.width())
			{
				lut = new double[pic.height()];
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
					double curCost = dist.get(cur);
					Double oldCost = dist.get(end);
					if (oldCost == null || curCost < oldCost)
					{
						dist.put(end, curCost);
						from.put(end, cur);
					}
				} else
				{
					Point a = new Point(cur.x + 1, cur.y + 1);
					Point b = new Point(cur.x + 1, cur.y);
					Point c = new Point(cur.x + 1, cur.y - 1);
					if (isInBound(a))
					{
						relax(cur, a, dist, from, lut[a.y]);
					}
					if (isInBound(b))
					{
						relax(cur, b, dist, from, lut[b.y]);
					}
					if (isInBound(c))
					{
						relax(cur, c, dist, from, lut[c.y]);
					}
				}

			}
		}

		int[] seam = new int[pic.width()];
		Point cur = end;
		for (int i = pic.width() - 1; i >= 0; i--)
		{
			Point prev = from.get(cur);
			seam[i] = prev.y;
			cur = prev;
		}
		return seam;
	}

	public int[] findVerticalSeam() // sequence of indices for vertical seam
	{
		// Iterable<Point> topologicalOrder = verticalTopSort();
		LinearProbingHashST<Point, Double> dist = new LinearProbingHashST<Point, Double>();
		LinearProbingHashST<Point, Point> from = new LinearProbingHashST<Point, Point>();

		Point start = new Point(-1, -1);
		Point end = new Point(pic.width(), pic.height());

		for (int i = 0; i < pic.width(); i++)
		{
			Point p = new Point(i, 0);
			from.put(p, start);
			dist.put(p, energy(p.x, p.y));
		}

		for (int y = 0; y < pic.height(); y++)
		{
			double[] lut = null;
			if (y + 1 < pic.height())
			{
				lut = new double[pic.width()];
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
					double curCost = dist.get(cur);
					Double oldCost = dist.get(end);
					if (oldCost == null || curCost < oldCost)
					{
						dist.put(end, curCost);
						from.put(end, cur);
					}
				} else
				{
					if (isInBound(x - 1, y + 1))
					{
						relax(cur, new Point(cur.x - 1, cur.y + 1), dist, from, lut[x - 1]);
					}
					if (isInBound(x, y + 1))
					{
						relax(cur, new Point(cur.x, cur.y + 1), dist, from, lut[x]);
					}
					if (isInBound(x + 1, y + 1))
					{
						relax(cur, new Point(cur.x + 1, cur.y + 1), dist, from, lut[x + 1]);
					}
				}

			}
		}

		int[] seam = new int[pic.height()];
		Point cur = end;
		for (int i = pic.height() - 1; i >= 0; i--)
		{
			Point prev = from.get(cur);
			seam[i] = prev.x;
			cur = prev;
		}
		return seam;
	}

	public void removeHorizontalSeam(int[] seam) // remove horizontal seam from
													// current picture
	{
		Picture p = new Picture(pic.width(), pic.height() - 1);
		for (int x = 0; x < p.width(); x++)
		{
			int pivot = seam[x];
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
		Picture p = new Picture(pic.width() - 1, pic.height());
		for (int y = 0; y < p.height(); y++)
		{
			int pivot = seam[y];
			for (int x = 0; x < pivot; x++)
				p.set(x, y, pic.get(x, y));

			for (int x = pivot; x < p.width(); x++)
				p.set(x, y, pic.get(x + 1, y));
		}
		pic = p;
	}
}