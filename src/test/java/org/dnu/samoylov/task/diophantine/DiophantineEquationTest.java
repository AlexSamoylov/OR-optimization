package org.dnu.samoylov.task.diophantine;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DiophantineEquationTest {
    final DiophantineEquation equation;

    public DiophantineEquationTest() {
        final int[] coefficients = new int[] {1,1};
        final int[] exponent = new int[] {1,1};
        final int result = 10;

        this.equation = new DiophantineEquation(coefficients, exponent, result);
    }

    @Test
    public void testCalculateObjective() throws Exception {
        final DioDecision decision = new DioDecision(new int[] {4, 4});

        final DioObjective objective = equation.calculateObjective(decision);

        Assert.assertEquals(objective.getValue(), -2);
    }

    @Test
    public void testGetNeighbor() throws Exception {
        final DioDecision decision = new DioDecision(new int[] {4, 4});
        final int radius = 5;

        for (int i = 0; i < 5; i++) {
            final DioDecision neighbor = equation.getNeighbor(decision, radius);
            for (int i1 = 0; i1 < neighbor.xValues.length; i1++) {
                final boolean isGood = (neighbor.xValues[i1] >= decision.xValues[i1] - radius) && (neighbor.xValues[i1] <= decision.xValues[i1] + radius);
                Assert.assertTrue(isGood);
            }
        }
    }

    @Test
    public void testGetAllNeighbor() throws Exception {
        final DioDecision decision = new DioDecision(new int[] {4, 4});
        Set<DioDecision> setDecision = new HashSet<>();
        setDecision.addAll(Arrays.asList(
                new DioDecision(new int[]{3, 3}),
                new DioDecision(new int[]{3, 4}),
                new DioDecision(new int[]{3, 5}),
                new DioDecision(new int[]{4, 3}),
                new DioDecision(new int[]{4, 4}),
                new DioDecision(new int[]{4, 5}),
                new DioDecision(new int[]{5, 3}),
                new DioDecision(new int[]{5, 4}),
                new DioDecision(new int[]{5, 5})
        ));

        final List<DioDecision> allNeighbor = equation.getAllNeighbor(decision, 1);

        setDecision.retainAll(allNeighbor);
        Assert.assertTrue(setDecision.size() == allNeighbor.size());

    }

    @Test
    public void testIsFirstBetter() throws Exception {

    }

    @Test
    public void testGetRandomDecision() throws Exception {

    }

    @Test
    public void testCalculateFitness() throws Exception {

    }
}