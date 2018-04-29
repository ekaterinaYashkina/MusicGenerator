import jm.JMC;
import jm.music.data.*;
import jm.util.Write;

/**
 * Writing the sample to midi file using JMusic library
 * <p>
 * Created by Ekaterina Yashkina on 16-11-2017.
 */
public class JMusicCreator implements JMC {
    private int [] notes;//melody
    private Chord[] chords;//accompaniment
    private final double tempo = 120;//tempo according the task
    private final String fileName = "generated_music.midi";//name of the file

    public String getFileName() {
        return fileName;
    }

    public void midiFile(){
        Score s = new Score("MIDI demo");
        s.setTempo(tempo);
        Part p1 = new Part("Melody", 0);
        Part p2 = new Part("Chords", 1);
        for (int i = 0; i<chords.length; i++){
            int [] chArray = {chords[i].getTonic(), chords[i].getSubdominant(), chords[i].getDominant()};
            CPhrase chord = new CPhrase();
            chord.addChord(chArray, QN);
            p2.addCPhrase(chord);
        }

        Phrase phr = new Phrase();
        for(int i = 0; i<notes.length; i++){
            Note n = new Note(notes[i], EN);
            phr.addNote(n);
        }
        p1.addPhrase(phr);
        s.addPart(p1);
        s.addPart(p2);
        Write.midi(s, fileName);
    }

    public JMusicCreator(int[] notes, Chord[] chords){
        this.notes = notes;
        this.chords = chords;
    }
}
