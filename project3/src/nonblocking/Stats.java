/*   Filename: Stats.java, No STUDENT changes.
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Assignment: #3
  Class Stats compensates for the fact that the default Java library
  stinks at statistical analysis support, unlike Python. This collection
  of static methods also includes readCSV and writeCSV methods.
  It is tagged @Immutable because it maintains no internal state.
  Its static methods just map input parameters to return values,
  without mutating input parameters.
*/

package nonblocking ;
import java.util.Arrays ;
import java.util.HashMap ; // Not thread-safe but confined to calling thread.
import java.util.Map ;
import java.util.Set ;
import java.util.List ;
import java.io.FileWriter ;
import java.io.IOException ;
import net.jcip.annotations.* ;

@Immutable
public class Stats {
    public static double [] meanPopStdev(double [] values) {
        // return the mean in [0] and population standard dev in [1]
        double [] result = new double [ 2 ];
        double sum = 0.0 ;
        for (double v : values) {
            sum += v ;
        }
        result[0] = (sum / values.length) ;
        double variance = 0.0 ;
        for (double v : values) {
            double diff = v - result[0];    // - sign gets squared away
            variance = variance + (diff * diff) ;
        }
        int divisor = values.length ;  // values.length-1 for a sample stdev
        result[1] = Math.sqrt(variance / divisor);
        return(result);
    }
    public static double median(double [] values) {
        double result ;
        double [] copy = Arrays.copyOf(values, values.length);
        // values.clone() should work as well, haven't tested it.
        Arrays.parallelSort(copy);  // new to me!
        if ((copy.length & 1) == 1) {       // odd length, use middle value.
            int mid = copy.length / 2 ;     // middle, discards fraction .5
            result = copy[mid];
        } else {
            int upper = copy.length / 2 ;
            result = (copy[upper-1] + copy[upper]) / 2.0 ;
            // mean of middle two elements
        }
        return(result);
    }
    public static double mode(double [] values) {
        // returns Double.NaN if there is no unique mode
        HashMap<Double, Integer> counters = new HashMap<Double, Integer>();
        for (double v : values) {
            if (counters.containsKey(v)) {
                counters.put(v,counters.get(v)+1);
            } else {
                counters.put(v,1);
            }
        }
        Set<Map.Entry<Double,Integer>> pairs = counters.entrySet();
        double result = 0.0 ;
        int count = 0 ;
        boolean duplicate = false ;
        for (Map.Entry<Double,Integer> p : pairs) {
            Double pk = p.getKey();
            Integer pv = p.getValue() ;
            if (pv.intValue() > count) {    // guaranteed 1st time thru
                count = pv.intValue() ;
                result = pk.doubleValue();
                duplicate = false ;
            } else if (pv.intValue() == count) {
                duplicate = true ;
            } // else this value is not a contender 
        }
        if (duplicate) {
            result = Double.NaN ;
        }
        return(result);
    }
    public static double [] minmax(double [] values) {
        // Returns a two-element array with min at [0] and max at [1].
        double [] result = new double [ 2 ];
        result[0] = result[1] = values[0];
        for (double v : values) {
            if (v < result[0]) {
                result[0] = v ;
            }
            if (v > result[1]) {
                result[1] = v ;
            }
        }
        return(result);
    }
    public static void writeCSV(String filename, List<List<Object>> data)
            throws IOException {    // exception when cannot open
        FileWriter f = new FileWriter(filename);
        // Let toString() figure it out. Saving readCSV for when needed.
        for (List<Object> row : data) {
            writeCSVrow(f, row);
        }
        f.close();
    }
    public static void writeCSVrow(FileWriter f, List<Object> row) {
        boolean firstcell = true ;
        try {
            for (Object cell : row) {
                if (! firstcell) {
                    f.write(',');
                }
                f.write(cell.toString());
                firstcell = false ;
            }
            f.write('\n');
        } catch (java.io.IOException xxx) {
            System.err.println("IO Exception on write: "
                + xxx.getMessage());
            System.exit(1);
        }
    }
    public static void writeCSV(String filename, Object [][] data)
            throws IOException {    // exception when cannot open
        FileWriter f = new FileWriter(filename);
        // Let toString() figure it out. Saving readCSV for when needed.
        for (Object[] row : data) {
            writeCSVrow(f, row);
        }
        f.close();
    }
    public static void writeCSVrow(FileWriter f, Object [] row) {
        boolean firstcell = true ;
        try {
            for (Object cell : row) {
                if (! firstcell) {
                    f.write(',');
                }
                f.write(cell.toString());
                firstcell = false ;
            }
            f.write('\n');
        } catch (java.io.IOException xxx) {
            System.err.println("IO Exception on write: "
                + xxx.getMessage());
            System.exit(1);
        }
    }
}
