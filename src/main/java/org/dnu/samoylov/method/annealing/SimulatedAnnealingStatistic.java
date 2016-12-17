package org.dnu.samoylov.method.annealing;

import org.dnu.samoylov.statistic.WorkStatistic;

import java.util.logging.Logger;

public class SimulatedAnnealingStatistic extends WorkStatistic {

    private static final Logger LOGGER = Logger.getLogger(SimulatedAnnealingStatistic.class.getName());

    long iterationCount = 0;

    public void increaseIterationCount() {
        iterationCount++;
    }

    @Override
    public String toString() {
        return "iterationCount= " + iterationCount;
    }
}
