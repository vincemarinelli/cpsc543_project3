/*   Filename: StatisticalAnalysisCSVSaver.java
  ************************************************************
  Author:     Dr. Parson
  Student co-author: Vince Marinelli
  Assignment: #3
  Class StatisticalAnalysisCSVSaver is the first pipeline stage
  that generates statistical distributions for downstream
  analysis.
*/

package nonblocking ;
import java.io.FileWriter ;
import java.io.IOException ;
import net.jcip.annotations.* ;
import java.lang.management.* ;
import java.io.PrintStream ;
import java.io.FileNotFoundException ;
import java.util.concurrent.CountDownLatch;

@ThreadSafe
public class StatisticalAnalysisCSVSaver implements Runnable {
// ['Distribution', 'Param1', 'Param2', 'Count', 'Mean', 'Median', 'Mode',
//     'Pstdev', 'Min', 'Max']
    private final int howManyTimesToYield ;
    private final INonBlockingTupleExchange<TenTuple> exchanger ;
    private final String CSVfilename ;
    @GuardedBy("printer")
    private final PrintStream printer ;
    private final FileWriter fwrt ;

    private final CountDownLatch cdLatch;

    public StatisticalAnalysisCSVSaver(int howManyTimesToYield,
        INonBlockingTupleExchange<TenTuple> exchanger, String CSVfilename,
        PrintStream printer,
        CountDownLatch cdLatch
    ) throws java.io.IOException {
        this.howManyTimesToYield = howManyTimesToYield ;
        this.exchanger = exchanger ;
        this.CSVfilename = CSVfilename ;
        this.printer = printer ;
        fwrt = new FileWriter(CSVfilename);
        Stats.writeCSVrow(fwrt, header);
        fwrt.flush();   // run() is in a different thread than constructor.
        this.cdLatch = cdLatch;
    }

    private static Object [] header =
        {"Distribution", "Param1", "Param2", "Count",
            "Mean", "Median", "Mode", "Pstdev", "Min", "Max"};

    public void run() {
        try {
            // Runs service thread only in multi-threaded case.
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            long startCPU = bean.getCurrentThreadCpuTime();
            long startUser = bean.getCurrentThreadUserTime();
            // Runs service thread or called directly in main-thread-only.
            for (int i = 0; i < howManyTimesToYield; i++) {
                run1iteration(i == (howManyTimesToYield - 1));
            }
            double allCPU = ((double) bean.getCurrentThreadCpuTime()
                    - (double) startCPU) / 1000000000d;
            double userCPU = ((double) bean.getCurrentThreadUserTime()
                    - (double) startUser) / 1000000000d;
            // Serialize access to the printer which is not thread-safe.
            synchronized (printer) {
                printer.println("\nStatisticalAnalysisCSVSaver CPU TIME "
                        + allCPU + ", USER CPU " + userCPU + " seconds");
                printer.flush();
            }
        }
        finally {
            cdLatch.countDown();
        }
    }

    void run1iteration(boolean isCloseCSV) {
        // needed for single threaded testing
        TenTuple inmessage = null ;
        inmessage = exchanger.spinUntilTake();
        Double mode = inmessage.mode;
        Object [] row = new Object [ 10 ];
        row[0] = inmessage.distribution ;
        row[1] = inmessage.Param1;
        row[2] = inmessage.Param2;
        row[3] = inmessage.size;
        row[4] = (double) Math.round(inmessage.mean); // round 0 places
        row[5] = (double) Math.round(inmessage.median);
        row[6] = mode.isNaN() ? "" : (double) Math.round(inmessage.mode);
        row[7] = (double) Math.round(inmessage.pstdev);
        row[8] = (double) Math.round(inmessage.min);
        row[9] = (double) Math.round(inmessage.max);
        Stats.writeCSVrow(fwrt, row);
        if (isCloseCSV) {
            try {
                fwrt.close();
            } catch (java.io.IOException xxx) {
                System.err.println("IO Exception on close " + CSVfilename
                    + ": " + xxx.getMessage());
                System.exit(1);
            }
        }
    }
}
