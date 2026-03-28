/*   Filename: NonBlockingQueue.java, STUDENT must implement this class.
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: Vince Marinelli
  Assignment: #3
  Interface INonBlockingTupleExchange for NonBlockingQueue,
    NonBlockingAtomicReference, and NonBlockingAtomicReferenceArray
    implementing classes.
*/


package nonblocking ;
import net.jcip.annotations.* ;
import java.util.Queue ;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class NonBlockingQueue<TupleType>
        implements INonBlockingTupleExchange<TupleType> {

    private final Queue<TupleType> ref;

    /* TupleType will be FiveTuple or TenTuple in this assignment. */
    public NonBlockingQueue(Queue<TupleType> q) {
        this.ref = q;
    }

    public void spinUntilPut(TupleType tuple) {
        while (true) {
            if (this.ref.offer(tuple)) {
                return;
            }
        }
    }

    public TupleType spinUntilTake() {
        while (true) {
            TupleType value = this.ref.poll();
            if (value != null) {
                return value;
            }
        }
    }
}
