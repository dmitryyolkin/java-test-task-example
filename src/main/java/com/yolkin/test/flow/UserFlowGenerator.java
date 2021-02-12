package com.yolkin.test.flow;

/**
 * It generates how user flow is distributed within time.
 * E.g. every client can come every second, minute, etc
 * or clients can come according to some distribution law of a random value (e.g. normal)
 */
public interface UserFlowGenerator {

    /**
     * @return array of time moments when a new client comes to a shop. Time moment is measured in seconds
     */
    long[] generate();
}
