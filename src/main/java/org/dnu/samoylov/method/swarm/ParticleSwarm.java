package org.dnu.samoylov.method.swarm;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

import java.util.ArrayList;
import java.util.List;

public class ParticleSwarm extends DecisionMethod {

    int swarmSize = 10000;
    float coefFi1 = 2.3f;
    float coefFi2 = 2.8f;
    float coefX = 2f / (coefFi1 + coefFi2);
    private int timeOfLife = 1_000;


    @Override
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> startTask) {

        if (!(startTask instanceof SwarmProblemTask)) {
            throw  new IllegalArgumentException(startTask + "task must be implements SwarmProblemTask");
        }

        SwarmProblemTask<DECISION, OBJECTIVE> task = (SwarmProblemTask<DECISION, OBJECTIVE>) startTask;

        //evaluate
        //compare topological neighborhood
        //imitate monkey see, monkey do

        Swarm<DECISION> swarm = Swarm.create(task, swarmSize);

        for (int i = 0; i < timeOfLife; i++) {
            for (Chronology<DECISION> x : swarm) {
                DECISION vLast = task.multiply(x.lastVelocity, coefX);

                DECISION velocityToPersonalBest = task.multiply(
                        task.subtract(x.personalBest, x.last),
                        coefFi1);

                DECISION velocityToGlobalBest = task.multiply(
                        task.subtract(x.globalBest, x.last),
                        coefFi2);

                DECISION newV = task.sum(
                        task.sum(vLast,
                                velocityToPersonalBest),
                        velocityToGlobalBest
                );

                DECISION newX = task.sum(x.last, newV);
                x.lastVelocity = newV;

                if (task.isFirstBetter(newX, x.personalBest)) {
                    x.personalBest = newX;

                    for (Chronology<DECISION> y : x.topologyNeighborhood) {
                        if (task.isFirstBetter(x.personalBest, y.globalBest)) {
                            y.globalBest = x.personalBest;
                        }
                    }
                }
            }
        }

        DECISION result = swarm.getGlobalBest(task);
        return new ResultTaskInfo(result, null);
    }



    private static final class Swarm<DECISION extends Decision> extends ArrayList<Chronology<DECISION>> {

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

        public DECISION getLast() {
            return last;
        }
        public void updateLast(DECISION newerDecision) {
            last = newerDecision;
        }
    }

}
