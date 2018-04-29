import java.util.Random;

/**
 * PSO2 for generating a note
 * The main idea is to make the set of particles move towards the global best and change their values
 * so as to get the better fitness values
 * <p>
 * The given set of w, c1 and c2 are useful only for this particular combination of particles, iterations amount and
 * fitness function
 * <p>
 * We generate 8 chords and repeat the sequence as it gives much better results than generating 16 chords
 * <p>
 * Created by Ekaterina Yashkina on 24-10-2017.
 */
public class PSO {
    private final int NUMC = 8;//amount of chords to generate
    private final int maxValue = 24 + 22;//maximum value of fitness function
    private final int MAXITERATIONS = 100;//maximum amount of iterations
    private final int PARTICLES = 10000;//maximum amount of particles
    private int tonality;//tonality of chords
    private boolean isMajor;//is tonality of chords is major or minor
    private Chord[] generated;//finally generated particle set in chord representation
    private Chord[] chords;//possible chord set of given tonality(in the lowest octave) to compare to
    private Particle[] particles;//set of particles for PSO
    private int[] globalBestPosition;//best values of chord values in all history of iterations
    private double globalBestCost;//best cost of chord values in all history of iterations
    private int iterations;

    private final int MIDILOWERBOUND = 48;//smallest possible midi value that can be generated for chord note
    private final int MIDIUPPERBOUND = 60;//biggest  possible midi value that can be generated for chord note

    public double getGlobalBestCost() {
        return globalBestCost;
    }

    public Chord[] getChords() {
        return generated;
    }

    /**
     * Fitness function for PSO
     * 1) If the value is out of range and the difference between two neighbour notes is too big the value is decreased
     * by 1000
     * 2) If all the notes in chords are similar to the one in example, we increase value by 3
     * 3) If we have 4 the same chords going one after one, we decrease value by 97
     * 4) If the sequence repeats the every 3 chords, value is increased by 11
     *
     * @param pos - values of chords
     * @return - fitness value
     */
    public double calculateFitness(int[] pos) {
        int value = 0;
        int maxvalue = value;
        for (int i = 0; i < pos.length; i += 3) {
            //checking for going out of range
            if (pos[i] < MIDILOWERBOUND || pos[i] > MIDIUPPERBOUND + 12 || pos[i + 1] < MIDILOWERBOUND || pos[i + 1] > MIDIUPPERBOUND + 12
                    || pos[i + 2] < MIDILOWERBOUND || pos[i + 2] > MIDIUPPERBOUND || pos[i + 1] - pos[i] < 3
                    || pos[i + 2] - pos[i + 1] < 3 || pos[i + 1] - pos[i] > 5 || pos[i + 2] - pos[i + 1] > 5) {
                value -= 1000;
            } else {
                //checking for the similarity with model chords
                int j = 0;
                while (j < 3) {
                    maxvalue = 0;
                    int a = 0;
                    if (chords[j].getTonic() % 12 == pos[i] % 12) {
                        a += 1;
                    }
                    if (chords[j].getSubdominant() % 12 == pos[i + 1] % 12) {
                        a++;
                    }
                    if (chords[j].getDominant() % 12 == pos[i + 2] % 12) {
                        a++;
                    }
                    if (a == 3) {
                        j = 3;
                    }
                    if (maxvalue < a) maxvalue = a;
                    j++;
                }
                value += maxvalue;

            }
        }
        //checking for repetition
        if (value >= 24) {
            int count = 1;
            int t = pos[0];
            int s = pos[1];
            int d = pos[2];
            for (int k = 3; k < pos.length; k += 3) {
                if (pos[k] == t && pos[k + 1] == s && pos[k + 2] == d) {
                    count++;
                } else {
                    t = pos[k];
                    s = pos[k + 1];
                    d = pos[k + 2];
                }
                if (count >= 4) {
                    value -= 97;
                    break;
                }
            }
        }
        //checking for repetition every 3 chords
        if (value >= 24) {
            for (int k = 0; k < pos.length; k += 12) {
                if (pos[k] == pos[k + 9] && pos[k + 1] == pos[k + 9 + 1] && pos[k + 2] == pos[k + 9 + 2]) {
                    value += 11;
                }
            }
        }
        return value;
    }


