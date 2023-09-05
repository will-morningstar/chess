package chess;

import java.util.ArrayList;
import java.util.Objects;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

public abstract class Piece
{
	
	// When a player clicks on their piece during their turn,
	// we display all possible legal moves for that piece.
	protected class PieceClickHandler implements EventHandler<MouseEvent>
	{
		Piece piece; 
		
		PieceClickHandler(Piece piece)
		{
			this.piece = piece;
		}
		
		@Override
		public void handle(MouseEvent arg0) 
		{			
			if(owner == ChessApp.getCurrentPlayer())
			{
				ChessApp.setCurrentPiece(piece);

				ArrayList<Position> moves = getMoves();
				
				if(!Objects.isNull(moves))
				{
					moves = ChessApp.checkLegality(moves);	
				}
						
				ChessApp.displayMoves(moves);
			}	
			else
			{
				ChessApp.clearCurrentPiece();
			}
		}
	}
	
	protected int x, y;
	protected Player owner;
	protected boolean hasMoved;
	protected Text image;
		
	public Piece(Player owner, int x, int y)
	{
		this.owner = owner;
		this.x = x;
		this.y = y;
		hasMoved = false;
		
		int squareSize = ChessApp.getSquareSize();
		int icon = this.getIcon(owner);

		image = new Text(x * squareSize, y * squareSize, new String(Character.toChars(icon)));
		image.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, squareSize * 9/10));			
		image.setFill(Color.BLACK);
		image.setTextOrigin(VPos.TOP);
		image.setTextAlignment(TextAlignment.CENTER);
		image.setWrappingWidth(squareSize);
		
		image.addEventFilter(MouseEvent.MOUSE_CLICKED, getEventHandler());
		
		ChessApp.addElement(image);
	}
	
	abstract public ArrayList<Position> getMoves(Board board);
	
	public ArrayList<Position> getMoves()
	{
		return getMoves(ChessApp.getBoard());
	}
	
	public ArrayList<Position> getThreats(Board board)
	{
		return getMoves(board);
	}
	
	public void setPos(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getIcon(Player player)
	{
		return 0;
	}
	
	public void setImage(Text image)
	{
		this.image = image;
	}
	
	public Text getImage()
	{
		return image;
	}

	public EventHandler<MouseEvent> getEventHandler() 
	{
		return new PieceClickHandler(this);
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public boolean hasMoved()
	{
		return hasMoved;
	}
	
	// Get moves horizontally and vertically in the manner of a rook or queen. 
	protected ArrayList<Position> getCardinals(Board board)
	{
		ArrayList<Position> moves = new ArrayList<Position>();

		for(int i = x + 1; i <= 7; i++)
		{
			if(!board.isOccupied(i, y))
			{
				moves.add(new Position(i, y));
			}
			else if(board.isOccupiedByPlayer(i, y, owner.getOtherPlayer()))
			{
				moves.add(new Position(i, y));
				break;
			}
			else
			{
				break;
			}
		}

		for(int i = x - 1; i >= 0; i--)
		{
			if(!board.isOccupied(i, y))
			{
				moves.add(new Position(i, y));
			}
			else if(board.isOccupiedByPlayer(i, y, owner.getOtherPlayer()))
			{
				moves.add(new Position(i, y));
				break;
			}
			else
			{
				break;
			}
		}
		
		for(int j = y + 1; j <= 7; j++)
		{
			if(!board.isOccupied(x, j))
			{
				moves.add(new Position(x, j));
			}
			else if(board.isOccupiedByPlayer(x, j, owner.getOtherPlayer()))
			{
				moves.add(new Position(x, j));
				break;
			}
			else
			{
				break;
			}	
		}

		for(int j = y - 1; j >= 0; j--)
		{
			if(!board.isOccupied(x, j))
			{
				moves.add(new Position(x, j));
			}
			else if(board.isOccupiedByPlayer(x, j, owner.getOtherPlayer()))
			{
				moves.add(new Position(x, j));
				break;
			}
			else
			{
				break;
			}	
		}
	
		return moves;		
	}

	// Get moves diagonally in the manner of a bishop or queen. 
	protected ArrayList<Position> getDiagonals(Board board)
	{
		ArrayList<Position> moves = new ArrayList<Position>();
		
		for(int i = x + 1, j = y + 1; i <= 7 && j <= 7 ; i++, j++)
		{
			if(!board.isOccupied(i, j))
			{
				moves.add(new Position(i, j));
			}
			else if(board.isOccupiedByPlayer(i, j, owner.getOtherPlayer()))
			{
				moves.add(new Position(i, j));
				break;
			}
			else
			{
				break;
			}	
		}

		for(int i = x + 1, j = y - 1; i <= 7 && j >= 0 ; i++, j--)
		{
			if(!board.isOccupied(i, j))
			{
				moves.add(new Position(i, j));
			}
			else if(board.isOccupiedByPlayer(i, j, owner.getOtherPlayer()))
			{
				moves.add(new Position(i, j));
				break;
			}
			else
			{
				break;
			}	
		}
		
		for(int i = x - 1, j = y + 1; i >= 0 && j <= 7 ; i--, j++)
		{
			if(!board.isOccupied(i, j))
			{
				moves.add(new Position(i, j));
			}
			else if(board.isOccupiedByPlayer(i, j, owner.getOtherPlayer()))
			{
				moves.add(new Position(i, j));
				break;
			}
			else
			{
				break;
			}	
		}
		
		for(int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--)
		{
			if(!board.isOccupied(i, j))
			{
				moves.add(new Position(i, j));
			}
			else if(board.isOccupiedByPlayer(i, j, owner.getOtherPlayer()))
			{
				moves.add(new Position(i, j));
				break;
			}
			else
			{
				break;
			}	
		}
		
		return moves;
	}

	// Update the position of the piece's image
	public void moveTo(Position position, Board board) 
	{
		int xDiff = position.getX() - this.x;
		int yDiff = position.getY() - this.y;
		
		Translate translate = new Translate();
		int squareSize = ChessApp.getSquareSize();
		translate.setX(xDiff * squareSize);
		translate.setY(yDiff * squareSize);
		
		image.getTransforms().add(translate);
		
		this.x = position.getX();
		this.y = position.getY();		
		
		hasMoved = true;
	}

	public Position getPosition() 
	{
		return new Position(x, y);
	}

	public void capture() 
	{
		ChessApp.removeElement(image);
		System.out.println("captured");
	}	
}