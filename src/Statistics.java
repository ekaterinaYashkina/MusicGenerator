/**
 * Gathering some statistics about PSO
 * <p>
 * Created by Ekaterina Yashkina on 17-11-2017.
 */
public class Statistics {
    private int numCH;

    public Statistics(int numCH) {
        this.numCH = numCH;
    }

    public void statistics() {
        Checking check = new Checking(numCH);
        long[] chordGenExecution;//how long have we generated chords for given iteration
        long[] noteGenExecution;//how long have we generated notes for given iteration
        int[] tonalityCount = new int[24];//count the tonalities we generated for determining which one is more
                                        //probable

        final int MAXITERATIONS = 50;
        int countGood = 0;//how many good samples we generated
        int countChordIterations[] = new int[MAXITERATIONS];
        int countNoteIterations[] = new int[MAXITERATIONS];
        chordGenExecution = new long[MAXITERATIONS];
        noteGenExecution = new long[MAXITERATIONS];
        for (int i = 0; i < MAXITERATIONS; i++) {
            System.out.println("Generating sample: " + i);
            long startTime = System.currentTimeMillis();
            TonalityInfo info = new TonalityInfo();
            PSO algo = new PSO(info);
            algo.pso();

            while (check.checkChords(algo.getChords(), info) != 8 || algo.getGlobalBestCost() < 46) {
                info = new TonalityInfo();
                algo = new PSO(info);
                algo.pso();
            }
            countChordIterations[i] = algo.getIterations();

            long stopTime = System.currentTimeMillis();
            chordGenExecution[i] = stopTime - startTime;
            int maj = info.isMajor() ? 1 : 0;
            tonalityCount[info.getTonality() * 2 + maj] += 1;
            startTime = System.currentTimeMillis();
            PSO2 sec = new PSO2(info, algo.getChords());
            sec.pso2();
            countNoteIterations[i] = sec.getIterations();
            stopTime = System.currentTimeMillis();
            noteGenExecution[i] = stopTime - startTime;
            if (check.checkNotes(sec.getGlobalBest(), algo.getChords(), info.getSteps()) == 16) {
                countGood += 1;
            }
        }
        long sumCH = 0;
        long sumN = 0;
        long sumCIt = 0;
        long sumNIt = 0;
        for (int k = 0; k < MAXITERATIONS; k++) {
            sumCH += chordGenExecution[k];
            sumN += noteGenExecution[k];
            sumCIt+=countChordIterations[k];
            sumNIt+=countNoteIterations[k];
        }
        int max = 0;
        int maxIndex = -1;
        String mj = "";
        for (int i = 0; i < tonalityCount.length; i++) {
            if (tonalityCount[i] > max) {
                max = tonalityCount[i];
                maxIndex = i;
            }
        }
        if (maxIndex % 2 == 0) {
            mj = "minor";
        } else mj = "major";
        System.out.println("The average time of generating a chord is " + sumCH / MAXITERATIONS / 1000L + " sec");
        System.out.println("The average time of generating a note is " + sumN / MAXITERATIONS / 1000L + " sec");
        System.out.println("Good music samples: " + countGood + " out of " + MAXITERATIONS);
        System.out.println("The average amount of iterations to generate a chord is "+sumCIt/MAXITERATIONS);
        System.out.println("The average amount of iterations to generate a note is "+sumNIt/MAXITERATIONS);
        System.out.println("The most frequent tonality generated: " + maxIndex / 2 + " " + mj);
    }

}
