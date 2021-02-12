package com.yolkin.test.flow;

/**
 * The generator produces times flow distributed in a time range evenly
 * e.g. every 20 seconds / every minute / etc
 */
public class EvenUserFlowGenerator implements UserFlowGenerator{
    private final long[] times;

    public EvenUserFlowGenerator(long timeRange, int intervals) {
        long step = timeRange / intervals;
        this.times = new long[intervals];
        for (int i = 0; i < intervals; i++) {
            times[i] = (step * i);
        }
    }

    @Override
    public long[] generate() {
        return times;
    }
}
