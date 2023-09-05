package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Board 
{
	Piece[][] pieces;
	HashMap<Player, King> kings;
	ArrayList<Piece> capturedPieces;
	Pawn enPassantTarget;
	
	
	public Board()
	{
		pieces = new Piece[8][8];
		capturedPieces = new ArrayList<Piece>();
		
		// TODO: alternate setups??

		for (Player player : Player.values())
		{
			int homeRow = player.getHomeRow();
			int pawnRow = player.getPawnRow();
			
			pieces[0][homeRow] = new Rook(player, 0, homeRow);
			pieces[1][homeRow] = new Knight(player, 1, homeRow);
			pieces[2][homeRow] = new Bishop(player, 2, homeRow);
			pieces[3][homeRow] = new Queen(player, 3, homeRow);
			pieces[4][homeRow] = new King(player, 4, homeRow);
			pieces[5][homeRow] = new Bishop(player, 5, homeRow);
			pieces[6][homeRow] = new Knight(player, 6, homeRow);
			pieces[7][homeRow] = new Rook(player, 7, homeRow);
			
			for(int i = 0; i < 8; i++)
			{
				pieces[i][pawnRow] = new Pawn(player, i, pawnRow);
			}
		}
	}
	
	// Make a copy of an existing board.
	public Board(Board original)
	{
		pieces = new Piece[8][8];
		
		for(int i = 0; i <=7; i++)
		{
			for(int j = 0; j <= 7; j++)
			{
				pieces[i][j] = original.getPiece(i, j);
			}
		}
		
		if(!Objects.isNull(original.capturedPieces))
		{
			capturedPieces = new ArrayList<Piece> (original.capturedPieces);
		}
		else
		{
			capturedPieces = new ArrayList<Piece>();
		}

		enPassantTarget = original.enPassantTarget;
	}
	
	
	public Position getKingPosition(Player player)
	{
		for(int i = 0; i <= 7; i++)
		{
			for(int j = 0; j <= 7; j++)
			{
				if(isOccupied(i, j))
				{
					Piece piece = getPiece(i, j);
					if(piece instanceof King && piece.getOwner() == player)
					{
						return new Position(i, j);
					}
				}
			}
		}
		return null;
	}
	
	
	public boolean isOccupied(int x, int y)
	{
		return !Objects.isNull(pieces[x][y]);
	}
	
	public boolean isOccupiedByPlayer(int x, int y, Player player)
	{
		Piece piece = pieces[x][y];
		return !Objects.isNull(piece) && piece.getOwner()==player;
	}
	
	public Piece getPiece(int x, int y)
	{
		return pieces[x][y];
	}

	public Board getCopy() 
	{
		return new Board(this);
	}
	
	public boolean isThreatened(Position square, Player player) 
	{
		ArrayList<Position> squareAsArray = new ArrayList<Position>();
		squareAsArray.add(square);

		return isThreatened(squareAsArray, player);
	}
	
	// Determine if particular spaces are under threat.
	// Used to determine if castling is possible & if king is in check.
	public boolean isThreatened(ArrayList<Position> squares, Player player) 
	{
		for(int i = 0; i <= 7; i++)
		{
			for(int j = 0; j <= 7; j++)
			{
				if(isOccupied(i, j))
				{
					Piece piece = getPiece(i, j);
					if(piece.getOwner() == player)
					{
						ArrayList<Position> threats = piece.getThreats(this); //TODO: ensure you exclude loop!!
						for(Position threat : threats)
						{
							for(Position square : squares)
							{
								if(threat.equals(square))
								{
									return true;
								}
							}
						}
						
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean mayCastleKingside(Player player) 
	{
		int homeRow = player.getHomeRow();
		
		Piece possibleKing = getPiece(4, homeRow); 
		Piece possibleRook = getPiece(7, homeRow); 

		if (possibleKing instanceof King && !possibleKing.hasMoved() 
		 && possibleRook instanceof Rook && !possibleRook.hasMoved()
		 && !isOccupied(5, homeRow) && !isOccupied(6, homeRow))
		{
			ArrayList<Position> squaresBetween = new ArrayList<Position>();
			squaresBetween.add(new Position(4, homeRow));
			squaresBetween.add(new Position(5, homeRow));
			squaresBetween.add(new Position(6, homeRow));
			squaresBetween.add(new Position(7, homeRow));
			
			if(!isThreatened(squaresBetween, player.getOtherPlayer()))
			{
				return true;
			}
		}
		
		return false;
	}

	public boolean mayCastleQueenside(Player player) 
	{
		int homeRow = player.getHomeRow();
		
		Piece possibleKing = getPiece(4, homeRow);
		Piece possibleRook = getPiece(0, homeRow);
		if (possibleKing instanceof King && !possibleKing.hasMoved() 
		 && possibleRook instanceof Rook && !possibleRook.hasMoved()
		 && !isOccupied(homeRow, 1) && !isOccupied(homeRow, 2) && !isOccupied(homeRow, 3))
		{
			ArrayList<Position> squaresBetween = new ArrayList<Position>();
			squaresBetween.add(new Position(0, homeRow));
			squaresBetween.add(new Position(1, homeRow));
			squaresBetween.add(new Position(2, homeRow));
			squaresBetween.add(new Position(3, homeRow));
			squaresBetween.add(new Position(4, homeRow));
			
			if(!isThreatened(squaresBetween, player.getOtherPlayer()))
			{
				return true;
			}
		}
		
		return false;	
	}
	
	public boolean mayCaptureEnPassant(int x, int y) 
	{
		return getPiece(x, y) == enPassantTarget;
	}
	
	// Determine the resulting boardstate after a specific move.
	public Move determineMove(Position moveTo, Piece piece) 
	{	
		Position initial = piece.getPosition();
		Player owner = piece.getOwner();
		boolean castlingKingside = false;
		boolean castlingQueenside = false;
	
		Move move = new Move();
		move.addPieceMove(new PieceMove(piece, initial, moveTo));
		
		Board resultBoard = this.getCopy();
		
		//Castling
		if(piece instanceof King 
				&& !piece.hasMoved()
				&& (moveTo.getX() == 6 || moveTo.getX() == 2))
		{
			if(moveTo.getX() == 6)
			{
				castlingKingside = true;
			}
			else if(moveTo.getX() == 2)
			{
				castlingQueenside = true;
			}
		}
		
		Piece targetPiece = getPiece(moveTo.getX(), moveTo.getY());
		
		//En Passant
		if(piece instanceof Pawn
				&& !Objects.isNull(enPassantTarget)
				&& moveTo.getX() == enPassantTarget.getPosition().getX() 
				&& moveTo.getY() == enPassantTarget.getPosition().getY() + 1 * piece.getOwner().forward())
		{
			move.setCapturedPiece(enPassantTarget);
			resultBoard.clearPiece(enPassantTarget.getPosition().getX(), enPassantTarget.getPosition().getY());
		}
		
		if(!Objects.isNull(targetPiece))
		{
			move.setCapturedPiece(targetPiece);
		}
		
		resultBoard.setPiece(moveTo.getX(), moveTo.getY(), piece);
		resultBoard.clearPiece(initial.getX(), initial.getY());
		
		if(castlingKingside)
		{
			int homeRow = owner.getHomeRow();
			Rook rook = (Rook) getPiece(7, homeRow);
			resultBoard.setPiece(5, homeRow, rook);
			resultBoard.clearPiece(7, homeRow);
			
			move.addPieceMove(new PieceMove(rook, new Position(7, homeRow), new Position(5, homeRow)));
		}
		else if(castlingQueenside)
		{
			int homeRow = owner.getHomeRow();
			Rook rook = (Rook) getPiece(0, homeRow);
			resultBoard.setPiece(3, homeRow, rook);
			resultBoard.clearPiece(0, homeRow);
			
			move.addPieceMove(new PieceMove(rook, new Position(0, homeRow), new Position(3, homeRow)));
		}
		
		move.setResult(resultBoard);
		return move;
	}
	
	public void setPiece(int x, int y, Piece piece)
	{
		pieces[x][y] = piece;
	}
	
	public void clearPiece(int x, int y)
	{
		pieces[x][y] = null;
	}
	
	public void addCapturedPiece(Piece piece)
	{
		capturedPieces.add(piece);
	}
	
	public void setEnPassantTarget(Pawn pawn)
	{
		enPassantTarget = pawn;
	}
	
	private void clearEnPassantTarget()
	{
		enPassantTarget = null;
	}

	public void checkForCheck(Player player) 
	{
		if(isThreatened(getKingPosition(player.getOtherPlayer()), player))
		{
			System.out.println("Check!");
			// TODO: check for checkmate here
		}		
	}
}

