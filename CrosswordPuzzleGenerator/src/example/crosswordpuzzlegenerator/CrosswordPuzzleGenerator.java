package example.crosswordpuzzlegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrosswordPuzzleGenerator
{
    private List<String> usedWords = new ArrayList<String>();
    private List<CrosswordGrid> generatedGrids = new ArrayList<CrosswordGrid>();
    private Dictionary words = new Dictionary();
    final private static int ATTEMPTS_TO_FIT_WORDS = 5000;
    final private static int GRIDS_TO_MAKE = 70;

    public static void main( String[] args )
    {
        CrosswordPuzzleGenerator generator = new CrosswordPuzzleGenerator();
        generator.createCrosswordPuzzle();
    }

    private void createCrosswordPuzzle()
    {
        System.out.println( "Generating crossword puzzle..." );
        System.out.println();
        System.out.println("PROGRESS");
        System.out.println("########");
        int counter = 0;

        for (int gridsMade = 0; gridsMade < GRIDS_TO_MAKE; ++gridsMade)
        {
            CrosswordGrid grid = new CrosswordGrid();
            String word = getAWord();
            WordInfo wordInfo = new WordInfo( word, 0, 0, getRandomDirection() );
            grid.updateGrid( wordInfo );
            getUsedWords().add( word );
           // initializeGridWithWords( grid );

            for (int attempts = 0; attempts < ATTEMPTS_TO_FIT_WORDS; ++attempts)
            {
                attemptToPlaceWordOnGrid( grid, wordInfo );
            }

            getGeneratedGrids().add( grid );
            counter++;
            if(counter%10 == 0)
            {
                System.out.print("*");
            }
        }

        System.out.println();
        CrosswordGrid crosswordPuzzle = getBestGrid( getGeneratedGrids() );
        System.out.println( "Complete!" );
        System.out.println();
        crosswordPuzzle.display();
    }

    private void attemptToPlaceWordOnGrid( CrosswordGrid grid, WordInfo wordInfo )
    {
        String word;
        word = getUniqueRandomWord();
        for (int row = 0; row < CrosswordGrid.GRID_SIZE; ++row)
        {
            for (int column = 0; column < CrosswordGrid.GRID_SIZE; ++column)
            {
                wordInfo.setFields( word, row, column, getRandomDirection() );

                if ( grid.isLetter( row, column ) )
                {
                    if ( grid.updateGrid( wordInfo ) )
                    {
                        getUsedWords().add( wordInfo.getWord() );
                        word = getUniqueRandomWord();
                        wordInfo.setWord( word );
                    }
                }
            }
        }
    }

    private CrosswordGrid getBestGrid( List<CrosswordGrid> grids )
    {
        CrosswordGrid bestGrid = grids.get( 0 );
        for (CrosswordGrid grid : grids)
        {
            if ( grid.getIntersections() >= bestGrid.getIntersections() && grid.getLetterCount() > bestGrid.getLetterCount() )
            {
                bestGrid = grid;
            }
        }
        return bestGrid;
    }


    private String getUniqueRandomWord()
    {
        String word = getWords().getRandomWord();

        while ( getUsedWords().contains( word ) )
        {
            word = getWords().getRandomWord();
        }
        return word;
    }

    private WordInfo.Direction getRandomDirection()
    {
        Random randomGenerator = new Random();
        final int seed = 100;
        int random = randomGenerator.nextInt( seed );
        WordInfo.Direction direction;
        if ( random >= seed / 2 )
        {
            direction = WordInfo.Direction.horizontal;
        }
        else
        {
            direction = WordInfo.Direction.vertical;
        }
        return direction;
    }

    private String getAWord()
    {
        final int startingWordLength = 9;
        String word = getUniqueRandomWord();
        while ( word.length() < startingWordLength )
        {
            word = getUniqueRandomWord();
        }

        return word;
    }

    private void initializeGridWithWords( CrosswordGrid grid )
    {
        for (int row = 0; row < CrosswordGrid.GRID_SIZE; ++row)
        {
            int column = 0;
            WordInfo wordInfo = new WordInfo( getAWord(), row, column, WordInfo.Direction.horizontal );
           if( grid.updateGrid( wordInfo ))
           {
               getUsedWords().add( wordInfo.getWord() );
           }
        }
    }


    private Dictionary getWords()
    {
        return words;
    }

    private List<String> getUsedWords()
    {
        return usedWords;
    }

    private List<CrosswordGrid> getGeneratedGrids()
    {
        return generatedGrids;
    }
}