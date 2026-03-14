/*   Filename: MakeUniform.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: 
  Major:      Professor
  Assignment: #3
  Class MakeUniform makes and stores a Uniform distribution
  @see NonBlockingDataflow
  @see FiveTuple
*/

package nonblocking ;
import java.util.Random ; // for all three distributions.
// ThreadlowalRandom might be better if we could seed it.
import net.jcip.annotations.* ;

@ThreadSafe
public class MakeUniform implements IMakeDistribution {
    /*
     * This is a uniform random distribution generator.
    */
    private final int low ;
    private final int high ;
    private final int size ;
    private final double range ;
    private final Random mygenerator ;
    public MakeUniform(int low, int high, int size, int seed) {
        /*
         * Construct a uniform random distribution generator.
         * low is the bottom of the distribution inclusive.
         * high is the top of the distribution exclusive.
         * size is the count of values returned by returnUniform().
         * seed is the seed for the generator.
        */
        this.low = low ;
        this.high = high ;
        this.size = size ;
        this.mygenerator = new Random(seed);
        range = high - low ;
    }
    public FiveTuple returnDistribution() {
        /*
         * returns a 5-tuple with ('uniform', low, high, size,
         *  an array of size uniform doubles.
        */
        double [] values = new double [ size ];
        for (int i = 0 ; i < values.length ; i++) {
            values[i] = mygenerator.nextDouble()
                // At this point it is in range [0.0, 1.0):
                * range + low ;
        }
        return(new FiveTuple("uniform", low, high, size, values));
    }
}
