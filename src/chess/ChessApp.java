package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ChessApp extends Application
{
	static Board board;
	static Player currentPlayer;
	static Group root;
	static Pawn enPassantTarget;
	static ArrayList<TargetSquare> targetSquares;
	static int boardSize = 320;
	static int squareSize = boardSize / 8;
	static Piece currentPiece;
	static HashMap<Player, King> kings;
	static Stage stage;
	
	@Override
	public void start(Stage arg0) throws Exception 
	{
		root = new Group(); 
		stage = arg0;
		stage.setTitle("Chess");
		Scene scene = new Scene(root, boardSize, boardSize);

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				Rectangle rect = new Rectangle(
						(j%2==0 ? squareSize : 0) + squareSize * 2 * i, 
						squareSize * j, 
						squareSize, squareSize);
				rect.setFill(Color.BLANCHEDALMOND); //TODO: color scheme options
				root.getChildren().add(rect); 
			}
		}

		board = new Board();

		currentPlayer = Player.WHITE;		
		enPassantTarget = null;
		targetSquares = new ArrayList<TargetSquare>();
			
		arg0.setScene(scene);
		
		arg0.show();
		
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}

	public static void displayMoves(ArrayList<Position> moves) 
	{
		clearMoveDisplay(); 
				
		if(Objects.isNull(moves))
		{
			return;
		}
		
		for(Position move : moves)
		{
			TargetSquare target = new TargetSquare(move.getX(), move.getY());
			targetSquares.add(target);
		}
	}

	public static Player getCurrentPlayer() 
	{
		return currentPlayer;
	}

	public static void clearMoveDisplay()
	{
		if(!Objects.isNull(targetSquares))
		{
			for(TargetSquare target : targetSquares)
			{
				target.clear();
			}	

			targetSquares.clear();
		}
	}

	public static void setCurrentPiece(Piece piece) 
	{
		currentPiece = piece;
	}

	// Determine the end state of a particular move, then update the board to match.
	public static void movePiece(Position position)
	{
		Move move = board.determineMove(position, currentPiece);

		board = move.getResult();

		// Pawn Promotion
		if(currentPiece instanceof Pawn && position.getY() == currentPlayer.getOtherPlayer().getHomeRow())
		{
			Queen newQueen = new Queen(currentPlayer, position.getX(), position.getY());
			board.setPiece(position.getX(), position.getY(), newQueen);
			currentPiece.capture();
			// TODO: allow selection
		}
		
		if(move.hasCapturedPiece())
		{
			Piece capturedPiece = move.getCapturedPiece();
			capturedPiece.capture();
			board.addCapturedPiece(capturedPiece);
		}
		
		for(PieceMove pieceMove : move.getPieceMoves())
		{
			pieceMove.getPiece().moveTo(pieceMove.getEndpoint(), board);
		}
		
		clearCurrentPiece();
		board.checkForCheck(currentPlayer);
		endTurn();
	}

	public static void clearCurrentPiece()
	{
		clearMoveDisplay();
		currentPiece = null;
	}
	
	public static void endTurn()
	{
		currentPlayer = currentPlayer.getOtherPlayer();
	}

	public static int getSquareSize() {
		return squareSize;
	}

	public static void addElement(Node elem) 
	{
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() 
		    {
				root.getChildren().add(elem);
		    }
		});
	}
	
	public static void removeElement(Node elem) 
	{		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() 
		    {
		    	root.getChildren().remove(elem);
		    }
		});
		
	}

	// Determine whether a player's possible moves would place them in check, and disallow such moves.
	public static ArrayList<Position> checkLegality(ArrayList<Position> possibleMoves) 
	{
		ArrayList<Position> legalMoves = new ArrayList<Position>();
		
		for(Position moveTo : possibleMoves) 
		{
			Board resultBoard = board.determineMove(moveTo, currentPiece).getResult();
			
			Position kingPosition = resultBoard.getKingPosition(currentPlayer);	
			
			if(!resultBoard.isThreatened(kingPosition, currentPlayer.getOtherPlayer()))
			{
				legalMoves.add(moveTo);
			}
		}
	
		return legalMoves;
	}

	public static Board getBoard() 
	{
		return board;
	}
}
