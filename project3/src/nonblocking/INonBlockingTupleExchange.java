/*   Filename: INonBlockingTupleExchange.java, no STUDENT changes needed.
  ************************************************************
  Author:     Dr. Parson (modified Spring 2026 by Dr. Schwesinger)
  Student co-author: 
  Major:      Professor
  Assignment: #3
  Interface INonBlockingTupleExchange for NonBlockingQueue,
    NonBlockingAtomicReference, and NonBlockingAtomicReferenceArray
    implementing classes.
  @see NonBlockingDataflow
  @see FiveTuple
*/

package nonblocking ;

public interface INonBlockingTupleExchange<TupleType> {
    /* TupleType will be FiveTuple or TenTuple in this assignment. */
    void spinUntilPut(TupleType tuple) ;
    /*
     * spinUntilPut spins until it succeeds in calling non-blocking
     * offer() into a supporting Queue<TupleType> or performs successful insertion
     * into an AtomicReference<TupleType> or AtomicReferenceArray<TupleType>
     * via compareAndSet().
    */
    TupleType spinUntilTake();
    /*
     * spinUntilTake spins until it succeeds in calling non-blocking
     * poll() out of a supporting Queue<TupleType> or performs successful retrieval
     * from an AtomicReference<TupleType> or AtomicReferenceArray<TupleType>
     * via getAndSet().
    */
}
