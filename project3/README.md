# Non-blocking Queues

## Get the Assignment Code
Login to your university Unix account and copy the assignment code to a location under your home directory:

cp -r /export/home/public/schwesin/cpsc543/assignments/project3 LOCATION
where LOCATION is a directory of your choosing.

## Overview
Perform all test execution on arya to avoid any platform-dependent output differences.

This assignment is an extension of the previous assignment. Here we are replacing the blocking queues with non-blocking queues. The specifications are:

(45 points, 15 points each): Implement the classes NonBlockingQueue, NonBlockingAtomicReference, and NonBlockingAtomicReferenceArray.

(40 points) Synchronize the pipelines with a java.util.concurrent.CountDownLatch. This will require changing the constructors and/or code in some of the existing classes.

(15 points) Edit the runtest.sh script according to the comments.

## Turning in the Assignment
For this assignment, you must turn in a zip file of a directory named project3 containing your edited files. Submit the zip file to the appropriate folder on D2L.