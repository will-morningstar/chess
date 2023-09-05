package chess;

import java.util.ArrayList;

public class Bishop extends Piece 
{

	public Bishop(Player player, int x, int y) 
	{
		super (player, x, y);
	}
	
	public int getIcon(Player player)
	{
		switch(player)
		{
		case WHITE:
			return 0x2657;
		case BLACK:
			return 0x265D;
		}
		return 0;	
	}

	@Override
	public ArrayList<Position> getMoves(Board board) 
	{
		return getDiagonals(board);
	}

}
