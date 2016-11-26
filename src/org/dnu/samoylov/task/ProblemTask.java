package org.dnu.samoylov.task;

import org.dnu.samoylov.Decision;
import org.dnu.samoylov.Objective;

import java.util.Collections;
import java.util.List;

public class ProblemTask {

    public Objective calculateObjective(Decision result) {
        return new Objective();
    }

    public Decision getNeighbor(Decision decision, int radious) {
        return new Decision();
    }

    public List<Decision> getAllNeighbor(Decision decision, int radious) {
        return Collections.EMPTY_LIST;
    }

    public boolean isFirstBetter(Decision first, Decision second) {
        return compare(first, second) > 0;
    }

    public int compare(Decision first, Decision second) {
        return 0;
    }

    public Decision getRandomDecision() {
        return new Decision();
    }

    public long calculateFitness(Decision decision) {
        return 0;
    }
}
