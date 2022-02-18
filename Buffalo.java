import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a buffalo.
 * Buffalos age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Buffalo extends Animal
{
    // Characteristics shared by all buffalos (class variables).

    // The age at which a buffalo can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a buffalo can live.
    private static final int MAX_AGE = 80;
    // The likelihood of a buffalo breeding.
    private static final double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Initial plant food value
    private static final int PLANT_FOOD_VALUE = 10;
    
    // Initial antelope food level.
    private int foodLevel;
    
    // Individual characteristics (instance fields).
    
    // The buffalo's age.
    private int age;

    /**
     * Create a new buffalo. A buffalo may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the buffalo will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Buffalo(boolean randomAge, Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
    }
    
    /**
     * This is what the buffalo does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newBuffalos A list to return newly born buffalos.
     */
    public void act(List<Species> newBuffalos)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newBuffalos); 
            
            // Try to find food.
            Location newLocation = findFood();
            
            if (newLocation == null) {
                // If you cannot find food, try to move into a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the buffalo's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this buffalo is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBuffalo A list to return newly born buffalo.
     */
    private void giveBirth(List<Species> newBuffalos)
    {
        // New Antelopes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        
        while(it.hasNext()) {
            Object animal = field.getObjectAt(getLocation());
            Location where = it.next();
            Object nextAnimal = field.getObjectAt(where);
            
            if(nextAnimal instanceof Buffalo) {
                Buffalo buffalo = (Buffalo) animal;
                Buffalo nextBuffalo = (Buffalo) nextAnimal;
                if(buffalo.getSex() != nextBuffalo.getSex()) {
                    for(int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Buffalo young = new Buffalo(false, field, loc, false);
                        newBuffalos.add(young);
                    }   
                }
            }
        } 
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A antelope can breed if it has reached the breeding age.
     * @return true if the antelope can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            if(plant instanceof Shrub) {
                Shrub shrub = (Shrub) plant;
                if(shrub.isAlive()) { 
                    shrub.setDead();
                    foodLevel = PLANT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
}
