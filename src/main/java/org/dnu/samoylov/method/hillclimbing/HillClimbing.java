package org.dnu.samoylov.method.hillclimbing;


import org.dnu.samoylov.Decision;
import org.dnu.samoylov.Objective;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.DecisionMethod;
import org.dnu.samoylov.task.ProblemTask;

import java.util.List;

public class HillClimbing extends DecisionMethod {
    int radiusFoundNeighbor;

    private HillClimbing(int radiusFoundNeighbor) {
        this.radiusFoundNeighbor = radiusFoundNeighbor;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    protected<DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        final HillClimbingStatistic statistic = new HillClimbingStatistic();

        DECISION currentNode = task.getRandomDecision();

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
        } while (found);

        final DECISION result = currentNode;

        return new ResultTaskInfo(result, statistic);
    }









    public static class Builder {
        int radiusFoundNeighbor = 1;

        private Builder() {
        }

        public Builder setRadiusFoundNeighbor(int radiusFoundNeighbor) {
            this.radiusFoundNeighbor = radiusFoundNeighbor;
            return this;
        }

        public HillClimbing build() {
            return new HillClimbing(radiusFoundNeighbor);
        }
    }
}
