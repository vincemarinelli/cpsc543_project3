/*   Filename: NonBlockingQueue.java, STUDENT must implement this class.
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: 
  Assignment: #3
  Interface INonBlockingTupleExchange for NonBlockingQueue,
    NonBlockingAtomicReference, and NonBlockingAtomicReferenceArray
    implementing classes.
*/

package nonblocking ;
import net.jcip.annotations.* ;
import java.util.Queue ;

public class NonBlockingQueue<TupleType>
        implements INonBlockingTupleExchange<TupleType> {
    /* TupleType will be FiveTuple or TenTuple in this assignment. */
    public NonBlockingQueue(Queue<TupleType> q) {

    }

    public void spinUntilPut(TupleType tuple) {

    }

    public TupleType spinUntilTake() {
        return null ;
    }
}
