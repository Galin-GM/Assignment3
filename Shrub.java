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
    // Characteristics shared by all antelopes (class variables).
    private static double NEW_SHRUB_PROBABILITY;    
    private static final int MAX_AGE = 8;
    private static final int SPAWNING_AGE = 4;
    private static final int MAX_LITTER_SIZE = 3;
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    
    // The shrub's age.
    private int age;
    
    /**
     * Create a new shrub. A shrub may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the shrub will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the animal is nocturnal.
     */
    public Shrub(boolean randomAge, Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    
    /**
     * This is what the shrub does most of the time - it grows new shrub. 
     * Sometimes it will breed or die of old age.
     * @param newShrub A list to return newly born Shrub.
     */
    public void act(List<Species> newShrub) 
    {
        incrementAge();
        if(isAlive()) {
            growPlants(newShrub);
        }
    }
    
    /**
     * Check whether or not this shrub is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newShrub A list to return newly born shrub.
     */
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
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    public int growth()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= NEW_SHRUB_PROBABILITY) 
        {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /**
     * A shrub can breed if it has reached the breeding age.
     * @return true if the shrub can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= SPAWNING_AGE;
    }
    
    /**
     * Increase the age.
     * This could result in the shrub's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Change the spawning probability of this shrub based on the current weather conditions.
     */
    static public void weatherInfluence(String currentWeather)
    {
        String weatherNow = currentWeather;
        switch(weatherNow) {
            case "Sunny":
                NEW_SHRUB_PROBABILITY = 0.15;
                break;
            case "Raining":
                NEW_SHRUB_PROBABILITY = 0.15;
                break;            
                
            default: NEW_SHRUB_PROBABILITY = 0.05;
        }
    }
}   
