package chess;

public enum Player { 
	WHITE (7, 6, 3), 
	BLACK (0, 1, 4);
	
	private final int homeRow;
	private final int pawnRow;
	private final int enPassantRow;
	
	Player (int homeRow, int pawnRow, int enPassantRow)
	{
		this.homeRow = homeRow;
		this.pawnRow = pawnRow;
		this.enPassantRow = enPassantRow;
	}
	
	public int getHomeRow ()
	{
		return homeRow;
	}
	
	public int getPawnRow ()
	{
		return pawnRow;
	}

	public int getEnPassantRow ()
	{
		return enPassantRow;
	}
	
	public Player getOtherPlayer()
	{
		switch(this)
		{
		case BLACK:
			return WHITE;
		case WHITE:
			return BLACK;
		}
		return null;
	}
	
	public int forward()
	{
		switch(this)
		{
		case BLACK:
			return 1;
		case WHITE:
			return -1;
		}
		return 0;
	}


}
