package org.dnu.samoylov.method.hillclimbing;

import org.dnu.samoylov.statistic.WorkStatistic;

import java.util.logging.Logger;

public class HillClimbingStatistic extends WorkStatistic {

    private static final Logger LOGGER = Logger.getLogger(HillClimbingStatistic.class.getName());

    long iterationCount = 0;

    public void increaseIterationCount() {
        iterationCount++;
    }

    @Override
    public String toString() {
        return "iterationCount= " + iterationCount;
    }
}
