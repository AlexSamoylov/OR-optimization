package org.dnu.samoylov.method.hillclimbing;


import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.util.List;
import java.util.logging.Logger;

public class HillClimbing extends DecisionMethod {


    private static final Logger LOGGER = Logger.getLogger(HillClimbing.class.getName());

    private final int radiusFoundNeighbor;
    private final int maxNumberOfIteration;

    private HillClimbing(int radiusFoundNeighbor, int maxNumberOfIteration) {
        this.radiusFoundNeighbor = radiusFoundNeighbor;
        this.maxNumberOfIteration = maxNumberOfIteration;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    protected<DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        final HillClimbingStatistic statistic = new HillClimbingStatistic();

        DECISION currentNode = task.getRandomDecision();
        LOGGER.info("start node:" + currentNode);

        boolean found;

        do {
            statistic.increaseIterationCount();
            found = true;
            List<DECISION> allNeighbor = task.getAllNeighbor(currentNode, radiusFoundNeighbor);
            for (DECISION neighbor : allNeighbor) {
                if (task.isFirstBetter(neighbor, currentNode)) {
                    currentNode = neighbor;
                    found = false;
                }
            }
        } while (!(found || statistic.iterationCount == maxNumberOfIteration));

        final DECISION result = currentNode;

        return new ResultTaskInfo(result, statistic);
    }









    public static class Builder {
        int radiusFoundNeighbor = 1;
        private int maxNumberOfIteration = -1;

        private Builder() {
        }

        public Builder setRadiusFoundNeighbor(int radiusFoundNeighbor) {
            this.radiusFoundNeighbor = radiusFoundNeighbor;
            return this;
        }

        public Builder setMaxNumberOfIteration(int maxNumberOfIteration) {
            this.maxNumberOfIteration = maxNumberOfIteration;
            return this;
        }

        public HillClimbing build() {
            return new HillClimbing(radiusFoundNeighbor, maxNumberOfIteration);
        }


    }
}
