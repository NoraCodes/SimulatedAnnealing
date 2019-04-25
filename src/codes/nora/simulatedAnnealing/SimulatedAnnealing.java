/**
 * This is an initial step at creating an abstract Simulated Annealing base class.
 * To implement simulated annealing for a specific problem, you should extend this
 * class, and then create code for each of the abstract methods shown.
 *
 * An instantiation of this class needs to implement the various abstract methods,
 * needs to hold a class variable representing the problem, AND needs to hold an
 * object variable representing the current solution to the problem. Methods of this
 * abstract class do not normally return instances of the problem solution, rather
 * they assume that the base class will alter the existing solution.
 *
 * The overall structure of the algorithm is to have two objects: The current
 * proposed solution, and a 'mutation' of that one (a small change in the solution).
 * We repeatedly: Create a mutation, evaluate whether we replace the old solution by
 * the new one, and if we do, then we replace the old solution data with the mutated
 * data solution.
 *
 * @author Darrah Chavey, Beloit College
 */

package codes.nora.simulatedAnnealing;

public abstract class SimulatedAnnealing {

    /** For simulated annealing, we need to know what the initial temperature
     *  should be. We assume a final temperature of 0.
     */
    protected static double startTemperature;

    /** We usually need to keep track of when the simulation began,
     *  and how much time we have been given to execute a simulation.
     */
    protected static long startTime;
    protected static double timeToRun;


    /** A default cooling curve to use for simulated annealing.
     * This should probably be overridden by a better cooling curve.
     * @return The current temperature of the annealing solution.
     */
    public static double getTemperature() {
        double theTemperature;
        long timeSpentSoFar = System.currentTimeMillis() - startTime;
        theTemperature = startTemperature - startTemperature*(timeSpentSoFar/timeToRun) ;
        return Math.max(theTemperature, 0);
    }


    /** A method to create the original problem to be analyzed. This should create
     *  a static representation of the problem, so that all instances have access
     *  to that data. Consequently, this method itself should be static.
     *
     *  This method must be overridden.
     */
    abstract protected void initializeProblem();


    /** Set the initial temperature of the problem search space.
     *  Set the amount of time that we have to run the algorithm.
     *  This should not be called until just before you're ready to run the algorithm,
     *  e.g. after initialization steps to reading in problem data, or other tasks
     *  needed to set and store the data for the problem.
     *
     *  This may need be overridden. For example, you might with to override the first
     *  line to set an initial temperature. (Usually, we want the initial temperature
     *  to be *just* large enough so that one solution will be able to jump to any
     *  other solution.)
     *
     * @param howLongToRunSimulation How long the algorithm should run, in seconds.
     */
    public static void initializeSetup( long howLongToRunSimulation ) {
        startTemperature = 500000;   // Half a million degrees
        timeToRun = howLongToRunSimulation*1000;	// Converts to a double
        startTime = System.currentTimeMillis();
    }


    /** Construct a random starting point for a solution, which will (presumably)
     *  be improved via Simulated Annealing.
     */
    abstract protected void randomize();


    /** Create a clone of the current solution, e.g. to mutate and compare with
     *  the original solution.
     */
    abstract protected SimulatedAnnealing clone();


    /** Once we've decided that the new solution is better than this one, we copy
     *  all of the data from that new solution into the old object. It accomplishes
     *  the equivalent of saying "this = newSolution", but in a safer manner.
     */
    abstract protected void copyData( SimulatedAnnealing toCopy );


    /** Mutate the current solution to another alternative.		 */
    abstract protected void mutate();


    /** When appropriate, do fast optimizations of the current solution.
     *  These should be the types of improvements to a solution that bring
     *  it to a local minimum in the solution space (hence the name).
     *  This method should run in time O(N).
     */
    abstract protected void localOptimization();


    /** Get the cost of the current solution. 					*/
    abstract protected double getCost();


    /** Based on the relative costs, and the current energy (temperature) of the
     *  situation, decide whether to accept the new solution as a replacement
     *  for the current solution.											*/
    abstract protected boolean acceptSolution( double costOfPrevious, double costOfNew);


    /** Create a String representation of the current solution. */
    abstract public String toString();


    /** Ideally, we should be able to structure this method so that it runs
     *  all of the simulated annealing structure itself. The child class should
     *  not need to know simulated annealing to use this.
     *
     *  NOTE: You will probably want a "main" in your child class that reads in, or
     *  constructs the problem data to work with, and then calls this method.
     *
     *  @param timeToRunSimulation how long, in seconds, should this run.
     */
    public void simulatedAnnealing( long timeToRunSimulation ) {
        // You are welcome to change this code if you need to, but try to capture
        // all of the crucial steps of the simulated annealing algorithm here, AND
        // Make sure that there is nothing here that presumes it knows what the
        // problem being solved actually is.
        SimulatedAnnealing possibleReplacement;
        double originalCost, replacementCost;

        initializeSetup( timeToRunSimulation );
        randomize();		// Get a (probably bad) solution to start from.
        while (getTemperature() > 0) {
            originalCost = getCost();     // Or, remember this value from the last loop.
            possibleReplacement = clone();
            possibleReplacement.mutate();
            possibleReplacement.localOptimization();
            replacementCost = possibleReplacement.getCost();
            if (acceptSolution( originalCost, replacementCost ))  {
                copyData( possibleReplacement );
            }
        }
    }
}
