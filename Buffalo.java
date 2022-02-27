import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a buffalo.
 * Buffalos age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling and Galin Mihaylov and Ricky Brown
 * @version 2016.02.29 (2)
 */
public class Buffalo extends Animal
{
    // Characteristics shared by all buffalos (class variables).

    // The age at which a buffalo can start to breed.
    private static final int BREEDING_AGE = 5;
    // The likelihood of a buffalo breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Initial plant food value
    private static final int PLANT_FOOD_VALUE = 10;
 
    // Individual characteristics (instance fields).
    
    // The buffalo's age.
    private int age;
    // Initial antelope food level.
    private int foodLevel;
    // The age to which a buffalo can live.
    private int MAX_AGE;

    /**
     * Create a new buffalo. A buffalo may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the buffalo will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the animal is nocturnal.
     */
    public Buffalo(boolean randomAge, Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        MAX_AGE = ageMethod();
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
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
     * Look at surrounding antelopes and there is a possibility that 
     * the disease spreads.
     */
    private void spreadDisease()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        
        double probability = 0.05;
        
        
        while(it.hasNext()) {
            Object animal = field.getObjectAt(getLocation());
            Location where = it.next();
            Object nextAnimal = field.getObjectAt(where);
            
            if(nextAnimal instanceof Buffalo) {
                Buffalo buffalo = (Buffalo) animal;
                Buffalo nextBuffalo = (Buffalo) nextAnimal;
                
                if(buffalo.getIsDiseased() && !nextBuffalo.getIsDiseased()) {
                    if(rand.nextDouble() <= probability) {
                        nextBuffalo.setIsDiseased();
                        nextBuffalo.updateDiseasedMaxAge();
                    }
                }
            }
        }
    }
    
    /**
     * Update the max age.
     */
    private void updateDiseasedMaxAge() 
    {
        MAX_AGE = 18;
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
     * A buffalo can breed if it has reached the breeding age.
     * @return true if the buffalo can breed, false otherwise.
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
     * Change the max age of the buffalo depending on whether or not is it diseased.
     */
    private int ageMethod()
    {
        if(getIsDiseased()) {
            // Max age if diseased.
            MAX_AGE = 18;
        }
        else {
            // Max age if not diseased.
            MAX_AGE = 24;
        }
        return MAX_AGE;
    }
    
    /**
     * Change the breeding probability of this buffalo based on the current weather conditions.
     */
    static public void weatherInfluence(String currentWeather)
    {
        String weatherNow = currentWeather;
        switch(weatherNow) {
            case "Sunny":
                BREEDING_PROBABILITY = 0.17;
                break;
            case "Raining":
                BREEDING_PROBABILITY = 0.1;
                break;            
                
            default: BREEDING_PROBABILITY = 0.05;
        }
    }
}
