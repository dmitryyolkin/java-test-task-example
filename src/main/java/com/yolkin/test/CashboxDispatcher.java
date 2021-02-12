package com.yolkin.test;


import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class implements a state machine that decides in what cashbox an user should go.
 * Please see details at README.md
 */
public class CashboxDispatcher {
    private static final int CASHBOX_LIMIT = 20;

    private final Map<Cashbox, DispatchInfo> cashBox2Info;

    public CashboxDispatcher(List<Cashbox> cashboxList) {
        this.cashBox2Info = cashboxList
                .stream()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> new DispatchInfo()
                ));
    }

    public int dispatch(long time) {
        Map.Entry<Cashbox, DispatchInfo> bestCandidate = cashBox2Info
                .entrySet()
                .stream()
                .filter(entry -> {
                    DispatchInfo info = entry.getValue();

                    int currQueueLength = info.getQueueLength();
                    long nextAvailableTime = info.getNextAvailableTime();
                    if (nextAvailableTime > time && currQueueLength >= CASHBOX_LIMIT) {
                        // cashbox queue reached max limit
                        return false;
                    }
                    return true;
                })
                .min(Comparator
                        .comparingLong((Map.Entry<Cashbox, DispatchInfo> entry) -> Math.max(time, entry.getValue().getNextAvailableTime()))
                        .thenComparingLong(entry -> entry.getKey().getThroughput()))
                .orElseThrow(() -> new IllegalStateException("There are no cashbox with free limits"));

        // update cashbox infos
        for (Map.Entry<Cashbox, DispatchInfo> entry : cashBox2Info.entrySet()) {
            Cashbox cashbox = entry.getKey();
            DispatchInfo info = entry.getValue();
            if (time >= info.getNextAvailableTime()) {
                // next availability time in the past
                info.setQueueLength(0);
                info.setNextAvailableTime(time);
            }

            if (cashbox.equals(bestCandidate.getKey())) {
                // update info for best cashbox
                info.setQueueLength(info.getQueueLength() + 1);
                info.setNextAvailableTime(info.getNextAvailableTime() + cashbox.getThroughput());
            }
        }
        return bestCandidate.getKey().getNumber();
    }

    private static class DispatchInfo {
        private long nextAvailableTime;
        private int queueLength;

        public long getNextAvailableTime() {
            return nextAvailableTime;
        }

        public void setNextAvailableTime(long nextAvailableTime) {
            this.nextAvailableTime = nextAvailableTime;
        }

        public int getQueueLength() {
            return queueLength;
        }

        public void setQueueLength(int queueLength) {
            this.queueLength = queueLength;
        }
    }
}
