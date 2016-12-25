package org.dnu.samoylov.method.annealing;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.math.BigInteger;
import java.util.Random;

public class SimulatedAnnealing extends DecisionMethod {

    private static final int ALPHA_RADIUS = 1000;
    private double initalT;

    private final Random random = new Random();
    private final double coolingAlpha;
    private final long numberOfAttempts;
    private final int numberOfAttemptsMultiplier;
    private final int exitConst;

    public SimulatedAnnealing(double initalT, double coolingAlpha, long numberOfAttempts, int numberOfAttemptsMultiplier, int exitConst) {
        this.initalT = initalT;
        this.coolingAlpha = coolingAlpha;
        this.numberOfAttempts = numberOfAttempts;
        this.numberOfAttemptsMultiplier = numberOfAttemptsMultiplier;
        this.exitConst = exitConst;
    }


    @Override
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        SimulatedAnnealingStatistic statistic = new SimulatedAnnealingStatistic();

        DECISION x = task.getRandomDecision();
        double T = initalT;
        int rejQuasInRaw = 0;

        if (T == 0) {
            T = calculateInitialTemperature(task, x, 1000);
        }

        int acceptedCounter = 0;
        int rejectedCounter = 0;

        boolean finished = false;
        do {
            statistic.increaseIterationCount();
            final DECISION y = task.getNeighbor(x, ALPHA_RADIUS);

            boolean calExpFdivT = calcMainFunction(task, y, x, T);
            if (calExpFdivT) {
                x = y;
                acceptedCounter++;
            } else {
                rejectedCounter++;
            }

            final int quasiEquilibriumReached = quasiEquilibriumReached(acceptedCounter, rejectedCounter);
            switch (quasiEquilibriumReached) {
                case 1: {
                    acceptedCounter = 0;
                    rejectedCounter = 0;
                    rejQuasInRaw = 0;
                    T = applyCoolingSchedule(T);
                    break;
                }

                case -1: {
                    if (rejQuasInRaw + 1 == exitConst) {
                        finished = true;
                    } else {
                        acceptedCounter = 0;
                        rejectedCounter = 0;
                        rejQuasInRaw++;
                        T = applyCoolingSchedule(T);
                        break;
                    }
                }
            }

        } while (!finished);


        final DECISION result = x;

        return new ResultTaskInfo(result, statistic);
    }


    private int quasiEquilibriumReached(long acceptedCounter, long rejectedCounter) {
        if (acceptedCounter == numberOfAttempts) {
            return 1;
        }
        if (rejectedCounter == numberOfAttemptsMultiplier * numberOfAttempts) {
            return -1;
        }
        return 0;
    }


    private double applyCoolingSchedule(double t) {
        return t * (1 - coolingAlpha);
    }


    private <DECISION extends Decision, OBJECTIVE extends Objective> double calculateInitialTemperature(ProblemTask<DECISION, OBJECTIVE> task, DECISION initialSolution, int numOfAttempts) {
        double temperatureChangingSpeed = 0.025;

        int initialTemp = 1;
        double initialTempAcceptingRate = calculateAcceptingRate(initialTemp, task, initialSolution, numOfAttempts);

        double currentTemp = initialTemp;
        double bestTemp = initialTemp;
        double bestTempAcceptingRate = initialTempAcceptingRate;

        for (int i = 0; i < numOfAttempts; i++) {
            double currentTempAcceptingRate = calculateAcceptingRate(currentTemp, task, initialSolution, numOfAttempts);

            if (currentTempAcceptingRate > 0.945 && currentTempAcceptingRate < 0.955) {
                return currentTemp;
            }

            double nextTemp;
            if (currentTempAcceptingRate < 0.95) {
                nextTemp = currentTemp / (1 - temperatureChangingSpeed);
            } else {
                nextTemp = currentTemp * (1 - temperatureChangingSpeed);
            }

            if (Math.abs(currentTempAcceptingRate - 0.95) < Math.abs(bestTempAcceptingRate - 0.95)) {
                bestTempAcceptingRate = currentTempAcceptingRate;
                bestTemp = currentTemp;
            }

            currentTemp = nextTemp;
        }

        return bestTemp;
    }

    private <DECISION extends Decision, OBJECTIVE extends Objective> Double calculateAcceptingRate(double t, ProblemTask<DECISION, OBJECTIVE> problem, DECISION initialSolution, int numOfAttempts) {
        int acceptedCounter = 0;
        int rejectedCounter = 0;


        for (int i = 0; i < numOfAttempts; i++) {
            DECISION randomSolution = problem.getNeighbor(initialSolution, 3);
            boolean accepted = calcMainFunction(problem, initialSolution, randomSolution, t);


            if (accepted) {
                acceptedCounter++;
            } else {
                rejectedCounter++;
            }
        }

        return (double) acceptedCounter / (acceptedCounter + rejectedCounter);
    }

    private <DECISION extends Decision, OBJECTIVE extends Objective> boolean calcMainFunction(ProblemTask<DECISION, OBJECTIVE> problem,
                                                                                              DECISION first, DECISION second,
                                                                                              double t) {
        BigInteger fitnessForStartSolution = problem.calculateFitness(first);

        long dF =  problem.calculateFitness(second)
                .subtract(fitnessForStartSolution)
                .longValueExact();


        return random.nextDouble() < Math.exp(-dF / t);
    }
}