package example.crosswordpuzzlegenerator;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dictionary
{
    private static final int MAX_WORD_LENGTH = CrosswordGrid.GRID_SIZE;
    private static final int MIN_WORD_LENGTH = 3;
    private Map<String, String> dictionary;
    private List<String> words;

    private Random randomGenerator = new Random();
    private String filePath;

    public boolean isValid()
    {
        return (getWords() != null && getWords().size() > 0);
    }

    // todo denny
    // 1. don't catch the exception
    // 2. catch and log, then re-throw
    // 3. catch and eat, set valid if no exceptions
    public Dictionary()
    {
        configureFilePath();
        setDictionary( new HashMap<String, String>() );
        setWords( new ArrayList<String>() );

        try
        {
            BufferedReader reader = new BufferedReader( new FileReader( filePath ) );
            Scanner scanner = new Scanner( reader );
            Pattern pattern = Pattern.compile( "^[a-z]+$" );
            while ( scanner.hasNext() )
            {
                String line = scanner.next();
                Matcher matcher = pattern.matcher( line );
                if ( matcher.find() )
                {
                    String word = matcher.group();
                    if ( word.length() >= MIN_WORD_LENGTH && word.length() <= MAX_WORD_LENGTH && !getDictionary().containsKey( word ) )
                    {
                        getDictionary().put( word, null );
                        getWords().add( word );
                    }
                }
            }
        }
        catch (IOException e)
        {
            setWords( null );
        }
    }

    private void configureFilePath()
    {
        Properties properties = new Properties();
        InputStream input = null;

        try
        {

            input = new FileInputStream( "crosswordconfiguration.properties" );

            // load a properties file
            properties.load( input );

            // get the property value and print it out
            filePath = properties.getProperty( "dictionary" ) ;


        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isValidWord( String word )
    {
        return getDictionary().containsKey( word );
    }

    public String getRandomWord()
    {
        int random = getRandomGenerator().nextInt( getWords().size() );
        return getWords().get( random );
    }

    private Random getRandomGenerator()
    {
        return randomGenerator;
    }

    public List<String> getWords()
    {
        return words;
    }

    public void setWords( List<String> words )
    {
        this.words = words;
    }

    public Map<String, String> getDictionary()
    {
        return dictionary;
    }

    public void setDictionary( Map<String, String> dictionary )
    {
        this.dictionary = dictionary;
    }
}