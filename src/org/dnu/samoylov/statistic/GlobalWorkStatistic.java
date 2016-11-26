package org.dnu.samoylov.statistic;

public class GlobalWorkStatistic extends WorkStatistic {
    private WorkStatistic specificWorkStatistic;

    long startTime = 0;
    long endTime = 0;

    public void startEstimationTime() {
        startTime = System.currentTimeMillis();
    }

    public void stopEstimationTime() {
        endTime = System.currentTimeMillis();
    }

    public long getSecondsOfWork() {
        return (endTime - startTime) / 1000;
    }

    public void setSpecificWorkStatistic(WorkStatistic specificWorkStatistic) {
        this.specificWorkStatistic = specificWorkStatistic;
    }

    @Override
    public String toString() {
        return "Time of work: " + getSecondsOfWork() + "second\n"
                + specificWorkStatistic.toString();
    }
}
