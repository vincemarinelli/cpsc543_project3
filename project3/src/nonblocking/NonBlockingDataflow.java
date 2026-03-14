/*   Filename: NonBlockingDataflow
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: 
  Assignment: #3
  Class NonBlockingDataflow is the main multi-threaded test driver for
  assignment 3's pipeline. This driver class is @Immutable because it
  shares no state outside main. Thread safety of the pipeline it
  builds depends on the pipeline's classes.
*/

package nonblocking ;
import net.jcip.annotations.* ;
import java.io.PrintStream ;
import java.io.FileNotFoundException ;
import java.util.Queue ;
import java.util.concurrent.atomic.AtomicReference ;
import java.util.concurrent.atomic.AtomicReferenceArray ;
import java.lang.reflect.* ;   // We look up the name of the Queue.

@Immutable
public class NonBlockingDataflow {
    public static void main(String [] args) {
        String usage =
            "USAGE: java NonBlockingDataflow SEED QueueOrAtomic 2(threads)";
        if (args.length != 3) {
            System.err.println(usage);
            System.exit(1);
        }
        PrintStream printer = null ;
        int seed = 0 ;
        String outfilename = "" ;
        boolean isthreads = true ;
        String QNAME = null ;
        INonBlockingTupleExchange<FiveTuple> q5a = null ;
        INonBlockingTupleExchange<TenTuple> q10a = null ;
        INonBlockingTupleExchange<FiveTuple> q5b = null ;
        INonBlockingTupleExchange<TenTuple> q10b = null ;
        INonBlockingTupleExchange<FiveTuple> q5c = null ;
        INonBlockingTupleExchange<TenTuple> q10c = null ;
        QNAME = args[1];
        try {
            String logfilename = QNAME + ".txt";
            printer = new PrintStream(logfilename);
            isthreads = Integer.parseInt(args[2]) == 2 ;
            seed = Integer.parseInt(args[0]);
        } catch (Exception xyz) {
            System.err.println("Invalid command line argument: "
                + xyz.getMessage());
            System.err.println(usage);
            System.exit(1);
        }
        if (QNAME.equals("AtomicReference")) {
            q5a = new NonBlockingAtomicReference<FiveTuple>(
                new AtomicReference<FiveTuple>());
            q10a = new NonBlockingAtomicReference<TenTuple>(
                new AtomicReference<TenTuple>());
            q5b = new NonBlockingAtomicReference<FiveTuple>(
                new AtomicReference<FiveTuple>());
            q10b = new NonBlockingAtomicReference<TenTuple>(
                new AtomicReference<TenTuple>());
            q5c = new NonBlockingAtomicReference<FiveTuple>(
                new AtomicReference<FiveTuple>());
            q10c = new NonBlockingAtomicReference<TenTuple>(
                new AtomicReference<TenTuple>());
        } else if (QNAME.equals("AtomicReferenceArray")) {
            q5a = new NonBlockingAtomicReferenceArray<FiveTuple>(
                new AtomicReferenceArray<FiveTuple>(2));
            q10a = new NonBlockingAtomicReferenceArray<TenTuple>(
                new AtomicReferenceArray<TenTuple>(2));
            q5b = new NonBlockingAtomicReferenceArray<FiveTuple>(
                new AtomicReferenceArray<FiveTuple>(2));
            q10b = new NonBlockingAtomicReferenceArray<TenTuple>(
                new AtomicReferenceArray<TenTuple>(2));
            q5c = new NonBlockingAtomicReferenceArray<FiveTuple>(
                new AtomicReferenceArray<FiveTuple>(2));
            q10c = new NonBlockingAtomicReferenceArray<TenTuple>(
                new AtomicReferenceArray<TenTuple>(2));
        } else {
            try {
                Constructor<Queue> cons = null ;
                Class Qclass = Class.forName("java.util.concurrent." + QNAME);
                Class [] intparam = new Class [1];
                intparam[0] = int.class ;
                Class [] noparam = new Class [0];
                Object [] intarg = new Object [1];
                intarg[0] = 2 ;
                Object [] noarg = new Object [0];
                // Queue construction starts here.
                try {
                    cons = Qclass.getConstructor(intparam);
                    q5a = new NonBlockingQueue<FiveTuple>(
                        (Queue<FiveTuple>) cons.newInstance(intarg));
                    q10a = new NonBlockingQueue<TenTuple>(
                        (Queue<TenTuple>) cons.newInstance(intarg));
                    q5b = new NonBlockingQueue<FiveTuple>(
                        (Queue<FiveTuple>) cons.newInstance(intarg));
                    q10b = new NonBlockingQueue<TenTuple>(
                        (Queue<TenTuple>) cons.newInstance(intarg));
                    q5c = new NonBlockingQueue<FiveTuple>(
                        (Queue<FiveTuple>) cons.newInstance(intarg));
                    q10c = new NonBlockingQueue<TenTuple>(
                        (Queue<TenTuple>) cons.newInstance(intarg));
                } catch (Exception noparams) {
                    cons = Qclass.getConstructor(noparam);
                    q5a = new NonBlockingQueue<FiveTuple>(
                        (Queue<FiveTuple>) cons.newInstance(noarg));
                    q10a = new NonBlockingQueue<TenTuple>(
                        (Queue<TenTuple>) cons.newInstance(noarg));
                    q5b = new NonBlockingQueue<FiveTuple>(
                        (Queue<FiveTuple>) cons.newInstance(noarg));
                    q10b = new NonBlockingQueue<TenTuple>(
                        (Queue<TenTuple>) cons.newInstance(noarg));
                    q5c = new NonBlockingQueue<FiveTuple>(
                        (Queue<FiveTuple>) cons.newInstance(noarg));
                    q10c = new NonBlockingQueue<TenTuple>(
                        (Queue<TenTuple>) cons.newInstance(noarg));
                }
                // Queue construction is done here.
            } catch (Exception xxx) {
                System.err.println("Invalid command line argument: "
                    + xxx.getMessage());
                System.err.println(usage);
                System.exit(1);
            }
        }
        final int size = 10000 ;
        final int iterations = 5000 ;
        MakeUniform munif = new MakeUniform(0, 100, size, seed);
        MakeNormal mnorm = new MakeNormal(50, 15, size, seed);
        // ^^^ Java library ignores the 15 standard dev.
        // I need to find a better Java stats library some day.
        MakeExponential mexp = new MakeExponential(10, size, seed);
        // ^^^ put mean at 10ish
        Thread [] runners = new Thread [ 9 ] ;

        DistributionGenerator dgen1 = new DistributionGenerator(munif,
            iterations, q5a, printer);
        DistributionGenerator dgen2 = new DistributionGenerator(mnorm,
            iterations, q5b, printer);
        DistributionGenerator dgen3 = new DistributionGenerator(mexp,
            iterations, q5c, printer);
        StatisticalAnalysisGenerator statgen_a
            = new StatisticalAnalysisGenerator(iterations, q5a, q10a, printer);
        StatisticalAnalysisGenerator statgen_b
            = new StatisticalAnalysisGenerator(iterations, q5b, q10b, printer);
        StatisticalAnalysisGenerator statgen_c
            = new StatisticalAnalysisGenerator(iterations, q5c, q10c, printer);
        // StatisticalAnalysisCSVSaver() now re-throws java.io.IOException
        // so an aborted creation of a FileWriter can avoid volatile file.
        StatisticalAnalysisCSVSaver csvgen_a = null ;
        StatisticalAnalysisCSVSaver csvgen_b = null ;
        StatisticalAnalysisCSVSaver csvgen_c = null ;
        try {
            csvgen_a = new StatisticalAnalysisCSVSaver(
                iterations, q10a, QNAME + "_a.csv", printer);
            csvgen_b = new StatisticalAnalysisCSVSaver(
                iterations, q10b, QNAME + "_b.csv", printer);
            csvgen_c = new StatisticalAnalysisCSVSaver(
                iterations, q10c, QNAME + "_c.csv", printer);
        } catch (java.io.IOException iox) {
            System.err.println("ERROR opening output CSV file: "
                + iox.getMessage());
            System.exit(1);
        }
        printer.println("RECORDING TIMES FOR NonBlockingDataflow "
            + "threads"
            + ", connector is " + q5a.getClass().getName());
        printer.flush();    // no sync needed because no thread contention.
        long startTime = System.currentTimeMillis();
        if (isthreads) {
            runners[0] = new Thread(dgen1);
            runners[1] = new Thread(dgen2);
            runners[2] = new Thread(dgen3);
            runners[3] = new Thread(statgen_a);
            runners[4] = new Thread(statgen_b);
            runners[5] = new Thread(statgen_c);
            runners[6] = new Thread(csvgen_a);
            runners[7] = new Thread(csvgen_b);
            runners[8] = new Thread(csvgen_c);
            for (Thread t : runners) {
                t.start();
            }
            for (Thread t : runners) {
                try {
                    t.join();
                } catch (InterruptedException xx) {
                }
            }
        } else {
            throw new RuntimeException(
            "No-thread testing not used in NonBlockingDataflow");
        }
        double endTime = (System.currentTimeMillis() - (double)startTime)
            / 1000d ;
        printer.println("\nTOTAL ELAPSED TIME FOR PIPELINE = "
            + endTime + " secs.");
        printer.close();    // no sync needed because no thread contention.
    }
}
