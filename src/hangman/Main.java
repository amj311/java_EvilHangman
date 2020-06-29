package hangman;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main (String[] args) {
        String dictionary = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);

        System.out.println(wordLength);

        File dictFile = new File(dictionary);
        EvilHangmanGame game = new EvilHangmanGame();

        try {
            game.startGame(dictFile, wordLength);
        }
        catch(IOException e) {
            System.out.println(e.toString());
        }
        catch(EmptyDictionaryException e) {
            System.out.println("⚠ "+e.toString()+": No words of length "+wordLength+" found in "+dictionary);
        }

        return;
    }
}
