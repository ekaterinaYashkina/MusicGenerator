import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import java.io.File;
import java.io.IOException;

/**
 * Writing the sample to midi file using JFugue library
 * <p>
 * Created by Ekaterina Yashkina on 16-11-2017.
 */
public class JFugueCreator {
    private String musicChord = "";
    private String musicNote = "";
    private String musicPath = "";

    private Chord[] chords;//accompaniment
    private int[] notes;//melody
    private final String fileName = "jfugue_generated";//name of the file

    private final int tempo = 120;//tempo according to the task
    private final String instrument = "Piano";//instrument
    private String midiFileNameEnd = ".mid";//extension


    public JFugueCreator(Chord[] chords, int[] notes) {
        this.chords = chords;
        this.notes = notes;
        musicChordBuilder();
        musicNoteBuilder();
    }

    public String getFileName() {
        return fileName;
    }

    public void createMidiFile() // throws IOException, InvalidMidiDataException
    {
        Pattern pattern = new Pattern("T" + Integer.toString(tempo) + " V0 I[" + instrument + "] " + musicNote + " V1 I[" + instrument + "] " + musicChord).setVoice(0);
        try {
            MidiFileManager.savePatternToMidi(pattern, new File(musicPath + fileName + midiFileNameEnd));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void musicChordBuilder() {
        for (int i = 0; i < chords.length - 1; i++) {
            musicChord += chords[i].getTonic() + "q+" + chords[i].getSubdominant() + "q+" + chords[i].getDominant() + "q ";
        }
        musicChord += chords[chords.length - 1].getTonic() + "q+" + chords[chords.length - 1].getSubdominant() + "q+" + chords[chords.length - 1].getDominant() + "q ";
    }


    private void musicNoteBuilder() {

        for (int i = 0; i < notes.length - 1; i++) {
            musicNote += notes[i] + "i ";
        }
        musicNote += notes[notes.length - 1] + "i";
    }
}
