import java.util.Random;

/**
 * Created by Ekaterina Yashkina on 16-11-2017.
 */
public class TonalityInfo {
    private int tonality;
    private boolean isMajor;
    private Chord[] chords;
    private int[] steps;


    public Chord[] getChords() {
        return chords;
    }

    public boolean isMajor() {
        return isMajor;
    }

    public int getTonality() {

        return tonality;
    }

    public int[] getSteps() {
        return steps;
    }

    public TonalityInfo(){
        Random random = new Random();
        tonality = random.nextInt(12 );
        int major = random.nextInt(2);
        if (major==0)
            isMajor = false;
        else isMajor = true;

        setChords();
        generateSteps();

    }

    private void setChords(){

        chords = new Chord[3];
        for(int i = 0; i<3; i++){
            chords[i] = new Chord();
        }
        chords[0].setTonic(tonality);
        chords[1].setTonic(tonality+5);
        chords[2].setTonic(tonality+7);
        if (isMajor){
            chords[0].setSubdominant(chords[0].getTonic()+4);
            chords[1].setSubdominant(chords[1].getTonic()+4);
            chords[2].setSubdominant(chords[2].getTonic()+4);
            chords[0].setDominant(chords[0].getSubdominant()+3);
            chords[1].setDominant(chords[1].getSubdominant()+3);
            chords[2].setDominant(chords[2].getSubdominant()+3);

        }else{
            chords[0].setSubdominant(chords[0].getTonic()+3);
            chords[1].setSubdominant(chords[1].getTonic()+3);
            chords[2].setSubdominant(chords[2].getTonic()+3);
            chords[0].setDominant(chords[0].getSubdominant()+4);
            chords[1].setDominant(chords[1].getSubdominant()+4);
            chords[2].setDominant(chords[2].getSubdominant()+4);
        }

    }

    private void generateSteps(){
        steps = new int[7];
        steps[0] = tonality;
        steps[1] = steps[0]+2;
        steps[3] = tonality+5;
        steps[4] = tonality+7;
        if (isMajor) {
            steps[2] = steps[1]+2;
            steps[5] = steps[4]+2;
        }
        else{
            steps[2] = steps[1]+1;
            steps[5] = steps[4]+1;
        }
        steps[6] = steps[5]+2;
    }

}
