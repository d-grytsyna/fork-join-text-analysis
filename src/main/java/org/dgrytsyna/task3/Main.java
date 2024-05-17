package org.dgrytsyna.task3;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("/Users/dianagr/Documents/GitHub/lab-4fork-join/src/main/resources/small-test"));
        Map<String, CommonWord> words = wordCounter.countWordsInParallel(folder);
        for (Map.Entry<String, CommonWord> entry : words.entrySet()) {
            String word = entry.getKey();
            CommonWord wordInfo = entry.getValue();
            System.out.println();
            System.out.print(word);
            wordInfo.printInfo();
            System.out.println();
        }
    }
}
