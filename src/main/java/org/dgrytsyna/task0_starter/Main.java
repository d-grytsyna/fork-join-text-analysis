package org.dgrytsyna.task0_starter;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("/Users/dianagr/Documents/GitHub/lab-4fork-join/src/main/resources"));
        System.out.println(wordCounter.countOccurrencesInParallel(folder, "amet"));
    }
}
