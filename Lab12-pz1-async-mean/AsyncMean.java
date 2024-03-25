import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsyncMean {
    static double[] array;

    static void initArray(int size) {
        array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = Math.random() * size / (i + 1);
        }

    }

    static class MeanCalcSupplier implements Supplier<Double> {
        private final int start;
        private final int end;
        double mean = 0;

        MeanCalcSupplier(int start, int end){
            this.start = start;
            this.end=end;
        }

        @Override
        public Double get() {
            // oblicz średnią
            for(int i = start; i < end; i++) {
                mean += array[i];
            }
            mean /= (end - start);

            System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
            return mean;
        }
    }
    public static void asyncMeanv1() {
        int size = 100_000_000;
        initArray(size);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        int n = 10;
        double t1 = System.nanoTime()/1e6;
        List<CompletableFuture<Double>> partialResults = new ArrayList<>();
        int lengthOfDividedArray = array.length/n;
        for(int i=0;i<n;i++){
            int begin = i*lengthOfDividedArray;
            int endOfDividedArray = begin + lengthOfDividedArray;
            CompletableFuture<Double> partialMean = CompletableFuture.supplyAsync(
                    new MeanCalcSupplier(begin,endOfDividedArray),executor);
            partialResults.add(partialMean);
        }
        double mean=0;
        for(var pr:partialResults){
            mean += pr.join();
        }
        mean /= n;
        double t2 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"AsynMeanV1: t2-t1 = %f ,mean=%f\n",t2-t1,mean);

        executor.shutdown();
    }

    static void asyncMeanv2() throws InterruptedException {
        int size = 100_000_000;
//        initArray(size);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        int n=10;

        double t1 = System.nanoTime()/1e6;
        BlockingQueue<Double> queue = new ArrayBlockingQueue<>(n);

        int lengthOfDividedArray = array.length/n;

        for (int i = 0; i < n; i++) {
            int begin = i*lengthOfDividedArray;
            int endOfDividedArray = begin + lengthOfDividedArray;
            CompletableFuture.supplyAsync(
                    new MeanCalcSupplier(begin,endOfDividedArray), executor)
            .thenApply(d -> queue.offer(d));
        }

        double mean=0;

        for(int i = 0; i < n; i++){
            mean += queue.take();
        }
        mean /= n;
        double t2 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"AsynMeanV2: t2-t1 = %f ,mean=%f\n",t2-t1,mean);

        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        asyncMeanv1();
        System.out.println();
        asyncMeanv2();

    }
}