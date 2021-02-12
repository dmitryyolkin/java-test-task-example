package com.yolkin.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The factory produces list of cashboxes available in the system.
 * If it's not a test task then we can use some Dependency Injection (e.g. Spring DI) to initialize it
 */
public class CashboxFactory {

    private CashboxFactory() {
    }

    public static List<Cashbox> produce() {
        return produce(new int[]{10, 13, 15, 17});
    }

    public static List<Cashbox> produce(int[] throughputs) {
        long hour = TimeUnit.HOURS.toSeconds(1);
        List<Cashbox> result = new ArrayList<>(throughputs.length);
        for (int i = 0; i < throughputs.length; i++) {
            result.add(new Cashbox(i + 1, hour / throughputs[i]));
        }
        return result;
    }
}
