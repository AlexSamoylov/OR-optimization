package org.dnu.samoylov.method.hillclimbing;


import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.OneDecisionInitializeMethod;
import org.dnu.samoylov.method.base.resume.ContinueWithOneDecisionInfo;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.util.List;
import java.util.logging.Logger;

public class HillClimbing extends OneDecisionInitializeMethod {


    private static final Logger LOGGER = Logger.getLogger("bla");


    private final int radiusFoundNeighbor;
    private final int maxNumberOfIteration;

    public HillClimbing(Decision startDecision, int radiusFoundNeighbor, int maxNumberOfIteration) {
        LOGGER.setUseParentHandlers(false);

        this.radiusFoundNeighbor = radiusFoundNeighbor;
        this.maxNumberOfIteration = maxNumberOfIteration;
        this.startDecision = startDecision;
    }

    public HillClimbing(int radiusFoundNeighbor, int maxNumberOfIteration) {
        this(null, radiusFoundNeighbor, maxNumberOfIteration);
    }

    @Override
    protected<DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        final HillClimbingStatistic statistic = new HillClimbingStatistic();

        DECISION currentNode = getStartNode(task);
        LOGGER.info("start node:" + currentNode);

        boolean found;

        do {
            statistic.increaseIterationCount(currentNode);
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

        ResultTaskInfo resultTaskInfo = new ResultTaskInfo(getClass().getSimpleName(), result, statistic);
        resultTaskInfo.setContinueData(new ContinueWithOneDecisionInfo(result));
        return resultTaskInfo;
    }



}
