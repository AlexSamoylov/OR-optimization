package org.dnu.samoylov.task.diophantine;

import org.dnu.samoylov.task.base.Mediator;

import java.util.Arrays;
import java.util.logging.Logger;

public class DioMediator implements Mediator<DioDecision> {

    private static final Logger LOGGER = Logger.getLogger(DioMediator.class.getSimpleName());

    long[] xValues;

    public DioMediator(int size) {
        this.xValues = new long[size];
    }

    public DioMediator(long[] xValues) {
        this.xValues = xValues;
    }

    public DioMediator(DioDecision decision) {
        final int[] ints = decision.getxValues();
        this.xValues = new long[ints.length];

        for (int i = 0; i <ints.length; i++) {
            xValues[i] = ints[i];
        }
    }

    public long[] getxValues() {
        return xValues;
    }

    public DioDecision extract() {
        final int[] ints = new int[xValues.length];

        for (int i = 0; i <ints.length; i++) {
            if (xValues[i] > Integer.MAX_VALUE) {
                ints[i] = Integer.MAX_VALUE;
                LOGGER.info("extract max overflow");

            } else if (xValues[i] < Integer.MIN_VALUE) {
                ints[i] = Integer.MIN_VALUE;
                LOGGER.info("extract min overflow");
            } else {
                ints[i] = (int) xValues[i];
            }
        }

        return new DioDecision(ints);
    }

    @Override
    public String toString() {
        return Arrays.toString(xValues);
    }
}
