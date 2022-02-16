import java.util.List;
import java.util.Random;

/**
 * Write a description of class Species here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class Species
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    
    private boolean isNocturnal;

    /**
     * Constructor for objects of class Species
     */
    public Species(Field field, Location location, boolean isNocturnal)
    {
        this.field = field;
        setLocation(location);
        alive = true;
        this.isNocturnal = isNocturnal;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Species> newAnimal);
    
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    
     /**
     * Check whether this animal is nocturnal or not.
     * @ return true if the animal is nocturnal.
     */
    protected boolean getIsNocturnal() {
        return isNocturnal;
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