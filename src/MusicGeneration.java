/**
 * Method for generating a music sample of random tonality
 * <p>
 * Created by Ekaterina Yashkina on 17-11-2017.
 */
public class MusicGeneration {
    public MusicGeneration(int numCH) {
        this.numCH = numCH;
    }

    private Chord[] chords;//accompaniment of numCH*2 chords
    private int[] notes;//melody of numCH*4 notes
    private int tonality;//tonality of sample
    private String maj;//is tonality major or minor
    private int numCH;//number of chords to be generated
    private long timeSpent;//time we spent for generating a chord

    public long getTimeSpent() {
        return timeSpent;
    }

    public String getMaj() {
        return maj;
    }

    public int getTonality() {
        return tonality;

    }

    /**
     * Generating the chords and notes for sample
     * The algorithm does not always generate all the 8 chords, so we should repeat until we get the right sequence
     */
    public void generate() {
        Checking check = new Checking(numCH);//creating a class with method for determining whether we created right
        //sequences
        TonalityInfo info = new TonalityInfo();//generating tonality and computing 7 steps and possible chords in
        // lowest tonality
        long startTime = System.currentTimeMillis();
        PSO algo = new PSO(info);
        algo.pso();

        while (check.checkChords(algo.getChords(), info) != 8 || algo.getGlobalBestCost() < 46) {
            info = new TonalityInfo();
            algo = new PSO(info);
            algo.pso();
        }

        PSO2 sec = new PSO2(info, algo.getChords());
        sec.pso2();
        long stopTime = System.currentTimeMillis();
        timeSpent = (stopTime - startTime) / 1000L;
        tonality = info.getTonality();
        maj = info.isMajor() ? "major" : "minor";
        chords = algo.getChords();
        notes = sec.getGlobalBest();

    }

    public Chord[] getChords() {
        return chords;
    }

    public int[] getNotes() {
        return notes;
    }
}
