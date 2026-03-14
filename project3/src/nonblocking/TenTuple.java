/*   Filename: TenTuple.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: 
  Assignment: #3
  @see NonBlockingDataflow
  TenTuple is a "dumb" data container that students must make
  effectively immutable. The field values are immutable objects.
*/

package nonblocking ;
import net.jcip.annotations.* ;
import java.util.concurrent.Delayed ;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.atomic.AtomicLong ;

@Immutable
public class TenTuple implements Delayed {
    public final String distribution ; // ('uniform', 'normal', or 'exponential')
    public final int Param1, Param2, size ;
    public final double mean, median, mode, pstdev, min, max ;
    private final static AtomicLong serialNumbers = new AtomicLong();
    private final long serialno ;
    public TenTuple(String distribution, int Param1, int Param2, int size,
        double mean, double median, double mode, double pstdev,
        double min, double max) {
        this.distribution = distribution ;
        this.Param1 = Param1 ;
        this.Param2 = Param2 ;
        this.size = size ;
        this.mean = mean ;
        this.median = median ;
        this.mode = mode ;
        this.pstdev = pstdev ;
        this.min = min ;
        this.max = max ;
        serialno = serialNumbers.getAndIncrement();
    }
    public String toString() {
        return("TenTuple: " + distribution + "," + Param1 + "," + Param2 + ","
            + size + "," + mean + "," + median + "," + mode + ","
            + pstdev + "," + min + "," + max);
    }
    // The following methods added to make PriorityBlockQueue and
    // DelayQueue work in this project.
    public long getDelay(TimeUnit unit) {
        return(0L); // fake it, no delay
    }
    public int compareTo(Delayed o) {
        if (o instanceof TenTuple) {
            TenTuple t = (TenTuple) o ;
            if (serialno < t.serialno) {
                return -1 ;
            } else if (serialno > t.serialno) {
                return 1 ;
            } else {
                return 0 ;
            }
        }
        return 0 ;  // default case, incompatible, won't happen in this project
    }
} ;
