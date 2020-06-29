package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    Set<String> words = new HashSet<String>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        // find dictionary words of length WordLength
        Scanner myReader = new Scanner(dictionary);
        while (myReader.hasNext()) {
            String data = myReader.next();
            if (data.length() == wordLength) words.add(data);
        }
        myReader.close();

        if (words.size() == 0) throw new EmptyDictionaryException();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
