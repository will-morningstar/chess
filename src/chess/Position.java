package chess;

public class Position 
{
	int x, y;
	
	public Position(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setPos(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public boolean equals(Position other)
	{
		return (x == other.getX() && y == other.getY());
	}
}
