/*   Filename: IMakeDistribution.java, no STUDENT changes needed.
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: Vince Marinelli
  Assignment: #3
  Interface IMakeDistribution for MakeUniform, MakeNormal, & MakeExponential.
  @see NonBlockingDataflow
  @see FiveTuple
*/

package nonblocking ;

public interface IMakeDistribution {
    FiveTuple returnDistribution() ;
    /* Return a "uniform", "normal", or "exponential" distribution. */
}
