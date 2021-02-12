package com.yolkin.test;

import java.util.Objects;

/**
 * Class is responsible for a certain Cashbox with number and throughput that measured in seconds per client
 */
public class Cashbox {
    private final int number;
    private final long throughput;

    public Cashbox(int number, long throughput) {
        this.number = number;
        this.throughput = throughput;
    }

    public int getNumber() {
        return number;
    }

    public long getThroughput() {
        return throughput;
    }

    @Override
    public String toString() {
        return "Cashbox{" +
                "number=" + number +
                ", throughput=" + throughput +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashbox cashbox = (Cashbox) o;
        return number == cashbox.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

}
