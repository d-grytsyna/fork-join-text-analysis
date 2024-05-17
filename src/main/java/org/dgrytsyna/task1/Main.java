package org.dgrytsyna.task1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("/Users/dianagr/Documents/GitHub/lab-4fork-join/src/main/resources/books"));

        List<Long> timesParallel = new ArrayList<>();
        for(int i=0; i<30; i++){
            long start1 = System.currentTimeMillis();
            WordAmount result  = wordCounter.countOccurrencesInParallel(folder);
            Double avgWordLengthParallel = (double)result.getSumLength()/result.getCount();
            System.out.println("AVG WORD SIZE PARALLEL " + avgWordLengthParallel);
            long end1 = System.currentTimeMillis();
            timesParallel.add(end1-start1);
        }


        List<Long> timesPlain = new ArrayList<>();
        for(int i=0; i<30; i++){
            long start = System.currentTimeMillis();
            WordAmount result = wordCounter.countWordsOnSingleThread(folder);
            Double avgWordLength = (double)result.getSumLength()/result.getCount();
            System.out.println("AVG WORD SIZE " + avgWordLength);
            long end = System.currentTimeMillis();
            timesPlain.add(end-start);
        }

        System.out.println(timesPlain);
        System.out.println(timesParallel);

        System.out.println("Plain avg time " + calculateAvgTime(timesPlain));
        System.out.println("Parallel avg time " + calculateAvgTime(timesParallel));

    }

    private static Long calculateAvgTime(List<Long> list){
        Long time = 0l;
        for(Long entry: list){
            time+=entry;
        }
        return time/list.size();
    }
}
