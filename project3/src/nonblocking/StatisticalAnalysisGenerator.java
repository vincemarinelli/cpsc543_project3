/*   Filename: StatisticalAnalysisGenerator.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: Vince Marinelli
  Assignment: #3
  Class StatisticalAnalysisGenerator is the 2nd, middle pipeline stage
  that generates statistical distributions for downstream analysis.
  It consumes FiveTuple and produces TenTuple objects.
*/

package nonblocking ;
import net.jcip.annotations.* ;
import java.lang.management.* ;
import java.io.PrintStream ;
import java.io.FileNotFoundException ;
import java.util.concurrent.CountDownLatch;

@ThreadSafe
public class StatisticalAnalysisGenerator implements Runnable {
// ['Distribution', 'Param1', 'Param2', 'Count', 'Mean', 'Median', 'Mode',
//     'Pstdev', 'Min', 'Max']
    private final int howManyTimesToYield ;
    private final INonBlockingTupleExchange<FiveTuple> exchanger5 ;
    private final INonBlockingTupleExchange<TenTuple> exchanger10 ;
    @GuardedBy("printer")
    private final PrintStream printer ;

    private final CountDownLatch cdLatch;


    public StatisticalAnalysisGenerator(int howManyTimesToYield,
        INonBlockingTupleExchange<FiveTuple> exchanger5,
        INonBlockingTupleExchange<TenTuple> exchanger10,
        PrintStream printer,
        CountDownLatch cdLatch
    ) {
        this.howManyTimesToYield = howManyTimesToYield ;
        this.exchanger5 = exchanger5 ;
        this.exchanger10 = exchanger10 ;
        this.printer = printer ;
        this.cdLatch = cdLatch;
    }

    public void run() {
        try {
            // Runs service thread only in multi-threaded case.
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long startCPU = bean.getCurrentThreadCpuTime();
            long startUser = bean.getCurrentThreadUserTime();
            for (int i = 0 ; i < howManyTimesToYield ; i++) {
                run1iteration();
            }
            double allCPU = ((double) bean.getCurrentThreadCpuTime()
                - (double) startCPU) / 1000000000d ;
            double userCPU = ((double) bean.getCurrentThreadUserTime()
                - (double) startUser) / 1000000000d ;
            // Serialize access to the printer which is not thread-safe.
            synchronized(printer) {
                printer.println("\nStatisticalAnalysisGenerator CPU TIME "
                        + allCPU + ", USER CPU " + userCPU + " seconds");
                printer.flush();
            }
        }
        finally {
            cdLatch.countDown();
        }
    }

    void run1iteration() {      // needed for single threaded testing
        FiveTuple inmessage = null ;
        inmessage = exchanger5.spinUntilTake();
        double [] valuesCopy = inmessage.getValues();
        double [] meanStdev = Stats.meanPopStdev(valuesCopy);
        double median = Stats.median(valuesCopy);
        double mode = Stats.mode(valuesCopy); // may be NaN
        double [] minmax = Stats.minmax(valuesCopy);
        TenTuple outmessage = new TenTuple(inmessage.distribution,
            inmessage.Param1, inmessage.Param2, inmessage.size,
            meanStdev[0], median, mode, meanStdev[1], minmax[0],
            minmax[1]);
        exchanger10.spinUntilPut(outmessage);
    }
}
