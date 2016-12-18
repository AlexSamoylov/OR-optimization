package org.dnu.samoylov.method.annealing;

import org.dnu.samoylov.method.base.statistic.WorkStatistic;

public class SimulatedAnnealingStatistic implements WorkStatistic {
    long iterationCount = 0;

    public void increaseIterationCount() {
        iterationCount++;
    }

    @Override
    public String toString() {
        return "iterationCount= " + iterationCount;
    }
}
