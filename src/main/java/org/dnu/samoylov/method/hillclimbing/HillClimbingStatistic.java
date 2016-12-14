package org.dnu.samoylov.method.hillclimbing;

import org.dnu.samoylov.statistic.WorkStatistic;

public class HillClimbingStatistic extends WorkStatistic {
    long iterationCount = 0;

    public void increaseIterationCount() {
        iterationCount++;
    }

    @Override
    public String toString() {
        return "iterationCount=" + iterationCount;
    }
}
