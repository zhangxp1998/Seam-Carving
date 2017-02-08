import java.awt.Color;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

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

	private Queue<Point> horizontalTopSort()
	{
		Queue<Point> topologicalOrder = new Queue<Point>();
		Stack<Point> stack = new Stack<Point>();
		LinearProbingHashSet<Point> visited = new LinearProbingHashSet<Point>();
		int height = pic.height();
		for (int i = 0; i < height; i++)
		{
			stack.push(new Point(0, i));
		}

		while (!stack.isEmpty())
		{
			Point cur = stack.peek();
			Point a = new Point(cur.x + 1, cur.y + 1);
			Point b = new Point(cur.x + 1, cur.y);
			Point c = new Point(cur.x + 1, cur.y - 1);

			if (isInBound(a) && !visited.contains(a))
			{
				stack.push(a);
				visited.add(a);
			} else if (isInBound(b) && !visited.contains(b))
			{
				stack.push(b);
				visited.add(b);
			} else if (isInBound(c) && !visited.contains(c))
			{
				stack.push(c);
				visited.add(c);
			} else
			{
				topologicalOrder.enqueue(stack.pop());
			}
		}
		return topologicalOrder;
	}

	public int[] findHorizontalSeam() // sequence of indices for horizontal seam
	{
		Queue<Point> topologicalOrder = horizontalTopSort();
		
	}

	public int[] findVerticalSeam() // sequence of indices for vertical seam
	{
	}

	public void removeHorizontalSeam(int[] seam) // remove horizontal seam from
													// current picture
	{
	}

	public void removeVerticalSeam(int[] seam) // remove vertical seam from
												// current picture
	{
	}
}