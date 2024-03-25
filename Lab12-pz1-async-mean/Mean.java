import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mean {
    static BlockingQueue<Double> results = new ArrayBlockingQueue<Double>(100);
    static double[] array;
    Mean(double[] array){
        Mean.array = array;
    }
    static void initArray(int size){
        array = new double[size];
        for(int i=0;i<size;i++){
            array[i]= Math.random()*size/(i+1);
        }
    }

    static class MeanCalc extends Thread{
        private final int start;
        private final int end;
        double mean = 0;

        MeanCalc(int start, int end){
            this.start = start;
            this.end=end;
        }
        public void run(){
            for(int i = start; i <end; i++) {
                mean += array[i];
            }
            mean /= (end - start);
            try {
                results.put(mean);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
        }

    }
    /**
     * Oblicza średnią wartości elementów tablicy array uruchamiając równolegle działające wątki.
     * Wypisuje czasy operacji
     * @param cnt - liczba wątków
     */
    static void parallelMean(int cnt) throws InterruptedException {
        // utwórz tablicę wątków
        MeanCalc threads[]=new MeanCalc[cnt];
        double mean = 0;
        // utwórz wątki, podziel tablice na równe bloki i przekaż indeksy do wątków
        int lengthOfDividedArray = array.length/cnt;
        for (int i = 0; i < cnt;i++){
            int begin = i*lengthOfDividedArray;
            int endOfDividedArray = begin + lengthOfDividedArray;

            threads[i] = new MeanCalc(begin,endOfDividedArray);
        }

        double t1 = System.nanoTime()/1e6;

        for (MeanCalc thread: threads) {
            thread.start();
        }

        double t2 = System.nanoTime()/1e6;

        // czekaj na ich zakończenie używając metody ''join''
//        for(MeanCalc mc:threads) {
//            mc.join();
//        }
        // oblicz średnią ze średnich
//        for(MeanCalc mc:threads) {
//            mean += mc.mean;
//        }
//        mean /= cnt;

        // Wywołanie take()
        double meanV2 = 0;
        for(int i =0; i<cnt; i++) {
            meanV2 += results.take();
        }
        meanV2 /= cnt;

        double t3 = System.nanoTime()/1e6;
//        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
//                array.length,
//                cnt,
//                t2-t1,
//                t3-t1, mean);
//        System.out.println();
        System.out.printf(Locale.US,"MeanV2 result = size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                t3-t1, meanV2);
        System.out.println();
    }

    static void parallelMeanV3(int cnt) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(16);

        int lengthOfDividedArray = array.length/cnt;
        double t1 = System.nanoTime()/1e6;

        for(int i=0;i<cnt;i++){
            int begin = i*lengthOfDividedArray;
            int endOfDividedArray = begin + lengthOfDividedArray;

            executor.execute(new MeanCalc(begin,endOfDividedArray));
        }

        double t2 = System.nanoTime()/1e6;

        executor.shutdown();

        double meanV3 = 0;
        for(int i =0; i<cnt; i++) {
            meanV3 += results.take();
        }
        meanV3 /= cnt;

        double t3 = System.nanoTime()/1e6;

        System.out.printf(Locale.US,"meanV3 : size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                t3-t1, meanV3);
        System.out.println();
    }




    public static void main(String[] args) throws InterruptedException {
//        initArray(100000000);
//        parallelMean(10);
//        System.out.println();
//        parallelMean(100);
//        System.out.println();
//        parallelMeanV3(10);

        initArray(128000000);
        for(int cnt:new int[]{1,2,4,8,16,32,64,128}){
            parallelMean(cnt);
        }


    }
}
