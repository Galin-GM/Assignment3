import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a hyena.
 * hyena age, move, eat buffalos and antelopes, and die.
 * 
 * @author David J. Barnes and Michael Kölling and Galin Mihaylov and Ricky Brown
 * @version 2016.02.29 (2)
 */
public class Hyena extends Animal
{
    // Characteristics shared by all hyena (class variables).
    
    // The age at which a hyena can start to breed.
    private static final int BREEDING_AGE = 10;
    // The likelihood of a hyena breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a hyena can go before it has to eat again.
    private static final int ANTELOPE_FOOD_VALUE = 32;
    private static final int BUFFALO_FOOD_VALUE = 32;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The hyena's age.
    private int age;
    // The hyena's food level, which is increased by eating rabbits.
    private int foodLevel;
     // The age to which a hyena can live.
    private int MAX_AGE = ageMethod();

    /**
     * Create a hyena. A hyena can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the hyena will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the animal is nocturnal.
     */
    public Hyena(boolean randomAge, Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(ANTELOPE_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = ANTELOPE_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the hyena does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newHyenas A list to return newly born hyena.
     */
    public void act(List<Species> newHyenas)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newHyenas);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
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
     * Increase the age. This could result in the hyena's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this hyena more hungry. This could result in the hyena's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Antelope){
                Antelope antelope = (Antelope) animal;
                if(antelope.isAlive()) { 
                    antelope.setDead();
                    foodLevel = ANTELOPE_FOOD_VALUE;
                    return where;
                }
            }
            else if(animal instanceof Buffalo) {
                Buffalo buffalo = (Buffalo) animal;
                if(buffalo.isAlive()) {
                    buffalo.setDead();
                    foodLevel = BUFFALO_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
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
            
            if(nextAnimal instanceof Hyena) {
                Hyena hyena = (Hyena) animal;
                Hyena nextHyena = (Hyena) nextAnimal;
                
                if(hyena.getIsDiseased() && !nextHyena.getIsDiseased()) {
                    if(rand.nextDouble() <= probability) {
                        nextHyena.setIsDiseased();
                        nextHyena.updateDiseasedMaxAge();
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
        MAX_AGE = 40;
    }
    
    /**
     * Check whether or not this hyena is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newHyenas A list to return newly born hyena.
     */
    private void giveBirth(List<Species> newHyenas)
    {
        // New hyenas are born into adjacent locations.
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
            
            if(nextAnimal instanceof Hyena) {
                Hyena hyena = (Hyena) animal;
                Hyena nextHyena = (Hyena) nextAnimal;
                if(hyena.getSex() != nextHyena.getSex()) {
                    for(int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Hyena young = new Hyena(false, field, loc, true);
                        newHyenas.add(young);
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
     * A hyena can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Change the max age of the hyena depending on whether or not is it diseased.
     */
    private int ageMethod()
    {
        if(getIsDiseased()) {
            // Max age if diseased.
            MAX_AGE = 30;
        }
        else {
            // Max age if not diseased.
            MAX_AGE = 48;
        }
        return MAX_AGE;
    }
    
    /**
     * Change the breeding probability of this hyena based on the current weather conditions.
     */
    static public void weatherInfluence(String currentWeather)
    {
        String weatherNow = currentWeather;
        switch(weatherNow) {
            case "Clear":
                BREEDING_PROBABILITY = 0.1;
                break;
            case "Raining":
                BREEDING_PROBABILITY = 0.1;
                break;
                
            default: BREEDING_PROBABILITY = 0.05;
        }
    }
}
