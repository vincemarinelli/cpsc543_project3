/*   Filename: FiveTuple.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: Vince Marinelli
  Assignment: # 3
  FiveTuple is a "dumb" data container that students must make
  effectively immutable. The values field is mutable, so students
  must update the getValues() method to return a copy of the array.
  Establishing a convention where the producer and consumer of the
  values array never modify it after construction would run OK if
  followed, but FiveTuple would not be effectively immutable.
  We will pay the overhead of one array copy per FiveTuple object
  in a pipeline to support effective immutability.
*/

package nonblocking ;
import net.jcip.annotations.* ;
import java.util.Arrays ;
import java.util.concurrent.Delayed ;
import java.util.concurrent.TimeUnit ;
import java.util.concurrent.atomic.AtomicLong ;

@Immutable
public class FiveTuple implements Delayed {
    public final String distribution ; // ('uniform', 'normal', or 'exponential')
    public final int Param1, Param2, size ;
    private final double [] values ;
    private final static AtomicLong serialNumbers = new AtomicLong();
    private final long serialno ;
    public FiveTuple(String distribution, int Param1, int Param2, int size,
        double [] values) {
        this.distribution = distribution ;
        this.Param1 = Param1 ;
        this.Param2 = Param2 ;
        this.size = size ;
        this.values = values ;
        serialno = serialNumbers.getAndIncrement();
    }
    public double [] getValues() {
        return Arrays.copyOf(values, values.length) ;
    }
    public String toString() {
        return("FiveTuple: " + distribution + "," + Param1 + "," + Param2 + ","
            + size + "," + values);
    }
    // The following methods added to make PriorityBlockQueue and
    // DelayQueue work in this project.
    public long getDelay(TimeUnit unit) {
        return(0L); // fake it, no delay
    }
    public int compareTo(Delayed o) {
        if (o instanceof FiveTuple) {
            FiveTuple f = (FiveTuple) o ;
            if (serialno < f.serialno) {
                return -1 ;
            } else if (serialno > f.serialno) {
                return 1 ;
            } else {
                return 0 ;
            }
        }
        return 0 ;  // default case, incompatible, won't happen in this project
    }
} ;
