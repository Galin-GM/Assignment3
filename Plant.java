
/**
 * A class representing shared characteristics of plant.
 *
 * @author Galin Mihaylov and Ricky Brown.
 */
public abstract class Plant extends Species
{
    /**
     * Create a new plant at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the plant is nocturnal.
     */
    public Plant(Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);        
    }
}
