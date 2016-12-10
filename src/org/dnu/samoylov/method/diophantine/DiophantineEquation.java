package org.dnu.samoylov.method.diophantine;


import org.dnu.samoylov.task.ProblemTask;

import java.util.*;

public class DiophantineEquation extends ProblemTask<DioDecision, DioObjective> {
    private final int[] coefficients;
    private final int[] exponent;

    private final int size;
    private final int result;

    private final Random random = new Random();

    public DiophantineEquation(int[] coefficients, int[] exponent, int result) {
        this.coefficients = coefficients;
        this.exponent = exponent;

        assert coefficients.length == exponent.length;

        this.size = coefficients.length;
        this.result = result;


    }

    @Override
    public DioObjective calculateObjective(DioDecision decision) {
        long objective = 0;

        final int[] x = decision.getxValues();

        for (int i = 0; i < size; i++) {
            objective += Math.pow(x[i], exponent[i]) * coefficients[i];
        }

        objective -= result;

        return new DioObjective(objective);
    }

    @Override
    public DioDecision getNeighbor(DioDecision decision, int radius) {
        final int[] x = Arrays.copyOf(decision.getxValues(), size);

        for (int i = 0; i < size; i++) {
            x[i] = x[i] - radius + random.nextInt(radius * 2);
        }

        return new DioDecision(x);
    }


    @Override
    public List<DioDecision> getAllNeighbor(DioDecision decision, int radius) {
        final NodeVariantsByPosition variantValueByPosition = getNodeVariants(decision, radius);

        final LinkedList<PartOfEquation> partOfEquations = getAllSequenceByNodes(variantValueByPosition);

        return convertToDecision(partOfEquations);
    }

    private NodeVariantsByPosition getNodeVariants(DioDecision decision, int radius) {
        final int[] values = decision.getxValues();

        final NodeVariantsByPosition variantValueByPosition = new NodeVariantsByPosition();

        for (int i = 0; i < size; i++) {
            NodeVariants variant = getAllVariantNode(values[i], radius);
            variantValueByPosition.add(variant);
        }
        return variantValueByPosition;
    }

    private List<DioDecision> convertToDecision(LinkedList<PartOfEquation> partOfEquations) {

        final List<DioDecision> result = new LinkedList<>();
        for (PartOfEquation partOfEquation : partOfEquations) {
            final Integer[] values = partOfEquation.toArray(new Integer[partOfEquation.size()]);
            int[] intArray = Arrays.stream(values).mapToInt(Integer::intValue).toArray();

            DioDecision dioDecision = new DioDecision(intArray);
            result.add(dioDecision);
        }
        return result;
    }


    private LinkedList<PartOfEquation> getAllSequenceByNodes(NodeVariantsByPosition variantValueByPosition) {

        final LinkedList<PartOfEquation> result = new LinkedList<>();

        if (variantValueByPosition.size() > 1) {
            final NodeVariants variantsValueForFirstPosition = variantValueByPosition.pollFirst();

            final LinkedList<PartOfEquation> allSequenceFollowingPos = getAllSequenceByNodes(variantValueByPosition);


            for (Integer variantValueForFirstPosition : variantsValueForFirstPosition) {
                for (PartOfEquation followingPartOfEquation : allSequenceFollowingPos) {
                    PartOfEquation partOfEquation = new PartOfEquation();

                    partOfEquation.add(variantValueForFirstPosition);
                    partOfEquation.addAll(followingPartOfEquation);

                    result.add(partOfEquation);
                }
            }
        } else {
            for (Integer variantValue : variantValueByPosition.get(0)) {
                PartOfEquation partOfEquation = new PartOfEquation();
                partOfEquation.add(variantValue);

                result.add(partOfEquation);
            }
        }

        return result;
    }


    @Override
    public boolean isFirstBetter(DioDecision first, DioDecision second) {
        final DioObjective objective1 = calculateObjective(first);
        final DioObjective objective2 = calculateObjective(second);

        return objective1.getValue() < objective2.getValue();
    }

    @Override
    public DioDecision getRandomDecision() {
        DioDecision dioDecision = new DioDecision(size);
        for (int i = 0; i < size; i++) {
            dioDecision.xValues[i] = random.nextInt();
        }

        return dioDecision;
    }

    @Override
    public long calculateFitness(DioDecision decision) {
        final DioObjective objective = calculateObjective(decision);
        return Math.abs(objective.getValue());
    }


    private NodeVariants getAllVariantNode(int x, int radius) {
        NodeVariants variants = new NodeVariants(radius);

        for (int i = 0; i <= radius * 2; i++) {
            int xI = x - radius + i;
            variants.add(xI);
        }

        return variants;
    }


    private final class NodeVariants extends ArrayList<Integer> {
        public NodeVariants(int initialCapacity) {
            super(initialCapacity);
        }
    }


    private final class NodeVariantsByPosition extends LinkedList<NodeVariants> {
        public NodeVariantsByPosition() {
        }
    }

    private final class PartOfEquation extends LinkedList<Integer> {
        public PartOfEquation() {
        }
    }
}
