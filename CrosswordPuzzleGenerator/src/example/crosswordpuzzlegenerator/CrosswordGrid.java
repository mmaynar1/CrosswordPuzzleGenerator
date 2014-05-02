package example.crosswordpuzzlegenerator;

public class CrosswordGrid
{
    public final static int GRID_SIZE = 20;
    private final static String EMPTY_CELL = "_ ";
    private final static String INVALID_DIRECTION = "Error: Invalid Direction";
    private final static String SPACE = " ";
    private String[][] grid;

    public CrosswordGrid()
    {
        grid = new String[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
            {
                grid[row][column] = EMPTY_CELL;
            }
        }
    }

    public Boolean updateGrid( WordInfo wordInfo )
    {
        Boolean gridUpdated = false;
        int row = wordInfo.getRow();
        int column = wordInfo.getColumn();

        if ( wordInfo.getDirection() == WordInfo.Direction.horizontal && canBePlacedHorizontally( wordInfo ) )
        {
            addWordToGrid( wordInfo, row, column );
            gridUpdated = true;
        }
        else if ( wordInfo.getDirection() == WordInfo.Direction.vertical && canBePlacedVertically( wordInfo ) )
        {
            addWordToGrid( wordInfo, row, column );
            gridUpdated = true;
        }

        return gridUpdated;
    }

    private void addWordToGrid( WordInfo wordInfo, int row, int column )
    {
        for (int letterIndex = 0; letterIndex < wordInfo.getWord().length(); ++letterIndex)
        {
            addLetter( wordInfo, row, column, letterIndex );
        }
    }

    private void addLetter( WordInfo wordInfo, int row, int column, int letterIndex )
    {
        if ( wordInfo.getDirection() == WordInfo.Direction.vertical )
        {
            row += letterIndex;
        }
        else
        {
            column += letterIndex;
        }

        getGrid()[row][column] = wordInfo.getWord().substring( letterIndex, letterIndex + 1 ) + SPACE;
    }

