package org.dgrytsyna.task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) {
        int size = 1000;
        int q = 2;

        Matrix A = new Matrix(size, size);
        A.generateMatrix();
        A.writeToFile("A");


        Matrix B = new Matrix(size, size);
        B.generateMatrix();
        B.writeToFile("B");


        List<Long> parallelFoxTime = new ArrayList<>();
        List<Long> forkJoinFoxTime = new ArrayList<>();


        //Start time
        for(int i=0; i<20; i++){
            long startTimeFox = System.currentTimeMillis();
            //FOX
            Matrix fox = calculateFOX(A, B, q);
            //End time
            long endTimeFox = System.currentTimeMillis();
            //Elapsed
            long elapsedTimeFox = endTimeFox - startTimeFox;
            parallelFoxTime.add(elapsedTimeFox);
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        for(int i=0; i<20; i++){
            //Start time
            long startTime = System.currentTimeMillis();
            Matrix forkJoinMatrix = forkJoinPool.invoke(new MatrixMultiplicationTask(A, B, q));
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

//            System.out.println("Parallel computing using forkJoin, elapsed time: " + elapsedTime);
            forkJoinFoxTime.add(elapsedTime);

        }

        System.out.println(parallelFoxTime);
        System.out.println(forkJoinFoxTime);

        System.out.println("Parallel avg time " + calculateAvgTime(parallelFoxTime));
        System.out.println("Fork join avg time " + calculateAvgTime(forkJoinFoxTime));

//        System.out.println("Parallel computing using fox algorithm, elapsed time: " + elapsedTimeFox);





//        System.out.println("Results equal fox & forkJoin: " +Service.matrixEqual(fox, forkJoinMatrix));


    }
    private static Long calculateAvgTime(List<Long> list){
        Long time = 0l;
        for(Long entry: list){
            time+=entry;
        }
        return time/list.size();
    }
    private static Matrix calculateFOX(Matrix A, Matrix B, int q){
        //Calculate number of threads needed
        int numThreads = q * q;

        //Split Matrix A into blocks
        MatrixBlock matrixBlockA = new MatrixBlock(q);
        matrixBlockA.setBlocks(A);

        //Split Matrix B into blocks
        MatrixBlock matrixBlockB = new MatrixBlock(q);
        matrixBlockB.setBlocks(B);

        //Set empty blocks in result matrix
        MatrixBlock resultBlocks = new MatrixBlock(q);
        resultBlocks.setEmptyBlocks(A);


        //Calculation
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int startIndex = i;
            threads[i] = new Thread(() -> {
                // Multiply Blocks
                Service.multiplyMatrixFox(matrixBlockA, matrixBlockB, resultBlocks, startIndex, q);
            });
            threads[i].start();
        }

        //Wait till all threads complete calculations
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



//        resultBlocks.printMatrix();

        // Revert blocks back to matrix to return as result
        Matrix finalResult  = resultBlocks.blocksToMatrix(q);
//
//        System.out.println("FOX FINAL RESULT ");
//
//        finalResult.printMatrix();

        return finalResult;
    }

}
