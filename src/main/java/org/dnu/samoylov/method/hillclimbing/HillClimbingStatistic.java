package org.dnu.samoylov.method.hillclimbing;

import org.dnu.samoylov.method.base.statistic.WorkStatistic;

public class HillClimbingStatistic implements WorkStatistic {

    private long iterationCount = 0;

    public void increaseIterationCount() {
        iterationCount++;
    }

    @Override
    public String toString() {
        return "iterationCount= " + iterationCount;
    }
}
