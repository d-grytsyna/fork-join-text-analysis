package org.dgrytsyna.task3;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WordCounter {

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    Map<String, CommonWord> countWordsInParallel(Folder folder) {
        return forkJoinPool.invoke(new FolderSearchTask(folder));
    }

     String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    Map<String, CommonWord> wordCounters(Document document) {
        Map<String, CommonWord> foundWords = new HashMap<>();
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                if(word.length()!=0) {
                    if(foundWords.containsKey(word)){
                        CommonWord existingWordInfo = foundWords.get(word);
                        existingWordInfo.incrementCount();
                        foundWords.put(word, existingWordInfo);
                    }else{
                        CommonWord newWordInfo = new CommonWord(List.of(document.getDocumentName()),1);
                        foundWords.put(word, newWordInfo);
                    }
                }
            }
        }
        return foundWords;
    }

    class DocumentSearchTask extends RecursiveTask<Map<String, CommonWord>> {
        private final Document document;
        DocumentSearchTask(Document document) {
            super();
            this.document = document;
        }

        @Override
        protected Map<String, CommonWord> compute() {
            return wordCounters(document);
        }
    }
    class FolderSearchTask extends RecursiveTask<Map<String, CommonWord>> {
        private final Folder folder;


        FolderSearchTask(Folder folder) {
            super();
            this.folder = folder;
        }

        @Override
        protected Map<String, CommonWord> compute() {
            Map<String, CommonWord> finalResult = new HashMap<>();
            List<RecursiveTask<Map<String, CommonWord>>> forks = new LinkedList<>();
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
            for (RecursiveTask<Map<String, CommonWord>> task : forks) {
                Map<String, CommonWord> resultMap = task.join();
                for (Map.Entry<String, CommonWord> entry : resultMap.entrySet()) {
                    String word = entry.getKey();
                    CommonWord wordInfo = entry.getValue();
                    if(finalResult.containsKey(word)){
                        CommonWord existingWord = finalResult.get(word);
                        existingWord.addList(wordInfo.getDocumentNames());
                        existingWord.addCounter(wordInfo.getCount());
                        finalResult.put(word, existingWord);
                    }else{
                        finalResult.put(word, wordInfo);
                    }
                }
            }
            return finalResult;
        }
    }

}