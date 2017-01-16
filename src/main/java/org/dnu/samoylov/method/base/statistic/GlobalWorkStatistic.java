package org.dnu.samoylov.method.base.statistic;

import org.dnu.samoylov.task.base.Decision;

import java.util.List;

public class GlobalWorkStatistic implements WorkStatistic {
    private TraceBestStatistic specificWorkStatistic;

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

    public void setSpecificWorkStatistic(TraceBestStatistic specificWorkStatistic) {
        this.specificWorkStatistic = specificWorkStatistic;
    }

    public List<Decision> getTraceBest() {
        return specificWorkStatistic.getDecisions();
    }


    @Override
    public String toString() {
        return "Time of work: " + getSecondsOfWork() + " second\n"
                + specificWorkStatistic;
    }
}
