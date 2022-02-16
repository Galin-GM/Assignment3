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
    private static final double NEW_SHRUB_PROBABILITY = 0.0001;
    private static final Random rand = Randomizer.getRandom();
    private int age;
    private static final int MAX_AGE = 12;
    private static final int SPAWNING_AGE = 10;
    private static final int MAX_LITTER_SIZE = 1;

    /**
     * Constructor for objects of class Shrub
     */
    public Shrub(Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        age = 0;
    }

    public void act(List<Species> newShrub) 
    {
        incrementAge();
        if(isAlive()) {
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                growPlants(newShrub);
            }
            else {
                setDead();
            }
        }
    }
    
    public void growPlants(List<Species> newShrub) 
    {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = free.iterator();
        int growths = growth();
        
        while(it.hasNext()) {
            for(int b = 0; b < growths && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Shrub youngShrub = new Shrub(field, loc, false);
                newShrub.add(youngShrub);
            }
        }
    }
    
    public int growth()
    {
        int births = 0;
        if(age > SPAWNING_AGE) //&& rand.nextDouble() <= NEW_SHRUB_PROBABILITY) 
        {
             births = MAX_LITTER_SIZE;
        }
        return births;
    }
    
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
}   
