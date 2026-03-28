#!/bin/bash
# NonBlockingDataflow
# runs & tests NonBlockingDataflow

# STUDENT NAME FOR ASSIGNMENT 3: Vince Marinelli

set -x
export CLASSPATH=..:./jcip-annotations.jar
JVM=/usr/bin/java
JAVAFILE=bug

make build
status=$?
if [ $status -ne 0 ]
then
    echo "make build has failed" 1>&2
    exit $status
fi
JAVAFILE=NonBlockingDataflow

# STUDENT: Consult all concrete classes implementing Queue THAT ARE
# PROVIDED IN PACKAGE java.util.concurrent that support .offer() and
# .poll() non-blocking operations and add the ones that work
# immediately after ArrayBlockingQueue in this "for queue in" loop
# header. You can determine the ones that do not work either by
# reading documentation or by testing. ArrayBlockingQueue works. For
# the ones that do not work, add a comment block for each in the
# README 8b question at the bottom of this script file, documenting
# why each of them does not work in this pipeline framework. Shell
# script comment lines begin with the # character like this line.

# STUDENT add your queues names after ArrayBlockingQueue separated by spaces.
# The AtomicReference and AtomicReferenceArray entries are OK as given.
# I recommend trying the BlockingQueue classes that work for Assignent 2,
# noting that this Assignment 3 calls .offer() and .poll() on them,
# not .put() and .take(). They may work as non-blocking queues.
# Make sure to add any additional Queue classes you find in package
# java.util.concurrent. Make sure to try ALL the Queues Dr. Parson's
# solution to Assignment 2 got working, including some that failed
# for you with their default configuration.
# for queue in AtomicReference AtomicReferenceArray ArrayBlockingQueue ; do
for queue in AtomicReference AtomicReferenceArray ArrayBlockingQueue ConcurrentLinkedDeque \
 ConcurrentLinkedQueue LinkedBlockingDeque LinkedBlockingQueue LinkedTransferQueue PriorityBlockingQueue; do
	java -classpath build nonblocking.${JAVAFILE} 343458543 ${queue} 2
    status=$?
    if [ $status -ne 0 ]
    then
        echo "Running ${JAVAFILE} has failed" 1>&2
        exit $status
    fi
    head -1 ${queue}_a.csv > multi.csv 
    cat ${queue}_*.csv > tmp.csv
    # Assignment 3 uses sort because AtomicReferenceArray is not necessarily
    # FIFO.
    grep uniform tmp.csv | sort >> multi.csv 
    grep normal tmp.csv | sort >> multi.csv 
    grep exponential tmp.csv | sort >> multi.csv 
    diff --ignore-trailing-space --strip-trailing-cr multi.csv multi.ref > multi.dif 
    status=$?
    if [ $status -ne 0 ]
    then
        echo "multi.csv and multi.ref mismatch, see multi.dif"
        exit $status
    fi
    /bin/mv ${queue}.txt ${queue}_2.txt
    /bin/rm -f ${queue}_*.csv multi.csv tmp.csv
    done
exit 0  # success

# STUDENT: document any Queue classes that will not work here as
# comments.
# - DelayQueue - requires a collection that extends Delayed
# - SynchronousQueue - causes a deadlock at the cdLatch.await() command. This fails b/c we cannot guarantee
#    that poll will be called before offer.