package chess;

import java.util.ArrayList;

public class Rook extends Piece
{	
	public Rook(Player player, int x, int y) 
	{
		super(player, x, y);
	}

	public int getIcon(Player player)
	{
		switch(player)
		{
		case WHITE:
			return 0x2656;
		case BLACK:
			return 0x265C;
		}
		return 0;	
	}
	
	@Override
	public ArrayList<Position> getMoves(Board board) 
	{	
		return getCardinals(board);
	}

}
