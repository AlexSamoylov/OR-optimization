package org.dnu.samoylov.method.genetic;

import org.dnu.samoylov.statistic.WorkStatistic;

public class GeneticStatistic extends WorkStatistic {
    private final int populationSize;
    private final int countOfIteration;

    public GeneticStatistic(int populationSize, int countOfIteration) {
        this.populationSize = populationSize;
        this.countOfIteration = countOfIteration;
    }

    @Override
    public String toString() {
        return   "populationSize= " + populationSize +
                ", iterationCount= " + countOfIteration;
    }
}
