package org.dnu.samoylov.method.swarm;

import org.dnu.samoylov.method.base.statistic.TraceBestStatistic;
import org.dnu.samoylov.method.base.statistic.WorkStatistic;
import org.dnu.samoylov.task.base.Decision;

public class ParticleSwarmStatistic extends TraceBestStatistic implements WorkStatistic {

    public ParticleSwarmStatistic(int countOfIteration) {
        this.iterationCount = countOfIteration;
    }

    @Override
    public void increaseIterationCount(Decision bestOnCurrentIteration) {
        decisions.add(bestOnCurrentIteration);
    }

}
