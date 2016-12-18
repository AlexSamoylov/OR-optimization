package org.dnu.samoylov.task.diophantine;

import org.dnu.samoylov.task.base.Objective;

public class DioObjective implements Objective {
    private final long objective;

    public DioObjective(long objective) {
        this.objective = objective;
    }

    public long getValue() {
        return objective;
    }

    @Override
    public String toString() {
        return "Diophantine objective = " + objective;
    }
}
