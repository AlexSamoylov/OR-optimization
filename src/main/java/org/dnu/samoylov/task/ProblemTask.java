package org.dnu.samoylov.task;

import org.dnu.samoylov.Decision;
import org.dnu.samoylov.Objective;
import org.dnu.samoylov.method.genetic.GeneticAlgorithm;

import java.util.List;

public abstract class ProblemTask<DECISION extends Decision, OBJECTIVE extends Objective> {

    public abstract OBJECTIVE calculateObjective(DECISION decision);

    public abstract DECISION getNeighbor(DECISION decision, int radius);

    public abstract List<DECISION> getAllNeighbor(DECISION decision, int radius);

    public abstract boolean isFirstBetter(DECISION first, DECISION second);


    public abstract DECISION getRandomDecision();

    public abstract long calculateFitness(DECISION decision);

    public abstract GeneticAlgorithm.Pair<DECISION> crossover(DECISION first, DECISION second);
}
