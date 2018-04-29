import java.util.Random;

/**
 * PSO2 for generating a note
 * The main idea is to make the set of particles move towards the global best and change their values
 * so as to get the better fitness values
 * <p>
 * The given set of w, c1 and c2 are useful only for this particular combination of particles, iterations amount and
 * fitness function
 * <p>
 * Created by Ekaterina Yashkina on 06-11-2017.
 */
public class PSO2 {
    private double w = 0.8;//inertia coefficient
    private double c1 = 0.5;//personal acceleration coefficient
    private double c2 = 1.3;//social acceleration coefficient
    private int NOTES = 32;
    private final int PARTICLES = 10000;//amount of particle population
    private final int ITERATIONS = 200;//maximum iterations
    private int[] steps;//steps of generated tonality(7 notes)
    private Chord[] chords;//possible three sound chords generated for given tonality(from tonic,
    // subdominant and dominant)
    private Particle[] particles;//array of generated particle population
    private int MAXOCTAVE = 72;//maximum possible value of generated note
    private int MINOCTAVE = 60;//minimum possible value of generated note
    private final int MIDILOWERBOUND = 60;//lower bound according to the task and maxvalue of previously
    //generated chords
    private final int MIDIUPPERBOUND = 96;//upper bound according to the task
    private final int MAXFITNESS = 1600;//maximum possible fitness according to fitness function
    private int[] globalBestPosition;//best values of notes in all history of iterations
    private double globalBestCost;//best value of fitness function in all histiry of iterations
    private int iterations;

    /**
     * Checks whether the first note of the pairs(there 16 pairs) are equal to k+12*n,
     * where k - a note from the chord and n - octave difference
     *
     * @param i     - note to check
     * @param chord - chord that will be played with the same pair of notes
     * @return - whether condition was satisfied
     */
    private boolean inChords(int i, Chord chord) {
        if (i - chord.getDominant() < 12) {
            if (i % 12 == chord.getTonic() % 12 || i % 12 == chord.getSubdominant() % 12 || i % 12 == chord.getDominant() % 12) {
                return true;

            }
        }
        return false;
    }

    /**
     * Checks whether a note is one of 7 steps of the tonality
     *
     * @param i - given note
     * @return - whether condition was satisfied
     */
    private boolean inSteps(int i) {
        for (int j = 0; j < steps.length; j++) {
            if (steps[j] % 12 == i % 12) return true;
        }
        return false;
    }

    /**
     * Fitness function for generating a note:
     * 1) It checks whether note value is in bounds determined by MINOCTAVE and MAXOCATVE - if not, value is decreased
     * by 100
     * 2) It checks the condition checkInChord(int i, Chord chord) for 1 note in the couple of ones that belongs to
     * a particular chords
     * 3) It checks the condition checkInNote(int i) for 2 note in the couple of ones that belongs to
     * a particular chords
     * 4) If both conditions are satisfied, the value is increased by 100, if only one of them  - increased by 3
     * 5) If two notes of the couple are the same - we decrease value by 11
     *
     * @param p - particle we should count the fitness value on
     * @return - counted fitness (maximum is determined in MAXFITNESS)
     */
    private int fitness(Particle p) {
        int value = 0;
        int[] pos = p.getPosition();
        for (int i = 0; i < pos.length; i += 2) {
            if (pos[i] <= MINOCTAVE || pos[i + 1] <= MINOCTAVE || pos[i] > MAXOCTAVE || pos[i + 1] > MAXOCTAVE)
                value -= 100;
            else {
                boolean a = inSteps(pos[i + 1]);
                boolean b = inChords(pos[i], chords[i / 2]);
                if (pos[i] == pos[i + 1]) value -= 11;
                else {
                    if (a && b) {
                        value += 100;
                    } else if (a || b) value += 3;
                }

            }
        }
        return value;
    }


    /**
     * Instantiation and initialization of all values we need for PSO, including:
     * 1) Information about tonality(description in class TonalityInfo)
     * 2) Generating the first particle population
     * 3) Determining the fitness values of initial particle population
     * 4) Determining local best for each particle and global best over all population at teh moment
     *
     * @param info   -
     * @param chords
     */
    public PSO2(TonalityInfo info, Chord[] chords) {
        particles = new Particle[PARTICLES];
        this.chords = chords;
        this.steps = info.getSteps();
        double lcost = -Double.MAX_VALUE;
        for (int i = 0; i < PARTICLES; i++) {
            particles[i] = new Particle(NOTES, MINOCTAVE, MAXOCTAVE);
            particles[i].generatePosition();
            double cost = fitness(particles[i]);
            particles[i].setCost(cost);
            int[] p = particles[i].getPosition();
            particles[i].setBestPosition(p);
            particles[i].setBestCost(cost);
            if (cost > lcost) {
                lcost = cost;
                globalBestPosition = p;
                globalBestCost = cost;
            }
        }

    }

    public double getGlobalBestCost() {
        return globalBestCost;
    }

    public int[] getGlobalBest() {
        return globalBestPosition;
    }

    public int getIterations() {
        return iterations;
    }

    /**
     * PSO for generating a sequence of notes suitable for chords
     * It refreshes the velocity values of each note in particle and then their values
     * After it counts the new fitness value and changes the local best and global best if necessary
     * <p>
     * To prevent values going out of range, we check this before changing local and global bests:
     * if the condition is not satisfied, we regenerate this value and recompute its velocity and fitness
     * <p>
     * If the global best`s cost is already a maximum fitness value, we got the right answer and loop terminates
     */
    public void pso2() {

        for (int i = 0; i < ITERATIONS; i++) {
            for (int j = 0; j < PARTICLES; j++) {
                for (int h = 0; h < 32; h++) {
                    //refreshing velocity and position values
                    particles[j].setVelocity(h, w * particles[j].getVelocity()[h]
                            + c1 * Math.random() * (particles[j].getBestPosition()[h] - particles[j].getPosition()[h])
                            + c2 * Math.random() * (
                           globalBestPosition[h] - particles[j].getPosition()[h]));
                    particles[j].setPosition(h, (int) Math.round(particles[j].getPosition()[h] + particles[j].getVelocity()[h]));

                    if (particles[j].getPosition()[h] <= MIDILOWERBOUND || particles[j].getPosition()[h] > MIDIUPPERBOUND) {
                        Random r = new Random();
                        particles[j].setPosition(h, r.nextInt(MAXOCTAVE - MINOCTAVE) + 1 + MINOCTAVE);
                        particles[j].setVelocity(h, c1 * Math.random() * (particles[j].getBestPosition()[h] - particles[j].getPosition()[h])
                                + c2 * Math.random() * (
                                globalBestPosition[h]- particles[j].getPosition()[h]));
                    }

                }

                //recomputing fitness and changing local and global bests if necessary
                particles[j].setCost(fitness(particles[j]));
                if (particles[j].getCost() > particles[j].getBestCost()) {
                    particles[j].setBestPosition(particles[j].getPosition().clone());
                    double a = particles[j].getCost();
                    particles[j].setBestCost(a);
                }
                if (particles[j].getBestCost() > globalBestCost){
                    double a = particles[j].getBestCost();
                    globalBestPosition = particles[j].getBestPosition().clone();
                    globalBestCost = a;
                }
                if (globalBestCost==MAXFITNESS) break;
            }
            if (globalBestCost==MAXFITNESS) {
                iterations = i+1;
                break;
            }
            iterations = i+1;

        }
    }

}

