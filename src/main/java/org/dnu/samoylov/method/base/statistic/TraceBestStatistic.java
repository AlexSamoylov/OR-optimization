package org.dnu.samoylov.method.base.statistic;

import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.ProblemTask;

import java.util.LinkedList;
import java.util.List;

public class TraceBestStatistic implements WorkStatistic {

    public long iterationCount = 0;

    protected final List<Decision> decisions = new LinkedList<>();

    public ProblemTask task;

    public void increaseIterationCount(Decision bestOnCurrentIteration) {
        decisions.add(bestOnCurrentIteration);
        iterationCount++;
    }

    public long getIterationCount() {
        return iterationCount;
    }

    @Override
    public String toString() {
        return "iterationCount= " + iterationCount;
    }
}
