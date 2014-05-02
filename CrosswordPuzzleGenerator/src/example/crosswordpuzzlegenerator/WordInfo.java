package example.crosswordpuzzlegenerator;

public class WordInfo
{
    public enum Direction
    {
        horizontal, vertical
    }

    private String word;
    private int row;
    private int column;
    private Direction direction;

    WordInfo( String word, int row, int column, Direction direction )
    {
        setFields( word, row, column, direction );
    }

    public String getWord()
    {
        return word;
    }

    public void setWord( String word )
    {
        this.word = word;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection( Direction direction )
    {
        this.direction = direction;
    }

    private void setRow( int row )
    {
        this.row = row;
    }

    private void setColumn( int column )
    {
        this.column = column;
    }

    public void setFields( String word, int row, int column, Direction direction )
    {
        setWord( word );
        setRow( row );
        setColumn( column );
        setDirection( direction );
    }
}