    public void display()
    {
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
            {
                System.out.print( getGrid()[row][column] );
            }
            System.out.println();
        }
        System.out.println();
    }

    public Boolean isLetter( int row, int column )
    {
        return (isValidPosition( row, column ) && Character.isLetter( getGrid()[row][column].charAt( 0 ) ));
    }

    public int getLetterCount()
    {
        int letterCount = 0;
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
            {
                if ( isLetter( row, column ) )
                {
                    ++letterCount;
                }
            }
        }
        return letterCount;
    }

    private Boolean isValidPosition( int row, int column )
    {
        return row >= 0 && row < GRID_SIZE && column >= 0 && column < GRID_SIZE;
    }

    private Boolean canBePlacedHorizontally( WordInfo wordInfo )
    {
        wordInfo.setDirection( WordInfo.Direction.horizontal );
        Boolean canBePlaced = true;
        int length = wordInfo.getWord().length();
        if ( isValidPosition( wordInfo.getRow(), wordInfo.getColumn() ) && wordInfo.getColumn() + length <= GRID_SIZE )
        {
            int row = wordInfo.getRow();
            int originalColumn = wordInfo.getColumn();
            int letterIndex = 0;
            while ( letterIndex < length )
            {
                int currentColumn = originalColumn + letterIndex;
                String gridIcon = getGridIcon( wordInfo.getWord(), letterIndex );
                if ( isSameCharacter( gridIcon, row, currentColumn ) || isEmptyCell( row, currentColumn ) )
                {
                    if ( isPlacementIllegal( wordInfo, row, currentColumn ) )
                    {
                        canBePlaced = false;
                    }
                }
                else
                {
                    canBePlaced = false;
                }
                ++letterIndex;
            }
        }
        else
        {
            canBePlaced = false;
        }
        return canBePlaced;
    }

    private Boolean canBePlacedVertically( WordInfo wordInfo )
    {
        wordInfo.setDirection( WordInfo.Direction.vertical );
        Boolean canBePlaced = true;
        int length = wordInfo.getWord().length();
        if ( isValidPosition( wordInfo.getRow(), wordInfo.getColumn() ) && wordInfo.getRow() + length <= GRID_SIZE )
        {
            int originalRow = wordInfo.getRow();
            int column = wordInfo.getColumn();
            int letterIndex = 0;
            while ( letterIndex < length )
            {
                int currentRow = originalRow + letterIndex;
                String gridIcon = getGridIcon( wordInfo.getWord(), letterIndex );
                if ( isSameCharacter( gridIcon, currentRow, column ) || isEmptyCell( currentRow, column ) )
                {
                    if ( isPlacementIllegal( wordInfo, currentRow, column ) )
                    {
                        canBePlaced = false;
                    }
                }
                else
                {
                    canBePlaced = false;
                }
                ++letterIndex;
            }
        }
        else
        {
            canBePlaced = false;
        }
        return canBePlaced;
    }

    private Boolean isPlacementIllegal( WordInfo wordInfo, int row, int column )
    {
        Boolean illegal;
        if ( wordInfo.getDirection() == WordInfo.Direction.horizontal )
        {
            illegal = isInterferenceAbove( row, column ) ||
                      isInterferenceBelow( row, column ) ||
                      isOverwritingHorizontalWord( row, column ) ||
                      isInvadingAnotherWordsSpace( wordInfo, row, column );
        }
        else if ( wordInfo.getDirection() == WordInfo.Direction.vertical )
        {
            illegal = isInterferenceRight( row, column ) ||
                      isInterferenceLeft( row, column ) ||
                      isOverwritingVerticalWord( row, column ) ||
                      isInvadingAnotherWordsSpace( wordInfo, row, column );
        }
        else
        {
            illegal = true;
            System.out.println( INVALID_DIRECTION );
        }
        return illegal;
    }


    private Boolean isInvadingAnotherWordsSpace( WordInfo wordInfo, int row, int column )
    {
        Boolean invading;
        if ( wordInfo.getDirection() == WordInfo.Direction.horizontal )
        {
            invading = (isEmptyCell( row, column ) &&
                        (isCharAbove( row, column ) || isCharBelow( row, column )) ||
                        ((isEndOfWord( wordInfo, row, column )) && (isCharRight( row, column ) || isCharAbove( row, column ) || isCharBelow( row, column ))));
        }
        else if ( wordInfo.getDirection() == WordInfo.Direction.vertical )
        {
            invading = (isEmptyCell( row, column ) &&
                        (isCharLeft( row, column ) || isCharRight( row, column )) ||
                        ((isEndOfWord( wordInfo, row, column )) && (isCharBelow( row, column ) || isCharRight( row, column ) || isCharLeft( row, column ))));
        }
        else
        {
            invading = true;
            System.out.println( INVALID_DIRECTION );
        }
        return invading;
    }

    private Boolean isOverwritingHorizontalWord( int row, int column )
    {
        int leftColumn = column - 1;
        return (isValidPosition( row, leftColumn ) && isLetter( row, column ) && isLetter( row, leftColumn ));
    }

    private Boolean isCharAbove( int row, int column )
    {
        int rowAbove = row - 1;
        return (isValidPosition( rowAbove, column ) && isLetter( rowAbove, column ));
    }

    private Boolean isCharBelow( int row, int column )
    {
        int rowAbove = row + 1;
        return (isValidPosition( rowAbove, column ) && isLetter( rowAbove, column ));
    }

    private Boolean isCharRight( int row, int column )
    {
        int columnRight = column + 1;
        return (isValidPosition( row, columnRight ) && isLetter( row, columnRight ));
    }

    private Boolean isCharLeft( int row, int column )
    {
        int columnLeft = column - 1;
        return (isValidPosition( row, columnLeft ) && isLetter( row, columnLeft ));
    }

    private Boolean isEndOfWord( WordInfo wordInfo, int currentRow, int currentColumn )
    {
        Boolean endOfWord = false;
        if ( wordInfo.getDirection() == WordInfo.Direction.horizontal )
        {
            if ( wordInfo.getColumn() + wordInfo.getWord().length() - 1 == currentColumn )
            {
                endOfWord = true;
            }
        }
        else if ( wordInfo.getDirection() == WordInfo.Direction.vertical )
        {
            if ( wordInfo.getRow() + wordInfo.getWord().length() - 1 == currentRow )
            {
                endOfWord = true;
            }
        }
        return endOfWord;
    }

    private Boolean canBePlaced( WordInfo wordInfo )
    {
        Boolean canBePlaced = false;
        if ( canBePlacedVertically( wordInfo ) || canBePlacedHorizontally( wordInfo ) )
        {
            canBePlaced = true;
        }
        return canBePlaced;
    }


    private Boolean isOverwritingVerticalWord( int row, int column )
    {
        int rowAbove = row - 1;
        return (isValidPosition( rowAbove, column ) && isLetter( row, column ) && isLetter( rowAbove, column ));
    }

    private Boolean isInterferenceRight( int row, int column )
    {
        return isInterference( row, column + 1, row + 1, column );
    }

    private Boolean isInterferenceLeft( int row, int column )
    {
        return isInterference( row, column - 1, row + 1, column );
    }

    private Boolean isInterferenceBelow( int row, int column )
    {
        return isInterference( row + 1, column, row, column + 1 );
    }

    private Boolean isInterferenceAbove( int row, int column )
    {
        return isInterference( row - 1, column, row, column + 1 );
    }

    private Boolean isInterference( int row, int column, int nextRow, int nextColumn )
    {
        return (isValidPosition( row, column )
                && isValidPosition( nextRow, nextColumn ) &&
                isLetter( row, column ) &&
                isLetter( nextRow, nextColumn ));
    }

    private String getGridIcon( String word, int index )
    {
        return word.substring( index, index + 1 ) + SPACE;
    }

    private Boolean isEmptyCell( int row, int column )
    {
        return isSameCharacter( EMPTY_CELL, row, column );
    }

    private Boolean isSameCharacter( String letter, int row, int column )
    {
        Boolean isSameCharacter = false;
        if ( getGrid()[row][column].equals( letter ) )
        {
            isSameCharacter = true;
        }
        return isSameCharacter;
    }

    public int getIntersections()
    {
        int intersections = 0;
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
            {
                if ( isLetter( row, column ) )
                {
                    if ( isValidPosition( row - 1, column ) &&
                         isValidPosition( row + 1, column ) &&
                         isValidPosition( row, column - 1 ) &&
                         isValidPosition( row, column + 1 ) &&
                         isLetter( row - 1, column ) &&
                         isLetter( row + 1, column ) &&
                         isLetter( row, column - 1 ) &&
                         isLetter( row, column + 1 ) )
                    {
                        ++intersections;
                    }
                }
            }
        }
        return intersections;
    }

    private String[][] getGrid()
    {
        return grid;
    }
}