package org.dgrytsyna.task4;

import org.dgrytsyna.task3.CommonWord;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WordCounter {

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    Map<String, List<String>> findWordsInParallel(Folder folder, List<String> searchedWords) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWords));
    }

     String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    Map<String, List<String>> wordsFinder(Document document, List<String> searchedWords) {
        List<String> foundWords = new ArrayList<>();
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                if(word.length()!=0) {
                if (searchedWords.contains(word)) {
                    if(!foundWords.contains(word)) foundWords.add(word);
                }
                }
            }
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put(document.getDocumentName(), foundWords);
        return result;
    }

    class DocumentSearchTask extends RecursiveTask<Map<String, List<String>>> {
        private final Document document;
        private final List<String> searchedWords;

        DocumentSearchTask(Document document, List<String> searchedWords) {
            super();
            this.document = document;
            this.searchedWords = searchedWords;
        }

        @Override
        protected Map<String, List<String>> compute() {
            return wordsFinder(document, searchedWords);
        }
    }
    class FolderSearchTask extends RecursiveTask<Map<String, List<String>>> {
        private final Folder folder;
        private final List<String> searchedWords;

        FolderSearchTask(Folder folder, List<String> searchedWords) {
            super();
            this.folder = folder;
            this.searchedWords = searchedWords;
        }

        @Override
        protected Map<String, List<String>> compute() {
            Map<String, List<String>>  finalResult = new HashMap<>();
            List<RecursiveTask<Map<String, List<String>>>> forks = new LinkedList<>();
            for (Folder subFolder : folder.getSubFolders()) {
                FolderSearchTask task = new FolderSearchTask(subFolder, searchedWords);
                forks.add(task);
                task.fork();
            }
            for (Document document : folder.getDocuments()) {
                DocumentSearchTask task = new DocumentSearchTask(document, searchedWords);
                forks.add(task);
                task.fork();
            }
            for (RecursiveTask<Map<String, List<String>>> task : forks) {
                Map<String, List<String>> resultMap = task.join();
                for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
                    String document = entry.getKey();
                    List<String> words = entry.getValue();
                    finalResult.put(document, words);
                }
            }
            return finalResult;
        }
    }
}