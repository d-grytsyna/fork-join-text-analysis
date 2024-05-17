package org.dgrytsyna.task4;

import org.dgrytsyna.task3.CommonWord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("/Users/dianagr/Documents/GitHub/lab-4fork-join/src/main/resources/medium-test"));
        List<String> searchedWords = new ArrayList<>();
        searchedWords.add("left");
        searchedWords.add("travel");
        searchedWords.add("dog");
        searchedWords.add("evil");
        searchedWords.add("good");
        searchedWords.add("castle");
        searchedWords.add("man");
        searchedWords.add("woman");
        searchedWords.add("arrive");
        Map<String, List<String>> words = wordCounter.findWordsInParallel(folder, searchedWords);
        for (Map.Entry<String, List<String>> entry : words.entrySet()) {
            String document = entry.getKey();
            List<String> wordsFound = entry.getValue();
            System.out.println("In document " + document + " found words " + wordsFound);
        }
    }
}
