package org.dnu.samoylov.method.hillclimbing;


import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.OneDecisionInitializeMethod;
import org.dnu.samoylov.method.base.resume.ContinueWithOneDecisionInfo;
import org.dnu.samoylov.method.genetic.GeneticAlgorithm;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.util.logging.Logger;

public class HillClimbingBest extends OneDecisionInitializeMethod {


    private static final Logger LOGGER = Logger.getLogger(HillClimbingBest.class.getCanonicalName());

    private int radiusFoundNeighbor;

    private final int maxNumberOfIteration;

    public HillClimbingBest(Decision startDecision, int radiusFoundNeighbor, int maxNumberOfIteration) {
        this.radiusFoundNeighbor = radiusFoundNeighbor;
        this.maxNumberOfIteration = maxNumberOfIteration;
        this.startDecision = startDecision;
    }

    public HillClimbingBest(int radiusFoundNeighbor, int maxNumberOfIteration) {
        this(null, radiusFoundNeighbor, maxNumberOfIteration);
    }

    @Override
    protected<DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        final HillClimbingStatistic statistic = new HillClimbingStatistic();

        DECISION currentNodeTmp = getStartNode(task);
        GeneticAlgorithm.FitnessDecision<DECISION> currentNode = GeneticAlgorithm.calculateFitness(task, currentNodeTmp);

        LOGGER.info("start node:" + currentNode);

        do {
            statistic.increaseIterationCount(currentNode.getDecision());

            if (statistic.iterationCount == maxNumberOfIteration / 2
                    || statistic.iterationCount == maxNumberOfIteration * 3 / 4
                    || statistic.iterationCount == maxNumberOfIteration * 4 / 5) {
                radiusFoundNeighbor = radiusFoundNeighbor / 4 + 1;
            }
            
            DECISION neighbor = task.getNeighbor(currentNode.getDecision(), radiusFoundNeighbor);
            GeneticAlgorithm.FitnessDecision<DECISION> neighborDecision = GeneticAlgorithm.calculateFitness(task, neighbor);
            if (GeneticAlgorithm.FitnessDecision.isFirstBetter(neighborDecision, currentNode)) {
                currentNode = neighborDecision;
            }

        } while (statistic.iterationCount != maxNumberOfIteration);

        final DECISION result = currentNode.getDecision();
        ResultTaskInfo resultTaskInfo = new ResultTaskInfo(getClass().getSimpleName(), result, statistic);
        resultTaskInfo.setContinueData(new ContinueWithOneDecisionInfo(result));
        return resultTaskInfo;
    }
}
