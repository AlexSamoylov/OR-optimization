package org.dnu.samoylov.method.diophantine;

import org.dnu.samoylov.Decision;

import java.util.Arrays;

public class DioDecision extends Decision {
    int[] xValues;

    public DioDecision(int size) {
        this.xValues = new int[size];
    }

    public DioDecision(int[] xValues) {
        this.xValues = xValues;
    }

    public int[] getxValues() {
        return xValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DioDecision that = (DioDecision) o;

        return Arrays.equals(xValues, that.xValues);

    }

    @Override
    public int hashCode() {
        return xValues != null ? Arrays.hashCode(xValues) : 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(xValues);

    }
}
