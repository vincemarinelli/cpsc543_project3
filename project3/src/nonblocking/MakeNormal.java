/*   Filename: MakeNormal.java
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: 
  Assignment: #3
  Class MakeNormal makes and stores a Gaussian distribution
  @see NonBlockingDataflow
  @see FiveTuple
*/

package nonblocking ;
import java.util.Random ; // for all three distributions.
// ThreadLocalRandom might be better if we could seed it.
import net.jcip.annotations.* ;

@ThreadSafe
public class MakeNormal implements IMakeDistribution {
    /*
     * This is a Gaussian random distribution generator.
    */
    private final int Loc ;
    private final int scale ;
    private final int size ;
    private final Random mygenerator ;
    public MakeNormal(int Loc, int scale, int size, int seed) {
        /*
         * Construct a uniform random distribution generator.
         * Loc is the Mean (“centre”) of the distribution.
         * scale is the Standard deviation of the distribution.
         * size is the count of values returned by returnUniform().
         * seed is the seed for the generator.
        */
        this.Loc = Loc ;
        this.scale = scale ;
        this.size = size ;
        this.mygenerator = new Random(seed);
    }
    public FiveTuple returnDistribution() {
        /*
         * returns a 5-tuple with ('uniform', Loc, scale, size,
         *  an array of size uniform doubles.
        */
        double [] values = new double [ size ];
        for (int i = 0 ; i < values.length ; i++) {
            values[i] = (mygenerator.nextGaussian() + 1.0)
                // At this point it is in range [0.0, 2.0] with mean at 1.0
                * Loc ; // Move mean to Loc. Figure out stdev mapping later.
        }
        return(new FiveTuple("normal", Loc, scale, size, values));
    }
}
