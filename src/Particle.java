import java.util.Random;

/**
 * A particle for generating a sequence of 8 chords(for more efficiency and simplisity)
 * It generates sequence of numbers randomly from MIDILOWERBOUND and MIDIUPPERBOUND(in one octave)
 * <p>
 * Created by Ekaterina Yashkina on 22-10-2017.
 */
public class Particle {
    private int POSITIONS;//number of chords * amount of notes in a chord
    private int MIDILOWERBOUND;//smallest possible midi value that can be generated
    private int MIDIUPPERBOUND;//biggest  possible midi value that can be generated
    private double[] velocity; //keeps velocities of every number to be use din PSO
    private int[] position;//keeps current positions of particles
    private double cost;//cost of particle according to fitness function
    private int tonality;//generated tonality of particle
    private boolean major;//is tonality major - 1 or minor - 0
    private int[] steps;//steps of tonality - notes that are available for given tonality
    private double bestCost;//best cost of a given particle over all history
    private int[] bestPosition;

    public Particle(int size, int midilower, int midiupper){
        POSITIONS = size;
        MIDILOWERBOUND = midilower;
        MIDIUPPERBOUND = midiupper;
        velocity = new double[POSITIONS];
        position = new int[POSITIONS];
        bestPosition = new int[POSITIONS];
    }


    public double getBestCost() {
        return bestCost;
    }

    public void setBestCost(double bestCost) {
        this.bestCost = bestCost;
    }

    public int[] getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(int[] bestPosition) {
        this.bestPosition = bestPosition;
    }

    public int getTonality() {
        return tonality;
    }

    public void setTonality(int tonality) {
        this.tonality = tonality;
    }

    /**
     * Generates positions of particle randomly in given range MIDILOWERBOUND - MIDIUPPERBOUND
     */
    public void generatePosition() {
        Random random = new Random();
        for (int i = 0; i < POSITIONS; i++) {

            position[i] = random.nextInt(MIDIUPPERBOUND - MIDILOWERBOUND) + 1 + MIDILOWERBOUND;
        }
    }

    public void setMajor(boolean major) {
        this.major = major;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(int a, double vel) {
        velocity[a] = vel;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int a, int num) {
        position[a] = num;
    }

    public int[] getSteps() {
        return steps;
    }

    public void setSteps(int[] steps) {
        this.steps = steps;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
