package chess;

import java.util.ArrayList;

public class Knight extends Piece 
{

	public Knight(Player player, int x, int y) 
	{
		super (player, x, y);
	}
	
	public int getIcon(Player player)
	{
		switch(player)
		{
		case WHITE:
			return 0x2658;
		case BLACK:
			return 0x265E;
		}
		return 0;	
	}

	// Knights may move to any space unoccupied by their own side's pieces that is 
	// exactly two squares away horizontally and one square away vertically 
	// or exactly exactly two squares away vertically and one square away horizontally
	@Override
	public ArrayList<Position> getMoves(Board board) 
	{
		ArrayList<Position> moves = new ArrayList<Position>();
		
		ArrayList<Position> potentialMoves = new ArrayList<Position>();
		
		potentialMoves.add(new Position(x + 1, y + 2));
		potentialMoves.add(new Position(x + 2, y + 1));
		potentialMoves.add(new Position(x - 1, y + 2));
		potentialMoves.add(new Position(x - 2, y + 1));
		potentialMoves.add(new Position(x + 1, y - 2));
		potentialMoves.add(new Position(x + 2, y - 1));
		potentialMoves.add(new Position(x - 1, y - 2));
		potentialMoves.add(new Position(x - 2, y - 1));
		
		for(Position potentialMove : potentialMoves)
		{
			int i = potentialMove.getX();
			int j = potentialMove.getY();

			if(i >= 0 && i <= 7 && j >= 0 && j <= 7 && !board.isOccupiedByPlayer(i, j, owner))
			{
				moves.add(new Position(i, j));
			}
		}
		
		return moves;
	}

}
