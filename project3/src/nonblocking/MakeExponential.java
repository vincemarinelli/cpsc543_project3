/*   Filename: MakeExponential.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: Vince Marinelli
  Assignment: #3
  Class MakeExponential makes and stores an Exponential distribution
  @see NonBlockingDataflow
  @see FiveTuple
*/

package nonblocking ;
import java.util.Random ; // for all three distributions.
// https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Random.html
// ThreadlowalRandom might be better if we could seed it.
// https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/ThreadlowalRandom.html
// https://docs.oracle.com/javase/8/docs/api/index.html?java/util/DoubleSummaryStatistics.html
import net.jcip.annotations.* ;

@ThreadSafe
public class MakeExponential implements IMakeDistribution {
    /*
     * This is an exponential random distribution generator.
     * See makeExponential in CSC223f23DataflowAssn4.py. Modeled after:
     * https://numpy.org/doc/stable/reference/random/generated/numpy.random.Generator.exponential.html
     * https://forums.oracle.com/ords/apexds/post/random-integers-with-exponential-distribution-9977
    */
    private final int scale ;
    private final int size ;
    private final Random mygenerator ;
    public MakeExponential(int scale, int size, int seed) {
        /*
         * Construct a exponential random distribution generator.
         * scale is the halfway point for instance count, 1.0/lambda
         * size is the count of values returned by returnExponential().
         * seed is the seed for the generator.
        */
        this.scale = scale ;
        this.size = size ;
        this.mygenerator = new Random(seed);
    }
    public FiveTuple returnDistribution() {
        /*
         * returns a 5-tuple with ('exponential', scale, -1 (IGNORE), size,
         *  an array of size exponential doubles.
        */
        double [] values = new double [ size ];
        for (int i = 0 ; i < values.length ; i++) {
            values[i] = (-scale * Math.log(mygenerator.nextDouble()));
        }
        return(new FiveTuple("exponential", scale, 0, size, values));
    }
}
