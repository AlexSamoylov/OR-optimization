package org.dnu.samoylov.task.diophantine;

import org.dnu.samoylov.task.base.Decision;

import java.util.Arrays;

public class DioDecision implements Decision {
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
    public String toString() {
        return Arrays.toString(xValues);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DioDecision that = (DioDecision) o;

        if (!Arrays.equals(xValues, that.xValues)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return xValues != null ? Arrays.hashCode(xValues) : 0;
    }
}
