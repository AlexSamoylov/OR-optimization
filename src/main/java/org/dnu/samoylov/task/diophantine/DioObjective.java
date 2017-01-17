package org.dnu.samoylov.task.diophantine;

import org.dnu.samoylov.task.base.Objective;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.logging.Logger;

public class DioObjective implements Objective {
    public static final Logger LOGGER = Logger.getLogger(DioObjective.class.getCanonicalName());

    private final BigInteger objective;
    private Comparator<DioObjective> comparing = Comparator.comparing(DioObjective::getValue);


    public DioObjective(BigInteger objective) {
        this.objective = objective;
    }

    public BigInteger getValue() {
        return objective;
    }

    public Comparator<DioObjective> getComparator() {
        return comparing;
    }

    @Override
    public String toString() {
        return "objective = " + objective;
    }
}
