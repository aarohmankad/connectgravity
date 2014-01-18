package connect4;
import java.awt.*;

public class Piece {
    
    private Color color;
    private int value;
   
   Piece(Color _color)
   {
       color = _color;
   }
    
    public Color getColor()
    {
        return (color);
    }
    public void setColor(Color _color)
    {
        color = _color;
    }
    
    public int getValue()
    {
        return value;
    }
    public void setValue(int _value)
    {
        value = _value;
    }
}
