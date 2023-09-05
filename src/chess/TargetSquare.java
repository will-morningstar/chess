package chess;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// Highlighted square indicating a possible move for a selected piece.
public class TargetSquare 
{
	Position position;
	Rectangle rect;
	
	public TargetSquare(int x, int y) 
	{
		position = new Position(x, y);
		int squareSize = ChessApp.getSquareSize();
		
		rect = new Rectangle(x * squareSize, y * squareSize, squareSize, squareSize);
		rect.setFill(Color.RED);
		rect.opacityProperty().set(0.3); 
			
		rect.addEventFilter(MouseEvent.MOUSE_CLICKED, getEventHandler());

		ChessApp.addElement(rect);
	}
	
	protected class TargetSquareClickHandler implements EventHandler<MouseEvent>
	{
		Position position;
		TargetSquareClickHandler(Position pos)
		{
			this.position = pos;
		}
		
		@Override
		public void handle(MouseEvent arg0) 
		{			
			ChessApp.movePiece(position);
		}
	};

	public EventHandler<MouseEvent> getEventHandler() 
	{
		return new TargetSquareClickHandler(position);
	}

	public void clear()
	{
		ChessApp.removeElement(rect);
	}
}
