package com.yolkin.test.flow;

/**
 * The generator returns certain times provided to the generator
 */
public class FixedUserFlowGenerator implements UserFlowGenerator{
    private final long[] times;

    public FixedUserFlowGenerator(long[] times) {
        this.times = times;
    }

    @Override
    public long[] generate() {
        return times;
    }
}
