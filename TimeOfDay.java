
/**
 * Write a description of class TimeOfDay here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TimeOfDay
{
    // instance variables - replace the example below with your own
    private boolean isDay;

    /**
     * Constructor for objects of class TimeOfDay
     */
    public TimeOfDay()
    {
        isDay = true;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public boolean isItDay()
    {
        return isDay;
    }
    
    public String dayOrNight()
    {
        if (isDay) {
            return "Day";
        }
        else {
            return "Night";
        }
    }
    
    public void flipTime() {
        isDay = !isDay;
    }
}
