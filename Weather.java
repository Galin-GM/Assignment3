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
    private ArrayList<String> dayTypesOfWeather;
    private ArrayList<String> nightTypesOfWeather;
    
    private String currentWeather;
    
    private boolean flip;
    
    private static final Random rand = Randomizer.getRandom();

    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        // initialise instance variables
        dayTypesOfWeather = new ArrayList<>();
        dayTypesOfWeather.add("Sunny");
        dayTypesOfWeather.add("Raining");
        dayTypesOfWeather.add("Drought");
        
        nightTypesOfWeather = new ArrayList<>();
        nightTypesOfWeather.add("Clear");
        nightTypesOfWeather.add("Raining");
        nightTypesOfWeather.add("Drought");
        
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
        if(flip) {
            currentWeather = dayTypesOfWeather.get(rand.nextInt(dayTypesOfWeather.size()));
            flip = !flip;
        }
        else {
            currentWeather = nightTypesOfWeather.get(rand.nextInt(nightTypesOfWeather.size()));
            flip = !flip;
        }
    }
    
    public String getCurrentWeather()
    {
        return currentWeather;
    }
}
