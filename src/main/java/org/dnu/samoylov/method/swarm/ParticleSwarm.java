package org.dnu.samoylov.method.swarm;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Mediator;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class ParticleSwarm extends DecisionMethod {
    public static final Logger LOGGER = Logger.getLogger(ParticleSwarm.class.getCanonicalName());

    private int swarmSize = 20;
    private int timeOfLife = 5_000;


    private final Random random = new Random();

    private final float coefFi1 = 2.0f;
    private final float coefFi2 = 2.1f;

    private final float Fi = coefFi1 + coefFi2;
    private final float coefX = (float) (2d / (2 * Fi - Math.sqrt(Math.pow(Fi, 2) - 4 * Fi)));




    public ParticleSwarm(int swarmSize, int timeOfLife) {
        this.timeOfLife = timeOfLife;
        this.swarmSize = swarmSize;
    }

    @Override
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> startTask) {
        if (!(startTask instanceof SwarmProblemTask)) {
            throw  new IllegalArgumentException(startTask + "task must be implements SwarmProblemTask");
        }

        SwarmProblemTask<DECISION, OBJECTIVE> task = (SwarmProblemTask<DECISION, OBJECTIVE>) startTask;

        Swarm<DECISION> swarm = Swarm.create(task, swarmSize);
        return internalProcess(task, new ContinueSwarmData(swarm));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> startTask, ContinueData continueData) {
        if (!(startTask instanceof SwarmProblemTask)) {
            throw  new IllegalArgumentException(startTask + "task must be implements SwarmProblemTask");
        }

        SwarmProblemTask<DECISION, OBJECTIVE> task = (SwarmProblemTask<DECISION, OBJECTIVE>) startTask;

        Swarm<DECISION> swarm = (Swarm<DECISION>) ((ContinueSwarmData)continueData).getSwarm();

        LOGGER.info(swarm.toString());

        ParticleSwarmStatistic statistic = new ParticleSwarmStatistic(timeOfLife);

        for (int i = 0; i < timeOfLife; i++) {
            statistic.increaseIterationCount(swarm.getGlobalBest(task));

            for (Chronology<DECISION> x : swarm) {

                Mediator<DECISION> lastVelocity = task.createMediator(x.lastVelocity);
                Mediator<DECISION> personalBest = task.createMediator(x.personalBest);
                Mediator<DECISION> globalBest = task.createMediator(x.globalBest);
                Mediator<DECISION> last = task.createMediator(x.last);

                Mediator<DECISION> vLast = task.multiply(lastVelocity, coefX);
                Mediator<DECISION> velocityToPersonalBest = task.multiply(
                        task.subtract(personalBest, last),
                        coefFi1 * random.nextFloat());

                Mediator<DECISION> velocityToGlobalBest = task.multiply(
                        task.subtract(globalBest, last),
                        coefFi2 * random.nextFloat());

                Mediator<DECISION> newV = task.sum(
                        task.sum(vLast,
                                velocityToPersonalBest),
                        velocityToGlobalBest
                );

                DECISION newX = task.sum(last, newV).extract();
                x.last = newX;
                x.lastVelocity = newV.extract();

                if (task.isFirstBetter(x.last, x.personalBest)) {
                    x.personalBest = x.last;

                    for (Chronology<DECISION> y : x.topologyNeighborhood) {
                        if (task.isFirstBetter(x.personalBest, y.globalBest)) {
                            y.globalBest = x.personalBest;
                        }
                    }
                }
            }
        }

        LOGGER.info(swarm.toString());
        DECISION result = swarm.getGlobalBest(task);

        ContinueSwarmData continueSwarmData = new ContinueSwarmData(swarm);
        ResultTaskInfo resultTaskInfo = new ResultTaskInfo(getClass().getSimpleName(), result, statistic);
        resultTaskInfo.setContinueData(continueSwarmData);
        return resultTaskInfo;
    }


    public static final class Swarm<DECISION extends Decision> extends ArrayList<Chronology<DECISION>> {

        public static <DECISION extends Decision> Swarm<DECISION> create(SwarmProblemTask<DECISION, ?> task, int initialCapacity) {
            final Swarm<DECISION> swarm = new Swarm<>(initialCapacity);

            for (int i = 0; i < initialCapacity; i++) {
                Chronology<DECISION> chronology = new Chronology<>(task);
                swarm.add(chronology);
            }

            setNeighborhood(swarm);

            updateGlobalBest(task, swarm);

            return swarm;
        }

        private static <DECISION extends Decision> void updateGlobalBest(SwarmProblemTask<DECISION, ?> task, Swarm<DECISION> swarm) {
            for (Chronology<DECISION> x : swarm) {
                for (Chronology<DECISION> y : x.topologyNeighborhood) {
                    if (task.isFirstBetter(x.personalBest, y.globalBest)) {
                        y.globalBest = x.personalBest;
                    }
                }
            }
        }

        private static <DECISION extends Decision> void setNeighborhood(Swarm<DECISION> swarm) {
            int initialCapacity = swarm.size();

            swarm.get(0).topologyNeighborhood.add(swarm.get(1));
            swarm.get(0).topologyNeighborhood.add(swarm.get(swarm.size() - 1));

            for (int i = 1; i < initialCapacity -1; i++) {
                Chronology<DECISION> target = swarm.get(i);

                Chronology<DECISION> leftNeighborhood = swarm.get((i - 1));
                Chronology<DECISION> rightNeighborhood = swarm.get((i + 1));

                target.topologyNeighborhood.add(leftNeighborhood);
                target.topologyNeighborhood.add(rightNeighborhood);
            }

            swarm.get(swarm.size() - 1).topologyNeighborhood.add(swarm.get(swarm.size() - 2));
            swarm.get(swarm.size() - 1).topologyNeighborhood.add(swarm.get(0));

        }


        private Swarm(int initialCapacity) {
            super(initialCapacity);
        }


        private DECISION getGlobalBest(SwarmProblemTask<DECISION, ?> task) {
            DECISION globalBest = this.get(0).personalBest;

            for (int i = 1; i < this.size(); i++) {
                DECISION candidate = this.get(i).last;
                if (task.isFirstBetter(candidate, globalBest)) {
                    globalBest = candidate;
                }
            }
            return globalBest;
        }

    }


    private static final class Chronology<DECISION extends Decision> {
        DECISION personalBest;
        DECISION globalBest;

        DECISION lastVelocity;

        private List<Chronology<DECISION>> topologyNeighborhood = new ArrayList<>(2);
        private DECISION last;

        public Chronology(SwarmProblemTask<DECISION, ?> task) {
            DECISION randomDecision = task.getRandomDecision();
            this.last = randomDecision;
            this.personalBest = randomDecision;
            this.globalBest = randomDecision;
            this.lastVelocity = task.createZero();
        }

        @Override
        public String toString() {
            return last.toString();
        }
    }

}