    public int[] getGlobalBest() {
        return globalBestPosition;
    }

    private double w = 0.201;//inertia coefficient
    private double c1 = 0.4;//personal acceleration coefficient
    private double c2 = 1.3;//social acceleration coefficient

    /**
     * Generates particles, calculates their fitness and local best
     */
    public void generateParticles() {
        for (int i = 0; i < PARTICLES; i++) {
            particles[i] = new Particle(NUMC*3, MIDILOWERBOUND, MIDIUPPERBOUND);
            particles[i].setTonality(tonality);
            particles[i].setMajor(isMajor);
            particles[i].generatePosition();
            double a = calculateFitness(particles[i].getPosition());
            particles[i].setCost(a);
            int[] pp = particles[i].getPosition();
            particles[i].setBestPosition(pp);
            particles[i].setBestCost(a);
        }
    }


    /**
     * Initializes all the values needed for PSO, determines global best at first step
     *
     * @param info
     */
    public PSO(TonalityInfo info) {
        particles = new Particle[PARTICLES];
        this.tonality = info.getTonality();
        this.isMajor = info.isMajor();
        this.chords = info.getChords();
        generateParticles();
        int[] pos = particles[0].getPosition();
        double bCost = particles[0].getCost();
        for (int i = 1; i < PARTICLES; i++) {
            if (particles[i].getCost() > bCost) {
                bCost = particles[i].getCost();
                pos = particles[i].getPosition();
            }
        }
        globalBestPosition = pos;
        globalBestCost = bCost;
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
    public void pso() {
        for (int i = 0; i < MAXITERATIONS; i++) {
            for (int j = 0; j < PARTICLES; j++) {
                for (int h = 0; h < NUMC * 3; h++) {
                    particles[j].setVelocity(h, w * particles[j].getVelocity()[h]
                            + c1 * Math.random() * (particles[j].getBestPosition()[h] - particles[j].getPosition()[h])
                            + c2 * Math.random() * (//globalBest.getPosition()[h]
                           globalBestPosition[h] - particles[j].getPosition()[h]));
                    particles[j].setPosition(h, (int) Math.round(particles[j].getPosition()[h] + particles[j].getVelocity()[h]));
                    if (particles[j].getPosition()[h] < MIDILOWERBOUND || particles[j].getPosition()[h] > MIDIUPPERBOUND + 12) {
                        Random r = new Random();
                        particles[j].setPosition(h, r.nextInt(MIDIUPPERBOUND - MIDILOWERBOUND) + 1 + MIDILOWERBOUND);
                        particles[j].setVelocity(h, c1 * Math.random() * (particles[j].getBestPosition()[h] - particles[j].getPosition()[h])
                                + c2 * Math.random() * (//globalBest.getPosition()[h]
                                globalBestPosition[h]- particles[j].getPosition()[h]));
                    }
                }
                particles[j].setCost(calculateFitness(particles[j].getPosition()));
                if (particles[j].getCost() >= particles[j].getBestCost()) {
                    particles[j].setBestPosition(particles[j].getPosition().clone());
                    double a = particles[j].getCost();
                    particles[j].setBestCost(a);
                }
                if (particles[j].getBestCost() >= globalBestCost){
                    double a = particles[j].getBestCost();
                    globalBestPosition = particles[j].getBestPosition().clone();
                    globalBestCost = a;
                }
                if (globalBestCost==maxValue)break;

            }
            if (globalBestCost==maxValue) {
                iterations = i+1;
                break;
            }
        iterations = i+1;
        }
        chordsRepresentation();
    }

    /**
     * Converting global best positions from the array to chord representation
     */
    private void chordsRepresentation() {
        generated = new Chord[NUMC * 2];
        int[] pos = globalBestPosition;

        for (int i = 0; i < pos.length; i += 3) {
            generated[i / 3] = new Chord(pos[i], pos[i + 1], pos[i + 2]);
            generated[i / 3 + 8] = new Chord(pos[i], pos[i + 1], pos[i + 2]);
        }
    }


}
