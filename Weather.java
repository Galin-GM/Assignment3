import java.util.ArrayList;
import java.util.Random;



/**
 * Write a description of class Weather here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Weather
{
    // instance variables - replace the example below with your own
    private ArrayList<String> typesOfWeather;
    
    private String currentWeather;
    
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        // initialise instance variables
        typesOfWeather = new ArrayList<>();
        typesOfWeather.add("Sunny");
        typesOfWeather.add("Raining");
        typesOfWeather.add("Drought");
        
        currentWeather = "Sunny";
    }
    
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setRandomWeather()
    {
        currentWeather = typesOfWeather.get(rand.nextInt(typesOfWeather.size()));
    }
    
    public String getCurrentWeather()
    {
        return currentWeather;
    }
}
