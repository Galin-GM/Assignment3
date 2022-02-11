import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public abstract class Animal
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    // Whether the animal is nocturnal or not.
    private boolean isNocturnal;
    // The sex of the animal.
    private char sex;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location, boolean isNocturnal)
    {
        alive = true;
        this.sex = generateSex();
        this.isNocturnal = isNocturnal;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Check whether this animal is a male or female.
     * @ return 'f' is this animal is a female and 'm' if this animal is a male.
     */
    protected char getSex() 
    {
        return sex;
    }
    
    protected char generateSex()
    {
        Random rand = Randomizer.getRandom();
        char sex = ' ';
        double probability = 0.5;
        if(rand.nextDouble() <= probability) {
            sex = 'f';
        }
        else {
            sex = 'm';
        }
        return sex;
    }
    
    /**
     * Check whether this animal is nocturnal or not.
     * @ return true if the animal is nocturnal.
     */
    protected boolean getIsNocturnal() {
        return isNocturnal;
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }
}
