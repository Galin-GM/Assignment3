import java.util.ArrayList;
import java.util.Random;



/**
 * A class for tracking the weather.
 *
 * @author Galin Mihaylov and Ricky Brown
 */
public class Weather
{
    // Arrays that hold all the types of weather for day and night.
    private ArrayList<String> dayTypesOfWeather;
    private ArrayList<String> nightTypesOfWeather;
    
    // The current weather of the simulation.
    private String currentWeather;
    
    // Boolean used to flip betweening selecting daytime weather and night-time weather.
    private boolean flip;
    // A random number generator to select random position in array.
    private static final Random rand = Randomizer.getRandom();

    /**
     * Start the simulation as sunny.
     * Weather at which the simulation starts can be changed by altering the assignment to currentWeather.
     */
    public Weather()
    {
        // Create ArrayList object for day time weathers.
        dayTypesOfWeather = new ArrayList<>();
        // Add all types of day time weathers to the array list.
        dayTypesOfWeather.add("Sunny");
        dayTypesOfWeather.add("Raining");
        dayTypesOfWeather.add("Drought");
        
        // Create ArrayList object for night time weathers.
        nightTypesOfWeather = new ArrayList<>();
        // Add all types of night time weathers to the array list.
        nightTypesOfWeather.add("Clear");
        nightTypesOfWeather.add("Raining");
        nightTypesOfWeather.add("Drought");
        
        // Set the weather at which the simulation starts.
        currentWeather = "Sunny";
    }
    
    /**
     * Select a random weather type from the day or night array list.
     * Store the weather type selected in the currentWeather field.
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
    
    /**
     * Returns the current weather of the simulation.
     * @return the current weather of the simulation.
     */
    public String getCurrentWeather()
    {
        return currentWeather;
    }
}
