import java.util.List;
import java.util.Random;
import java.util.Iterator; 

/**
 * Write a description of class Shrub here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Shrub extends Plant
{
    // instance variables - replace the example below with your own
    private static double NEW_SHRUB_PROBABILITY = 0.1;
    private static final Random rand = Randomizer.getRandom();
    private int age;
    private static final int MAX_AGE = 20;
    private static final int SPAWNING_AGE = 10;
    private static final int MAX_LITTER_SIZE = 2;

    /**
     * Constructor for objects of class Shrub
     */
    public Shrub(boolean randomAge, Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    
    public void act(List<Species> newShrub) 
    {
        incrementAge();
        if(isAlive()) {
            growPlants(newShrub);
        }
    }
    
    public void growPlants(List<Species> newShrub) 
    {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = free.iterator();
        int growths = growth();
        
           for(int b = 0; b < growths && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Shrub youngShrub = new Shrub(true, field, loc, false);
            newShrub.add(youngShrub);
        }
    }
    
    public int growth()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= NEW_SHRUB_PROBABILITY) 
        {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    private boolean canBreed()
    {
        return age >= SPAWNING_AGE;
    }
    
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    static public void weatherInfluence(String currentWeather)
    {
        String weatherNow = currentWeather;
        switch(weatherNow) {
            case "Sunny":
                NEW_SHRUB_PROBABILITY = 0.1;
                break;
            case "Raining":
                NEW_SHRUB_PROBABILITY = 0.3;
                break;
            case "Drought":
                NEW_SHRUB_PROBABILITY = 0.3;
                break;
            case "Clear":
                NEW_SHRUB_PROBABILITY = 0.3;
                break;
            
                
            default: NEW_SHRUB_PROBABILITY = 0.05;
        }
    }
}   
