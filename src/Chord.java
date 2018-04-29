/**
 * Created by Ekaterina Yashkina on 24-10-2017.
 */
public class Chord {
    private int tonic;
    private int subdominant;
    private int dominant;

    public Chord(int tonic, int subdominant, int dominant) {
        this.tonic = tonic;
        this.subdominant = subdominant;
        this.dominant = dominant;
    }

    public  Chord(){}

    public int getTonic() {
        return tonic;
    }

    public void setTonic(int tonic) {
        this.tonic = tonic;
    }

    public int getSubdominant() {
        return subdominant;
    }

    public void setSubdominant(int subdominant) {
        this.subdominant = subdominant;
    }

    public int getDominant() {
        return dominant;
    }

    public void setDominant(int dominant) {
        this.dominant = dominant;
    }
}
