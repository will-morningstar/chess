package chess;

import java.util.ArrayList;

public class Pawn extends Piece 
{
	public Pawn(Player owner, int x, int y) 
	{
		super (owner, x, y);
	}
	
	public int getIcon(Player player)
	{
		switch(player)
		{
		case WHITE:
			return 0x2659;
		case BLACK:
			return 0x265F;
		}
		return 0;	
	}

	// Pawns may move one space forward (if it is unoccupied). 
	// They may move two spaces on their first move of the game (if both are unoccupied). 
	// They cannot capture with these moves, but they may capture pieces one space forward and one to the side. 
	// If a pawn's initial move of two spaces puts them to the side of an opponent's pawn 
	// such that, had they moved only one space, they would be under threat by that pawn,
	// the opponent may capture them "en passant", by moving their pawn as though the first
	// pawn had only moved one space. 
	@Override
	public ArrayList<Position> getMoves(Board board) 
	{		
		ArrayList<Position> moves = new ArrayList<Position>();

		int forward = owner.forward();
		
		if(!board.isOccupied(x, y + 1 * forward))
			{
			moves.add(new Position(x, y + 1 * forward));
			
			if(!hasMoved && !board.isOccupied(x, y + 2 * forward))
			{
				moves.add(new Position(x, y + 2 * forward));				
			}
		}
		
		if( x > 0 && board.isOccupiedByPlayer(x - 1, y + 1 * forward, owner.getOtherPlayer()))
		{
			moves.add(new Position(x - 1, y + 1 * forward));
		}
		
		if( x < 7 && board.isOccupiedByPlayer(x + 1, y + 1 * forward, owner.getOtherPlayer()))
		{
			moves.add(new Position(x + 1, y + 1 * forward));
		}
		
		// En Passant 
		if(y == owner.getEnPassantRow())
		{
			if(x > 0 && board.mayCaptureEnPassant(x - 1, y))
			{
				moves.add(new Position(x - 1, y + 1 * forward));
			}

			if(x < 0 && board.mayCaptureEnPassant(x + 1, y))
			{
				moves.add(new Position(x - 1, y + 1 * forward));
			}
		}
	
		return moves;
	}
	
	public ArrayList<Position> getThreats(Board board)
	{
		ArrayList<Position> threats = new ArrayList<Position>();
		
		int forward = owner.forward();
		
		if( x > 0 && !board.isOccupiedByPlayer(x - 1, y + 1 * forward, owner))
		{
			threats.add(new Position(x - 1, y + 1 * forward));
		}
		
		if( x < 7 && !board.isOccupiedByPlayer(x + 1, y + 1 * forward, owner))
		{
			threats.add(new Position(x - 1, y + 1 * forward));
		}
		
		if(y == owner.getEnPassantRow())
		{
			if(x > 0 && board.mayCaptureEnPassant(x - 1, y))
			{
				threats.add(new Position(x - 1, y + 1 * forward));
			}

			if(x < 0 && board.mayCaptureEnPassant(x + 1, y))
			{
				threats.add(new Position(x - 1, y + 1 * forward));
			}
		}
		
		return threats;
	}
	
	@Override
	public void moveTo(Position target, Board board)
	{			
		if(!hasMoved && target.getY() == owner.getOtherPlayer().getEnPassantRow())
		{
			board.setEnPassantTarget(this);
		}
		
		super.moveTo(target, board);
	}
	
}
