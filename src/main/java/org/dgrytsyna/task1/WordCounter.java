package org.dgrytsyna.task1;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class WordCounter {

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    WordAmount countOccurrencesInParallel(Folder folder) {
        return forkJoinPool.invoke(new FolderSearchTask(folder));
    }

     String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    WordAmount countWordsLength(Document document) {
        WordAmount wordAmount = new WordAmount();
        int count = 0;
        int lenght = 0;
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                if(word.length()!=0) {
                    count = count + 1;
                    lenght += word.length();
                }
            }
        }
        wordAmount.setCount(count);
        wordAmount.setSumLength(lenght);
        return wordAmount;
    }
    class DocumentSearchTask extends RecursiveTask<WordAmount> {
        private final Document document;

        DocumentSearchTask(Document document) {
            super();
            this.document = document;
        }

        @Override
        protected WordAmount compute() {
            return countWordsLength(document);
        }
    }
    class FolderSearchTask extends RecursiveTask<WordAmount> {
        private final Folder folder;

        FolderSearchTask(Folder folder) {
            super();
            this.folder = folder;
        }

        @Override
        protected WordAmount compute() {
            WordAmount wordsLength = new WordAmount();
            List<RecursiveTask<WordAmount>> forks = new LinkedList<>();
            for (Folder subFolder : folder.getSubFolders()) {
                FolderSearchTask task = new FolderSearchTask(subFolder);
                forks.add(task);
                task.fork();
            }
            for (Document document : folder.getDocuments()) {
                DocumentSearchTask task = new DocumentSearchTask(document);
                forks.add(task);
                task.fork();
            }
            for (RecursiveTask<WordAmount> task : forks) {
                WordAmount calculateResult = task.join();
                wordsLength.addCount(calculateResult.getCount());
                wordsLength.addLength(calculateResult.getSumLength());
            }
            return wordsLength;
        }
    }

    WordAmount countWordsOnSingleThread(Folder folder) {
        WordAmount finalResult = new WordAmount();
        for (Folder subFolder : folder.getSubFolders()) {
            WordAmount amount = countWordsOnSingleThread(subFolder);
            finalResult.addLength(amount.getSumLength());
            finalResult.addCount(amount.getCount());
        }
        for (Document document : folder.getDocuments()) {
            WordAmount amount = countWordsLength(document);
            finalResult.addLength(amount.getSumLength());
            finalResult.addCount(amount.getCount());
        }
        return finalResult;
    }

}