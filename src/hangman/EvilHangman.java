package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman {
    private static EvilHangmanGame game = new EvilHangmanGame();
    static int numGuesses = 0;
    static int prevNumLetters;

    public static void main(String[] args) {
        String dictionary = args[0];
        int wordLength = Integer.parseInt(args[1]);
        numGuesses = Integer.parseInt(args[2]);

        File dictFile = new File(dictionary);

        try {
            game.startGame(dictFile, wordLength);
        }
        catch(IOException e) {
            System.out.println(e.toString());
        }
        catch(EmptyDictionaryException e) {
            System.out.println("⚠ "+e.toString()+": No words of length "+wordLength+" found in "+dictionary);
        }

        System.out.println("Word: "+game.getWordDisplay());
        runRound();
        return;
    }

    public static void runRound() {
        System.out.println("Guess a letter! ("+numGuesses+" guesses left)");

        try {
            Scanner userIn = new Scanner(System.in);
            String guessStr = userIn.nextLine().toLowerCase();
            Set<String> possibilities = new HashSet<>();

            // Make sure input char is between 'a' and 'z'
            if (guessStr.length() == 0 || guessStr.charAt(0) <= 96 || 123 <= guessStr.charAt(0)) {
                System.out.println("⚠ Invalid Input!");
                runRound();
            }

            try {
                possibilities = game.makeGuess(guessStr.charAt(0));
            } catch (GuessAlreadyMadeException e) {
                System.out.println("⚠ You already guessed that!");
                runRound();
            };

            if (prevNumLetters <= game.getNumLettersLeft()) numGuesses--;
            prevNumLetters = game.getNumLettersLeft();

            System.out.println("Word: "+game.getWordDisplay());

            if (game.getNumLettersLeft() > 0) {
                if (numGuesses > 0) {
                    System.out.println("Guessed letters: "+game.getGuessedLetters().toString());
                    runRound();
                }
                else {
                    System.out.println("No more guesses! Sorry.");
                    System.out.println("The word was: "+possibilities.toArray()[0]);
                    return;
                }
            }
            else  {
                System.out.println("Congrats!");
                System.out.println("The word was: "+possibilities.toArray()[0]);
                return;
            }

        } catch (StringIndexOutOfBoundsException e) {
//            System.out.println("⚠ Invalid Input!");
            runRound();
        }
    }
}
