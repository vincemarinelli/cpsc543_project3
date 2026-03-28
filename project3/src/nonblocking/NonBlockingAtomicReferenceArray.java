/*   Filename: NonBlockingAtomicReferenceArray.java,
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

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicReferenceArray ;

@ThreadSafe
public class NonBlockingAtomicReferenceArray<TupleType>
        implements INonBlockingTupleExchange<TupleType> {

    private final AtomicReferenceArray<TupleType> originalArray;
    private final TupleType[] originalValues;

    /* TupleType will be FiveTuple or TenTuple in this assignment. */
    public NonBlockingAtomicReferenceArray(AtomicReferenceArray<TupleType> q) {
        this.originalArray = q;
        originalValues = (TupleType[]) new Object[q.length()];
        for (int i = 0; i < originalArray.length(); i++) {
                this.originalValues[i] = q.get(i);
            }
    }

    public void spinUntilPut(TupleType tuple) {
        while (true) {
            for (int i = 0; i < originalArray.length(); i++) {
                if (originalArray.compareAndSet(i, originalValues[i], tuple)) {
                    return;
                }
            }
        }
    }

    public TupleType spinUntilTake() {
        while (true) {
            for (int i = 0; i < originalArray.length(); i++) {
                TupleType value = originalArray.getAndSet(i, originalValues[i]);
                if (value != originalValues[i]) {
                    return value;
                }
            }
        }
    }
}
