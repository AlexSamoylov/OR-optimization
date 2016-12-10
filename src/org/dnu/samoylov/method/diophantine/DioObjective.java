package org.dnu.samoylov.method.diophantine;

import org.dnu.samoylov.Objective;

public class DioObjective extends Objective {
    private final long objective;

    public DioObjective(long objective) {
        this.objective = objective;
    }

    public long getValue() {
        return objective;
    }
}
