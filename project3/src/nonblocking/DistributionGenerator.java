/*   Filename: DistributionGenerator.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: Vince Marinelli
  Class DistributionGenerator is the first pipeline stage
  that generates statistical distributions for downstream
  analysis.
*/

package nonblocking ;
import net.jcip.annotations.* ;
import java.lang.management.* ;
import java.io.PrintStream ;
import java.io.FileNotFoundException ;
import java.util.concurrent.CountDownLatch;

@ThreadSafe
public class DistributionGenerator implements Runnable {

// ['Distribution', 'Param1', 'Param2', 'Count', 'Values array']
    private final IMakeDistribution statsMaker ;
    private final int howManyTimesToYield ;
    private final INonBlockingTupleExchange<FiveTuple> exchanger ;
    @GuardedBy("printer")
    private final PrintStream printer ;

    private final CountDownLatch cdLatch;

    public DistributionGenerator(
        IMakeDistribution statsMaker,
        int howManyTimesToYield,
        INonBlockingTupleExchange<FiveTuple> exchanger,
        PrintStream printer,
        CountDownLatch cdLatch
    ) {
        this.statsMaker = statsMaker ;
        this.howManyTimesToYield = howManyTimesToYield ;
        this.exchanger = exchanger ;
        this.printer = printer ;
        this.cdLatch = cdLatch;
    }

    public void run() {
        try {
            // Runs service thread only in multi-threaded case.
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long startCPU = bean.getCurrentThreadCpuTime();
            long startUser = bean.getCurrentThreadUserTime();

            for (int i = 0; i < howManyTimesToYield; i++) {
                run1iteration();
            }
            double allCPU = ((double) bean.getCurrentThreadCpuTime()
                    - (double) startCPU) / 1000000000d;
            double userCPU = ((double) bean.getCurrentThreadUserTime()
                    - (double) startUser) / 1000000000d;
            // Since there is only 1 unique printer object, this implicit
            // lock serializes any attempted concurrent use of it.
            // Serialize access to the printer which is not thread-safe.
            synchronized (printer) {
                printer.println("\nDistributionGenerator "
                        + statsMaker.getClass().getName()
                        + " CPU TIME "
                        + allCPU + ", USER CPU " + userCPU + " seconds");
                printer.flush();
            }
        }
        finally {
            cdLatch.countDown();
        }

    }

    void run1iteration() {      // needed for single threaded testing
        // Runs service thread or called directly in main-thread-only.
        FiveTuple message = statsMaker.returnDistribution();
        exchanger.spinUntilPut(message);
    }
}
