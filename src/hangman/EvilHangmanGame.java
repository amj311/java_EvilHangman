package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    Set<String> words = new HashSet<>();
    SortedSet<Character> guessedLetters = new TreeSet<>();
    String wordDisplay = "";
    int numLettersLeft;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        words = new HashSet<>();
        guessedLetters = new TreeSet<>();
        wordDisplay = "";

        // find dictionary words of length WordLength
        Scanner myReader = new Scanner(dictionary);
        while (myReader.hasNext()) {
            String data = myReader.next();
            if (data.length() == wordLength) {
                words.add(data);
//                System.out.println(data);
            }
        }
        myReader.close();

        if (words.size() == 0) throw new EmptyDictionaryException();

        StringBuilder newWordDisplay = new StringBuilder();
        while (newWordDisplay.length() < wordLength) newWordDisplay.append("-");
        wordDisplay = newWordDisplay.toString();
        numLettersLeft = wordLength;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = (""+guess).toLowerCase().charAt(0);

        if (guessedLetters.contains(guess)) throw new GuessAlreadyMadeException();
        else guessedLetters.add(guess);

        // create map of all words with matching posVectors, keeping track of the largest one
        Set<Vector<Integer>> allCodes = new HashSet<>();
        Map<Vector<Integer>, HashSet<String>> codeToWordsMap = new HashMap<>();
        for (String word : words) {
            Vector<Integer> wordPosCode = getPosCode(word, guess);
            allCodes.add(wordPosCode);
//            System.out.println(word+": "+wordPosCode);
            if (codeToWordsMap.containsKey(wordPosCode)) {
                Set<String> setForCode = codeToWordsMap.get(wordPosCode);
                setForCode.add(word);
            }
            else {
                HashSet<String> newSet = new HashSet<>();
                newSet.add(word);
                codeToWordsMap.put(getPosCode(word, guess), newSet);
            }
        }

        // select set to return
        Set<Vector<Integer>> eliminationSet = new HashSet<>(allCodes);
        for (Vector<Integer> code : allCodes) {

//            System.out.println(eliminationSet.toString());
//            System.out.println(code);
            Set<String> codeSet = codeToWordsMap.get(code);
            for (Vector<Integer> otherCode : allCodes) {
//                System.out.println("    "+otherCode);

                if (otherCode == code) continue;
                if (!eliminationSet.contains(code)) continue;
                if (!eliminationSet.contains(otherCode)) continue;
                if (eliminationSet.size() == 1) break;

                Set<String> otherCodeSet = codeToWordsMap.get(otherCode);
                // Eliminate otherCode if its set is smaller than the first
                if (otherCodeSet.size() > codeSet.size()) {
                    eliminationSet.remove(code);
                    continue;
                } else if (codeSet.size() > otherCodeSet.size()) {
                    eliminationSet.remove(otherCode);
                    continue;
                } else if (code.size() != otherCode.size()) {
                    if (code.size() == 0) {
                        eliminationSet.remove(otherCode);
                        continue;
                    } else if (otherCode.size() == 0) {
                        eliminationSet.remove(code);
                        continue;
                    } else if (code.size() < otherCode.size()) {
                        eliminationSet.remove(otherCode);
                        continue;
                    } else if (otherCode.size() < code.size()) {
                        eliminationSet.remove(code);
                        continue;
                    }
                } else {
                    for (int i = code.size() - 1; i >= 0; i--) {
                        if (code.elementAt(i) > otherCode.elementAt(i)) {
                            eliminationSet.remove(otherCode);
                            continue;
                        } else if (otherCode.elementAt(i) > code.elementAt(i)) {
                            eliminationSet.remove(code);
                            continue;
                        }
                    }
                }


            }
        }


        //update words set
        words = codeToWordsMap.get(eliminationSet.toArray()[0]);

        // update wordDisplay.
        addGuessToDisplay(guess, (Vector<Integer>) eliminationSet.toArray()[0]);
//        System.out.println(wordDisplay);
        return words;
    }

    public Vector<Integer> getPosCode(String word, char guess) {
        Vector<Integer> posVector = new Vector<>();
        for (int c = 0; c < word.length(); c++) {
            if (word.charAt(c) == guess) posVector.add(c);
        }
        return posVector;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getWordDisplay() {
        return wordDisplay;
    }

    private void addGuessToDisplay(char guess, Vector<Integer> posVector) {
//        System.out.println(guess);
//        System.out.println(posVector);
        numLettersLeft -= posVector.size();

        StringBuilder newDisplay = new StringBuilder(wordDisplay);
        for (int p = 0; p < posVector.size(); p++) {
            int pos = Integer.parseInt(String.valueOf(posVector.elementAt(p)));
            newDisplay.replace(pos, pos+1, String.valueOf(guess));
        }
        wordDisplay = newDisplay.toString();
    }

    public int getNumLettersLeft() {
        return numLettersLeft;
    }
}
