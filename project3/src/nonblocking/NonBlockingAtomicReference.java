/*   Filename: NonBlockingAtomicReference.java,
 *   STUDENT must implement this class.
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
import java.util.concurrent.atomic.AtomicReference ;

@ThreadSafe
public class NonBlockingAtomicReference<TupleType>
        implements INonBlockingTupleExchange<TupleType> {

    private final AtomicReference<TupleType> ref;
    private final TupleType originalValue;

    /* TupleType will be FiveTuple or TenTuple in this assignment. */
    public NonBlockingAtomicReference(AtomicReference<TupleType> q) {
        this.ref = q;
        this.originalValue = q.get();
    }

    public void spinUntilPut(TupleType tuple) {
        while (true) {
            if (this.ref.compareAndSet(originalValue, tuple)) {
                return;
            }
        }
    }

    public TupleType spinUntilTake() {
        while (true) {
            TupleType value = this.ref.getAndSet(originalValue);
            if (value != originalValue) {
                return value;
            }
        }
    }
}
