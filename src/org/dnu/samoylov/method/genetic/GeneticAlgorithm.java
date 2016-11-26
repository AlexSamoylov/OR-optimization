package org.dnu.samoylov.method.genetic;

import org.dnu.samoylov.Decision;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.DecisionMethod;
import org.dnu.samoylov.task.ProblemTask;

import java.util.List;

public class GeneticAlgorithm extends DecisionMethod {
    int populationSize = 100;

    @Override
    protected ResultTaskInfo internalProcess(ProblemTask task) {
        final GeneticStatistic statistic = new GeneticStatistic();

        final List<FitnessDecision> population = generatePopulation(populationSize);

        do {
            final Pair parent =  parentSelection(population);

            final Pair child = crossover(parent);

            final Pair mutationChild = mutation(parent);

            FitnessDecision fitnessDecision1 = calculateFitness(task, mutationChild.first);
            FitnessDecision fitnessDecision2 = calculateFitness(task, mutationChild.second);
            replacement(population, fitnessDecision1, fitnessDecision2);
        } while (true);

        final Decision result = new Decision();

        return new ResultTaskInfo(result, statistic);
    }


    private FitnessDecision calculateFitness(ProblemTask task, Decision decision) {
        return new FitnessDecision(decision, task.calculateFitness(decision));
    }

    private static class Pair {
        Decision first;
        Decision second;

        public Pair(Decision first, Decision second) {
            this.first = first;
            this.second = second;
        }

        public Decision getFirst() {
            return first;
        }

        public void setFirst(Decision first) {
            this.first = first;
        }

        public Decision getSecond() {
            return second;
        }

        public void setSecond(Decision second) {
            this.second = second;
        }
    }
    private static class FitnessDecision {
        private Decision decision;
        private long fitness;

        public FitnessDecision(Decision decision, long fitness) {
            this.decision = decision;
            this.fitness = fitness;
        }

        public Decision getDecision() {
            return decision;
        }

        public void setDecision(Decision decision) {
            this.decision = decision;
        }

        public long getFitness() {
            return fitness;
        }

        public void setFitness(long fitness) {
            this.fitness = fitness;
        }
    }
}
