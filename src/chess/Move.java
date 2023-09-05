package chess;

import java.util.ArrayList;


// A Move usually consists of one PieceMove (movement of a single piece from
// one position to another) as well as the resulting boardstate.
// It may include multiple PieceMoves (in the case of castling) 
// and may include the capture of a piece
public class Move 
{
	Board board;
	ArrayList<PieceMove> pieceMoves;
	Piece capturedPiece;
	
	
	public Move()
	{
		this.pieceMoves = new ArrayList<PieceMove>();
	}
	
	public Move(Board board, ArrayList<PieceMove> pieceMoves, Piece capturedPiece)
	{
		this.board = board;
		this.pieceMoves = pieceMoves;
		this.capturedPiece = capturedPiece;
	}

	public void addPieceMove(PieceMove pieceMove) 
	{
		pieceMoves.add(pieceMove);
	}

	public void setResult(Board resultBoard) 
	{
		board = resultBoard;
	}

	public void setCapturedPiece(Piece capturedPiece) 
	{
		this.capturedPiece = capturedPiece;
	}

	public boolean hasCapturedPiece() 
	{
		return capturedPiece != null;
	}

	public Piece getCapturedPiece() 
	{
		return capturedPiece;
	}

	public ArrayList<PieceMove> getPieceMoves() 
	{
		return pieceMoves;
	}
	
	public Board getResult()
	{
		return board;
	}
}
