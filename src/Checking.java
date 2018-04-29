/**
 * Check for full amount of correctly generated chords and notes
 *
 * Created by Ekaterina Yashkina on 17-11-2017.
 */
public class Checking {
    private int numCH;//number of chords we generated
    public Checking(int numCH){
        this.numCH = numCH;
    }

    /**
     * Checking whether the notes are suitable for generated tonality and chords
     * @param notes
     * @param chords
     * @param steps
     * @return - amount of correctly generated pairs of notes
     */
    public int checkNotes(int [] notes, Chord[] chords, int[] steps){
        int val = 0;
        for(int i = 0; i<chords.length; i++ ){
            if (i*2>notes.length-1) break;
            if (notes[2*i]%12==chords[i].getTonic()%12||notes[i*2]%12==chords[i].getSubdominant()%12||notes[i*2]%12==chords[i].getDominant()%12){
                for(int k = 0; k<steps.length; k++){
                    if (notes[2*i+1]%12==steps[k]%12) {
                        val++;
                        break;
                    }
                }
            }
        }
        return val;
    }

    /**
     * Checking whether accords are suitable for generated tonality
     * @param generated
     * @param info
     * @return
     */
    public int checkChords(Chord [] generated,TonalityInfo info){
        int count = 0;
        Chord[] inTonality = info.getChords();
        for(int i = 0; i<numCH; i++){
            for (int j = 0; j<3; j++) {
                if (generated[i].getTonic()%12==inTonality[j].getTonic()%12 &&
                        generated[i].getSubdominant()%12==inTonality[j].getSubdominant()%12 &&
                        generated[i].getDominant()%12==inTonality[j].getDominant()%12) {
                    count++;
                }
            }

        }
        return count;
    }
}
