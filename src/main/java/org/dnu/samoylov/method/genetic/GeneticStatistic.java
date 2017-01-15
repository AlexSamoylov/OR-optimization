package org.dnu.samoylov.method.genetic;

import org.dnu.samoylov.method.base.statistic.TraceBestStatistic;
import org.dnu.samoylov.method.base.statistic.WorkStatistic;
import org.dnu.samoylov.task.base.Decision;

public class GeneticStatistic extends TraceBestStatistic implements WorkStatistic {

    private final int populationSize;

    public GeneticStatistic(int populationSize, int countOfIteration) {
        this.populationSize = populationSize;
        this.iterationCount = countOfIteration;
    }

    @Override
    public void increaseIterationCount(Decision bestOnCurrentIteration) {
        decisions.add(bestOnCurrentIteration);
    }

    @Override
    public String toString() {
        return "populationSize= " + populationSize +
                ", " + super.toString();
    }
}
