import java.util.Scanner;

/**
 * Simple interaction with user: gather statistics over 50 iterations or just creating a music sample
 * <p>
 * Created by Ekaterina Yashkina on 24-10-2017.
 */

public class Main {
    public static final int numCH = 8;//number of chords we want to generate

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("If you want to gather statistics, enter 1\nIf you want to create a file, enter 2");
        int input = in.nextInt();

        if (input == 1) {
            Statistics statistics = new Statistics(numCH);
            statistics.statistics();

        } else if (input == 2) {

            MusicGeneration gen = new MusicGeneration(numCH);
            gen.generate();
            System.out.println("Tonality: "+gen.getTonality()+48 + " " + gen.getMaj());

            System.out.println("Generated chords: ");
            for (int i = 0; i < gen.getChords().length; i++) {
                System.out.println(gen.getChords()[i].getTonic() + " " + gen.getChords()[i].getSubdominant()
                        + " " + gen.getChords()[i].getDominant());
            }
            System.out.println("Generated notes: ");
            for (int i = 0; i < gen.getNotes().length; i++) {
                System.out.print(gen.getNotes()[i] + " ");
            }
            System.out.println();
            System.out.println("Time spent: " + gen.getTimeSpent()+" sec");

            System.out.println("\n");
            System.out.println("Enter 1 if you want to use JFugue for listening\nEnter 2 if you want to use JMusic for listening");
            int inp = in.nextInt();
            if (inp == 1) {

                JFugueCreator output1 = new JFugueCreator(gen.getChords(), gen.getNotes());
                output1.createMidiFile();
                System.out.println("Your output has been written to file: " + output1.getFileName());
            } else if (inp == 2) {
                JMusicCreator output = new JMusicCreator(gen.getNotes(), gen.getChords());
                output.midiFile();
                System.out.println("Your output has been written to file: " + output.getFileName());
            }
        }


    }
}
