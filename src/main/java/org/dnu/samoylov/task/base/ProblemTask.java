package org.dnu.samoylov.task.base;

import org.dnu.samoylov.method.genetic.GeneticAlgorithm;

import java.math.BigInteger;
import java.util.List;

public interface ProblemTask<DECISION extends Decision, OBJECTIVE extends Objective> {

    OBJECTIVE calculateObjective(DECISION decision);

    DECISION getNeighbor(DECISION decision, int radius);

    List<DECISION> getAllNeighbor(DECISION decision, int radius);

    boolean isFirstBetter(DECISION first, DECISION second);

    DECISION getRandomDecision();

    BigInteger calculateFitness(DECISION decision);

    GeneticAlgorithm.Pair<DECISION> crossover(DECISION first, DECISION second);
}
