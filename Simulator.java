import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;


/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing Lions, Tigers, Hyenas, Antelopes and Buffalos.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    
    // The probability that a lion will be created in any given grid position.
    private static final double LION_CREATION_PROBABILITY = 0.01;
    // The probability that a antelope will be created in any given grid position.
    private static final double ANTELOPE_CREATION_PROBABILITY = 0.02;
    // The probability that a buffalo will be created in any given grid position.
    private static final double BUFFALO_CREATION_PROBABILITY = 0.01; 
    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.02;
    // The probability that a hyena will be created in any given grid position.
    private static final double HYENA_CREATION_PROBABILITY = 0.04;
    
    private static final double SHRUB_CREATION_PROBABILITY = 0.02;


    // List of animals in the field.
    private List<Species> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // A tracker for the time of day.
    private TimeOfDay timeTracker;
    // A tracker for the weather.
    private Weather weatherTracker;
    

    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        animals = new ArrayList<>();
        field = new Field(depth, width);
        timeTracker = new TimeOfDay();
        weatherTracker = new Weather();
        
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        
        // Set prey colours.
        view.setColor(Antelope.class, Color.ORANGE);
        view.setColor(Buffalo.class, Color.BLACK);

        // Set predator colours
        view.setColor(Lion.class, Color.BLUE);
        view.setColor(Tiger.class, Color.RED);
        view.setColor(Hyena.class, Color.gray);
        
        view.setColor(Shrub.class, Color.GREEN);

        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(300);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        trackTime();

        // Provide space for newborn animals.
        List<Species> newAnimals = new ArrayList<>();        
        // Let all rabbits act.
        for(Iterator<Species> it = animals.iterator(); it.hasNext(); ) {
            
            Species animal = it.next();
            if(!animal.getIsNocturnal() & timeTracker.isItDay()) {
                animal.act(newAnimals);
            }
            else if(animal.getIsNocturnal() & !timeTracker.isItDay()) {
                animal.act(newAnimals);
            }
            
            if(! animal.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);

        view.showStatus(step, field, timeTracker.dayOrNight(), weatherTracker.getCurrentWeather());
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field, timeTracker.dayOrNight(), weatherTracker.getCurrentWeather());
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= LION_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Lion lion = new Lion(true, field, location, false);
                    animals.add(lion);
                }
                else if(rand.nextDouble() <= ANTELOPE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Antelope antelope = new Antelope(true, field, location, false);
                    animals.add(antelope);
                }

                else if(rand.nextDouble() <= BUFFALO_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Buffalo buffalo = new Buffalo(true, field, location, false);
                    animals.add(buffalo);
                }
                // else if(rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    // Location location = new Location(row, col);
                    // Tiger tiger = new Tiger(true, field, location, false);
                    // animals.add(tiger);

                // }
                // else if(rand.nextDouble() <= HYENA_CREATION_PROBABILITY) {
                    // Location location = new Location(row, col);
                    // Hyena hyena = new Hyena(true, field, location, true);
                    // animals.add(hyena);
                
                else if(rand.nextDouble() <= SHRUB_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Shrub shrub = new Shrub(field, location, false);
                    animals.add(shrub);
                }
                // }
                // else leave the location empty.
            }
        }
    }
    
    private void trackTime() {
        if (step % 24 == 0) {
            timeTracker.flipTime();
            weatherTracker.setRandomWeather();
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
