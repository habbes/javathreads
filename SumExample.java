import java.util.*;

public class SumExample {

    public static void main(String[] args) {
        
        int[] nums = createNumberArray(100000000);
        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        long sum = sequentialSum(nums);
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0; // divide by 1000 to convert to secs
        System.out.printf("Sequential result %d\n, took %.3f seconds\n", sum, elapsedTime);

        startTime = System.currentTimeMillis();
        long sum2 = parallelSum(nums, 4);
        endTime = System.currentTimeMillis();
        elapsedTime = (endTime - startTime) / 1000.0;
        System.out.printf("Parallel result %d\n, took %.3f seconds\n", sum2, elapsedTime);
    }

    public static int[] createNumberArray(int size) {
        // create an array and fill it with random numbers
        Random randomGenerator = new Random();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = randomGenerator.nextInt();
        }

        return result;
    }

    public static void printArray(int[] items) {
        for (int i = 0; i < items.length; i++) {
            System.out.print(items[i] + ", ");
        }
        System.out.println();
    }
    
    public static long sequentialSum(int[] items) {

        long sum = 0;
        for (int i = 0; i < items.length; i++) {
            sum += items[i];
        }

        return sum;
    }

    public static long parallelSum(int[] items, int numThreads) {
        // assume we have [1, 2, 3, 4] and 2 threads
        // the sum is 1+2+3+4 = 10
        // to compute in parallel, we can split the task
        // between 2 threads
        // thread1 could do 1 + 2 = 3
        // thread2 could do 3 + 4 = 7
        // once all threads have completed, we can sum their results 3 + 7 = 10

        int totalLength = items.length;
        int sliceLength = totalLength / numThreads;
        // NOTE: we have a potential bug here,
        // if totalLength is not divisible by numThreads
        // there's a portion of the array that might not be assigned
        // to any thread, and that will lead to incorrect results
        // (e.g. totalLength = 22 and numThreads = 5, then sliceLength will be 4
        // 5 threads will operate on 4 elements each i.e. 20 elements total
        // meaning the 2 last elements won't be accounted for

        AdderThread[] threads = new AdderThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            // assume sliceLength = 3
            // 1st iter: start = 0, stop = 2
            // i = 1, start = 3, stop = 5
            // it's important that we don't have overlaps
            // between the different array slices otherwise we'll get incorrect results
            int startPos = i * sliceLength;
            int stopPos = startPos + sliceLength - 1;
            AdderThread thread = new AdderThread(items, startPos, stopPos);
            threads[i] = thread;
            thread.start();
        }

        
        

        try {
            // we should wait for all the threads to complete
            // their calculations before we aggregate the final results
            // otherwise we might get incorrect results
            for(AdderThread thread : threads) {
                thread.join();
            }
        }
        catch (Exception e) {

        }

        long sum = 0;
        // aggregate the result of each thread into
        // the final result sum
        for (AdderThread thread : threads) {
            sum += thread.result;
        }

        return sum;

    }
}

class AdderThread extends Thread {
    int[] items;
    int startPos;
    int endPos;

    long result;

    public AdderThread(int[] items, int startPos, int endPos) {
        this.items = items;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public void run() {
        long sum = 0;
        for (int i = startPos; i <= endPos; i++) {
            sum += items[i];
        }

        result = sum;
    }
}
