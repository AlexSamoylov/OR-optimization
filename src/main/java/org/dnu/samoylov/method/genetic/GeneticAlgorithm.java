package org.dnu.samoylov.method.genetic;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Logger;

public class GeneticAlgorithm extends DecisionMethod {
    public static final double MUTATION_RATE = 0.05;
    private static final int MUTATION_ALPHA_RADIUS = 100;



    private static final Logger log = Logger.getLogger(DecisionMethod.class.getName());

    private final int populationSize;
    private final int countOfIteration;

    ArrayList<Integer> rankMap;
    private final Random random = new Random();
    private Comparator<FitnessDecision> fitnessDecisionComparator;

    public GeneticAlgorithm(int populationSize, int countOfIteration) {
        this.populationSize = populationSize;
        this.countOfIteration = countOfIteration;
        this.rankMap = new ArrayList<>(populationSize);

        fitnessDecisionComparator = Comparator.comparing(x -> x.fitness);
        fitnessDecisionComparator = fitnessDecisionComparator.reversed();
    }


    @Override
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        final GeneticStatistic statistic = new GeneticStatistic(populationSize, countOfIteration);
        int futureNumberIteration = countOfIteration;

        final ArrayList<FitnessDecision<DECISION>> population = generatePopulation(task, populationSize);

        log.info(population.toString());
        int sumOfRank = calcSumOfRank(populationSize);
        do {
            population.sort(fitnessDecisionComparator);

            statistic.increaseIterationCount(bestOfSortedPopulation(population));

            final Pair<DECISION> parent = parentSelection(population, sumOfRank);

            final Pair<DECISION> child = crossover(task, parent);

            final Pair<DECISION> mutationChild = mutation(task, child);

            final FitnessDecision<DECISION> fitnessDecision1 = calculateFitness(task, mutationChild.first);
            final FitnessDecision<DECISION> fitnessDecision2 = calculateFitness(task, mutationChild.second);

            replacement(population, fitnessDecision1, fitnessDecision2, sumOfRank);

        } while ( --futureNumberIteration != 0 );

        population.sort(fitnessDecisionComparator);
        final DECISION result = bestOfSortedPopulation(population);

        log.info(population.toString());
        return new ResultTaskInfo(getClass().getSimpleName(), result, statistic);
    }

    private <DECISION extends Decision> DECISION bestOfSortedPopulation(ArrayList<FitnessDecision<DECISION>> population) {
        return population.get(populationSize - 1).decision;
    }


    private <DECISION extends Decision> ArrayList<FitnessDecision<DECISION>> generatePopulation(final ProblemTask<DECISION, ?> task, int populationSize) {
        final ArrayList<FitnessDecision<DECISION>> pop = new ArrayList<>(populationSize);

        for (int i = 0; i < populationSize; i++) {
            FitnessDecision<DECISION> fitnessDecision = calculateFitness(task, task.getRandomDecision());
            pop.add(fitnessDecision);
        }

        return pop;
    }

    private <DECISION extends Decision> Pair<DECISION> parentSelection(ArrayList<FitnessDecision<DECISION>> population, int sumOfRank) {
        int topIndex1 = getTopIndex(sumOfRank);
        int topIndex2 = getTopIndex(sumOfRank, topIndex1);

        DECISION first = population.get(topIndex1).decision;
        DECISION second = population.get(topIndex2).decision;

        return Pair.create(first, second);
    }


    private <DECISION extends Decision> Pair<DECISION> crossover(final ProblemTask<DECISION, ?> task, Pair<DECISION> pair) {
        DECISION first = pair.first;
        DECISION second = pair.second;

        return task.crossover(first, second);
    }

    private <DECISION extends Decision> Pair<DECISION> mutation(final ProblemTask<DECISION, ?> task, Pair<DECISION> pair) {
        if (random.nextFloat() < MUTATION_RATE) {
            pair.first = task.getNeighbor(pair.first, MUTATION_ALPHA_RADIUS);
        }

        return pair;
    }

    private <DECISION extends Decision> void replacement(ArrayList<FitnessDecision<DECISION>> population, FitnessDecision<DECISION> fitnessDecision1, FitnessDecision<DECISION> fitnessDecision2) {
        population.sort(fitnessDecisionComparator);

        population.set(0, fitnessDecision1);
        population.set(1, fitnessDecision2);
    }

    private <DECISION extends Decision> void replacement(ArrayList<FitnessDecision<DECISION>> population, FitnessDecision<DECISION> fitnessDecision1, FitnessDecision<DECISION> fitnessDecision2, int sumOfRank) {
        population.sort(fitnessDecisionComparator);

        int bottomIndex1 = getBottomIndex(sumOfRank);
        int bottomIndex2 = getBottomIndex(sumOfRank, bottomIndex1);

        population.set(bottomIndex1, fitnessDecision1);
        population.set(bottomIndex2, fitnessDecision2);
    }

    private int getBottomIndex(int sumOfRank, int disallowedIndex) {
        int topIndex = getTopIndex(sumOfRank, disallowedIndex);
        return populationSize - 1 - topIndex;
    }

    private int getBottomIndex(int sumOfRank) {
        return getBottomIndex(sumOfRank, -1);
    }

    private int getTopIndex(int sumOfRank, int disallowedIndex) {
        int topIndex;

        do {
            topIndex = getTopIndex(sumOfRank);
        } while (topIndex == disallowedIndex);

        return topIndex;
    }

    private int getTopIndex(int sumOfRank) {
        return indexNodeByRank(random.nextInt(sumOfRank));
    }



    private <DECISION extends Decision, OBJECTIVE extends Objective> FitnessDecision<DECISION> calculateFitness(ProblemTask<DECISION, OBJECTIVE> task, DECISION decision) {
        return new FitnessDecision<>(decision, task.calculateFitness(decision));
    }

    private int indexNodeByRank(int rank) {
        for (int i = 0; i < rankMap.size(); i++) {
            if (rank < rankMap.get(i)) {
                return i;
            }
        }
        return -1;
    }


    private int calcSumOfRank(int populationSize) {
        int sumOfRank;

        if (populationSize == 1) {
            sumOfRank = populationSize;
        } else {
            sumOfRank = populationSize + calcSumOfRank(populationSize - 1);
        }

        rankMap.add(sumOfRank);

        return sumOfRank;
    }


    public final static class Pair<DECISION extends Decision> {
        DECISION first;
        DECISION second;

        public static <DECISION extends Decision> Pair<DECISION> create(DECISION first, DECISION second) {
            return new Pair<>(first, second);
        }

        public Pair(DECISION first, DECISION second) {
            this.first = first;
            this.second = second;
        }
    }

    public final static class FitnessDecision<DECISION extends Decision> {
        private DECISION decision;
        private BigInteger fitness;

        public FitnessDecision(DECISION decision, BigInteger fitness) {
            this.decision = decision;
            this.fitness = fitness;
        }

        public DECISION getDecision() {
            return decision;
        }

        public void setDecision(DECISION decision) {
            this.decision = decision;
        }

        public BigInteger getFitness() {
            return fitness;
        }

        public void setFitness(BigInteger fitness) {
            this.fitness = fitness;
        }

        @Override
        public String toString() {
            return  decision.toString() + "("+fitness+")";
        }
    }

}
