
public class Point
{
	public final int x;
	public final int y;
	private final int hash;
	private String rep;

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
		hash = x << 16 | y;
	}

	@Override
	public int hashCode()
	{
		return hash;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Point)
		{
			Point p = (Point) o;
			return p.x == x && p.y == y;
		}
		return false;
	}

	@Override
	public String toString()
	{
		if (rep == null)
		{
			rep = String.format("(%d, %d)", x, y);
		}
		return rep;
	}
}
