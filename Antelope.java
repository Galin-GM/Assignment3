import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a antelope.
 * Antelopes age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling and Galin Mihaylov and Ricky Brown.
 * @version 2016.02.29 (2)
 */
public class Antelope extends Animal
{
    // Characteristics shared by all antelopes (class variables).

    // The age at which a antelope can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a antelope can live.
    private static final int MAX_AGE = 70;
    // The likelihood of a antelope breeding.
    private static double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // Initial plant food value.
    private static final int PLANT_FOOD_VALUE = 10;
    
    
    // Individual characteristics (instance fields).
    
    // The antelope's age.
    private int age;
    // Initial antelope food level.
    private int foodLevel;

    /**
     * Create a new antelope. A antelope may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the antelope will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the animal is nocturnal.
     */
    public Antelope(boolean randomAge, Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
    }
    
    /**
     * This is what the antelope does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newAntelopes A list to return newly born Antelopes.
     */
    public void act(List<Species> newAntelopes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newAntelopes); 
            
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
     * This could result in the Antelopes's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this antelopes is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newAntelopes A list to return newly born antelopes.
     */
    private void giveBirth(List<Species> newAntelopes)
    {
        // New Antelopes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        // Get a list of adjacent locations.
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        int births = breed();
        
        while(it.hasNext()) {
            Object animal = field.getObjectAt(getLocation());
            Location where = it.next();
            Object nextAnimal = field.getObjectAt(where);
            
            if(nextAnimal instanceof Antelope) {
                Antelope antelope = (Antelope) animal;
                Antelope nextAntelope = (Antelope) nextAnimal;
                if(antelope.getSex() != nextAntelope.getSex()) {
                    for(int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Antelope young = new Antelope(false, field, loc, false);
                        newAntelopes.add(young);
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
    
    /**
     * Look for shrub adjacent to the current location.
     * Only the first live shrub is eaten.
     * @return Where food was found, or null if it wasn't.
     */
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
    
    /**
     * Make this antelope more hungry. This could result in the antelope's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Change the breeding probability of this antelope based on the current weather conditions.
     */
    static public void weatherInfluence(String currentWeather)
    {
        String weatherNow = currentWeather;
        switch(weatherNow) {
            case "Sunny":
                BREEDING_PROBABILITY = 0.1;
                break;
            case "Raining":
                BREEDING_PROBABILITY = 0.3;
                break;
            case "Drought":
                BREEDING_PROBABILITY = 0.3;
                break;
            case "Clear":
                BREEDING_PROBABILITY = 0.3;
                break;
            
                
            default: BREEDING_PROBABILITY = 0.05;
        }
    }
}
