package chess;

import java.util.ArrayList;

public class King extends Piece
{

	public King(Player player, int x, int y) 
	{
		super (player, x, y);
	}

	public int getIcon(Player player)
	{
		switch(player)
		{
		case WHITE:
			return 0x2654;
		case BLACK:
			return 0x265A;
		}
		return 0;	
	}

	// Kings can move to any adjacent square unoccupied by their own side's pieces.
	@Override
	public ArrayList<Position> getMoves(Board board) 
	{
		System.out.println("In king getMoves()");
		ArrayList<Position> moves = new ArrayList<Position>();
		moves.addAll(getThreats(board));
		
		// May castle if the king has not moved, the rook on a side has not moved,
		// the intervening space is empty, and no square involved is threatened by the opponent.
		if(board.mayCastleKingside(owner))
		{
			moves.add(new Position(6, owner.getHomeRow()));
		}
		
		if(board.mayCastleQueenside(owner))
		{
			moves.add(new Position(2, owner.getHomeRow()));
		}
		
		return moves;
	}
	
	public ArrayList<Position> getThreats(Board board)
	{
		ArrayList<Position> moves = new ArrayList<Position>();
		
		for(int i = Math.max(0,  x-1); i<= Math.min(7, x+1); i++)
		{
			for(int j = Math.max(0,  y-1); j <= Math.min(7, y+1); j++)
			{
				System.out.println("Checking king move " + i + "/" + j);
				
				if (!(x == i && y == j) && !board.isOccupiedByPlayer(i, j, owner))
				{
					System.out.println("Accepted");
					moves.add(new Position(i,j));
				}
			}
		}
				
		return moves;
	}


}
