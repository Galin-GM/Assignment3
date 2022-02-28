import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling and Galin Mihaylov and Ricky Brown
 * @version 2016.02.29 (2)
 */
public abstract class Animal extends Species
{
    // The sex of the animal.
    private char sex;

    // If the animal is diseased.
    private boolean isDiseased;

    private static final Random rand = Randomizer.getRandom();

    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isNocturnal If true, the animal is nocturnal.
     */
    public Animal(Field field, Location location, boolean isNocturnal)
    {
        super(field, location, isNocturnal);
        this.sex = generateSex();
        this.isDiseased = generateIsDiseased();
    }

    /**
     * Check whether this animal is a male or female.
     * @return 'f' is this animal is a female and 'm' if this animal is a male.
     */
    protected char getSex() 
    {
        return sex;
    }

    /**
     * Randomly generate a sex for this animal.
     * @return the sex that is randomly generated for the animal.
     */
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
     * Return whether the animal is diseased.
     * @return true if the animal is diseased
     */
    protected boolean getIsDiseased()
    {
        return isDiseased;
    }

    /**
     * Generate whether the animal is diseased.
     * @return true if the animal is diseased
     */
    protected boolean generateIsDiseased()
    {
        double probability = 0.1;

        if(rand.nextDouble() <= probability) {
            isDiseased = true;
        }
        else {
            isDiseased = false;
        }
        return isDiseased;
    }

    /**
     * Set the animal to diseased.
     */
    protected void setIsDiseased()
    {
        isDiseased = true;
    }

}
