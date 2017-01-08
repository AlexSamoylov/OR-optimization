package org.dnu.samoylov.method.annealing;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.math.BigInteger;
import java.util.Random;

public class SimulatedAnnealing extends DecisionMethod {

    private int ALPHA_RADIUS = 1000;
    public static final int NUM_OF_ATTEMPTS = 1000;
    private double initalT;

    private final Random random = new Random();
    private final double coolingAlpha;
    private final int exitConst;

    /**
     * @param initalT if == 0 than calculate default initial temperature
     */
    public SimulatedAnnealing(double initalT, double coolingAlpha, int exitConst) {
        this.initalT = initalT;
        this.coolingAlpha = coolingAlpha;
        this.exitConst = exitConst;
    }


    @Override
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task) {
        SimulatedAnnealingStatistic statistic = new SimulatedAnnealingStatistic();

        DECISION currentBest = task.getRandomDecision();
        double T = initalT;
        int rejQuasInRaw = 0;

        if (T == 0) {
            T = calculateInitialTemperature(task, currentBest, NUM_OF_ATTEMPTS);
        }

        int acceptedCounter = 0;
        int rejectedCounter = 0;

        boolean finished = false;
        do {
            statistic.increaseIterationCount();
            final DECISION candidate = task.getNeighbor(currentBest, ALPHA_RADIUS);

            boolean calExpFdivT = calcMainFunction(task, currentBest, candidate, T);
            if (calExpFdivT) {
                currentBest = candidate;
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
                    if (rejQuasInRaw + 1 == exitConst / 2
                            || rejQuasInRaw + 1 == exitConst * 3 / 4
                            || rejQuasInRaw + 1 == exitConst * 4 / 5) {
                        ALPHA_RADIUS /= 4;
                    }
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


        final DECISION result = currentBest;

        return new ResultTaskInfo(result, statistic);
    }


    private int quasiEquilibriumReached(long acceptedCounter, long rejectedCounter) {
        if (acceptedCounter == ALPHA_RADIUS) {
            return 1;
        }
        if (rejectedCounter == 2 * ALPHA_RADIUS) {
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
                                                                                              DECISION oldDecision, DECISION newDecision,
                                                                                              double t) {
        BigInteger fitnessForStartSolution = problem.calculateFitness(oldDecision);

        long dF;
        final BigInteger dFBig = problem.calculateFitness(newDecision)
                .subtract(fitnessForStartSolution);
        try {
            dF = dFBig.longValueExact();

        } catch (ArithmeticException e) {
            if (dFBig.signum() > 0) {
                return false;
            }  else {
                return true;
            }
        }


        return random.nextDouble() < Math.exp(-dF / t);
    }
}