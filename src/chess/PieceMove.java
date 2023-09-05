package chess;

// The movement of a single piece. Usually only one per "Move", 
// but castling involves two PieceMoves
public class PieceMove 
{
	Piece piece;
	Position initial;
	Position endpoint;
	
	public PieceMove(Piece piece, Position initial, Position endpoint)
	{
		this.piece = piece;
		this.initial = initial;
		this.endpoint = endpoint;
	}

	public Piece getPiece() 
	{
		return piece;
	}

	public Position getEndpoint() 
	{
		return endpoint;
	}
}
