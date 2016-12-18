package org.dnu.samoylov.method.genetic;

import org.dnu.samoylov.method.base.statistic.WorkStatistic;

public class GeneticStatistic implements WorkStatistic {

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
