
/**
 * A class for tracking the time of day.
 * It can either be day or night.
 *
 * @author Galin Mihaylov and Ricky Brown
 */
public class TimeOfDay
{
    // Whether it is daytime in the simulation.
    private boolean isDay;

    /**
     * Start the simulation at daytime.
     * If simulation is wanted to be started at night-time, change to false.
     */
    public TimeOfDay()
    {
        isDay = true;
    }

    /**
     * Check whether it is daytime or night-time 
     *
     * @return true if it is daytime, false if it is night-time.
     */
    public boolean isItDay()
    {
        return isDay;
    }
    
    /**
     * Return whether is it day or night, in String form.
     * Used to display whether it is day or night in the SimulatorView.
     * @return Whether it is day or night, in String form.
     */
    public String dayOrNight()
    {
        if (isDay) {
            return "Day";
        }
        else {
            return "Night";
        }
    }
    
    /**
     * Change the time of day.
     * If it is daytime flip to night-time.
     * If it is night-time flip to daytime.
     */
    public void flipTime() {
        isDay = !isDay;
    }
}
