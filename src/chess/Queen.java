package chess;

import java.util.ArrayList;

public class Queen extends Piece
{

	public Queen(Player player, int x, int y) 
	{
		super (player, x, y);
	}
	
	public int getIcon(Player player)
	{
		switch(player)
		{
		case WHITE:
			return 0x2655;
		case BLACK:
			return 0x265B;
		}
		return 0;	
	}

	@Override
	public ArrayList<Position> getMoves(Board board) 
	{
		ArrayList<Position> moves = getCardinals(board);
		moves.addAll(getDiagonals(board));
		return moves;
	}

}
