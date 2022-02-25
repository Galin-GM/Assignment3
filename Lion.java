import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a lion.
 * Lions age, move, eat antelopes and buffalos, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Lion extends Animal
{
    // Characteristics shared by all lions (class variables).
    
    // The age at which a lion can start to breed.
    private static final int BREEDING_AGE = 12;
    
    // The likelihood of a lion breeding.
    private static double BREEDING_PROBABILITY;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single antelope/buffalo. In effect, this is the
    // number of steps a lion can go before it has to eat again.
    private static final int ANTELOPE_FOOD_VALUE = 24;
    private static final int BUFFALO_FOOD_VALUE = 12;

    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The lion's age.
    private int age;
    // The lion's food level, which is increased by eating antelopes/buffalos.
    private int foodLevel;
    // The age to which a lion can live.
    private int MAX_AGE = ageMethod();

    /**
     * Create a lion. A lion can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the lion will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the animal is nocturnal.
     */
    public Lion(boolean randomAge, Field field, Location location, boolean isNocturnal)
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
     * This is what the lion does most of the time: it hunts for
     * antelopes and buffalo. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newLions A list to return newly born lions.
     */
    public void act(List<Species> newLions)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            spreadDisease();
            giveBirth(newLions);            
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
     * Increase the age. This could result in the lion's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this lion more hungry. This could result in the lion's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for antelopes/buffalos adjacent to the current location.
     * Can only eat under a certain amount of hunger.
     * Only the first live antelope/buffalo is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        if(foodLevel < (ANTELOPE_FOOD_VALUE * 0.7)) {
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
        }
        return null;
    }
    
    /**
     * Look at surrounding lions and there is a possibility that 
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
            
            if(nextAnimal instanceof Lion) {
                Lion lion = (Lion) animal;
                Lion nextLion = (Lion) nextAnimal;
                
                if(lion.getIsDiseased() && !nextLion.getIsDiseased()) {
                    if(rand.nextDouble() <= probability) {
                        nextLion.setIsDiseased();
                        nextLion.updateDiseasedMaxAge();
                    }
                }
            }
        }
    }
    
    /**
     * Check whether or not this lion is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newLions A list to return newly born lions.
     */
    private void giveBirth(List<Species> newLions)
    {
        // New lions are born into adjacent locations.
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
            
            if(nextAnimal instanceof Lion) {
                Lion lion = (Lion) animal;
                Lion nextLion = (Lion) nextAnimal;
                if(lion.getSex() != nextLion.getSex()) {
                    for(int b = 0; b < births && free.size() > 0; b++) {
                        Location loc = free.remove(0);
                        Lion young = new Lion(false, field, loc, false);
                        newLions.add(young);
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
     * A lion can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Change the max age of the lion depending on whether or not is it diseased.
     */
    private int ageMethod()
    {
        if(getIsDiseased()) {
            // Max age if diseased.
            MAX_AGE = 36;
        }
        else {
            // Max age if not diseased.
            MAX_AGE = 48;
        }
        return MAX_AGE;
    }
    
    /**
     * Update the max age.
     */
    private void updateDiseasedMaxAge() 
    {
        MAX_AGE = 36;
    }
    
    /**
     * Change the breeding probability of this lion based on the current weather conditions.
     */
    static public void weatherInfluence(String currentWeather)
    {
        String weatherNow = currentWeather;
        switch(weatherNow) {
            case "Sunny":
                BREEDING_PROBABILITY = 0.1;
                break;
            case "Raining":
                BREEDING_PROBABILITY = 0.2;
                break;            
                
            default: BREEDING_PROBABILITY = 0.05;
        }
    }
}