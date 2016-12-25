package org.dnu.samoylov.task.base;

import org.dnu.samoylov.method.genetic.GeneticAlgorithm;

import java.math.BigInteger;
import java.util.List;

public interface ProblemTask<DECISION extends Decision, OBJECTIVE extends Objective> {

    public OBJECTIVE calculateObjective(DECISION decision);

    public DECISION getNeighbor(DECISION decision, int radius);

    public List<DECISION> getAllNeighbor(DECISION decision, int radius);

    public boolean isFirstBetter(DECISION first, DECISION second);

    public DECISION getRandomDecision();

    public BigInteger calculateFitness(DECISION decision);

    public GeneticAlgorithm.Pair<DECISION> crossover(DECISION first, DECISION second);
}
