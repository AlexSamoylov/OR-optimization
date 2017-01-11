package org.dnu.samoylov.task.diophantine;


import org.dnu.samoylov.method.genetic.GeneticAlgorithm;
import org.dnu.samoylov.method.swarm.SwarmProblemTask;
import org.dnu.samoylov.task.base.Mediator;

import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;

public class DiophantineEquation implements SwarmProblemTask<DioDecision, DioObjective> {
    public static final Logger LOGGER = Logger.getLogger(DiophantineEquation.class.getSimpleName());

    public static int BOUND_OF_SOLUTION = -1; //Short.MAX_VALUE;
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
        BigInteger objective = BigInteger.valueOf(0);

        final int[] x = decision.getxValues();

        for (int i = 0; i < size; i++) {
            BigInteger val = BigInteger.valueOf(x[i])
                    .pow(exponent[i])
                    .multiply(BigInteger.valueOf(coefficients[i]));
            objective = objective.add(val);
        }
        objective = objective.subtract(BigInteger.valueOf(result));
        BigInteger absoluteDistanceToZero = objective.abs();

        return new DioObjective(absoluteDistanceToZero);
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

        return objective1.getValue().compareTo(objective2.getValue()) < 0;
    }

    @Override
    public DioDecision getRandomDecision() {
        DioDecision dioDecision = new DioDecision(size);
        for (int i = 0; i < size; i++) {
            if (BOUND_OF_SOLUTION == -1) {
                dioDecision.xValues[i] = random.nextInt();
            } else {
                dioDecision.xValues[i] = random.nextInt(BOUND_OF_SOLUTION) - BOUND_OF_SOLUTION / 2;
            }
        }

        return dioDecision;
    }

    @Override
    public BigInteger calculateFitness(DioDecision decision) {
        final DioObjective objective = calculateObjective(decision);
        return objective.getValue();
    }

    @Override
    public GeneticAlgorithm.Pair<DioDecision> crossover(DioDecision firstD, DioDecision secondD) {
        DioDecision crossDec = getOneCrossoverDecision(firstD, secondD);
        DioDecision oneCrossDec2 = getOneCrossoverDecision(firstD, secondD);

        return GeneticAlgorithm.Pair.create(crossDec, oneCrossDec2);
    }

    private DioDecision getOneCrossoverDecision(DioDecision firstD, DioDecision secondD) {
        final double alpha = random.nextDouble() * 2 - 0.5D;

        DioDecision dioDecision = new DioDecision(size);

        int[] first = firstD.getxValues();
        int[] second = secondD.getxValues();

        for (int i = 0; i < size; i++) {

            Long tmpExtLong = (long) (alpha * (first[i] - second[i]) + first[i]);


            if (tmpExtLong > Integer.MAX_VALUE) {
                dioDecision.xValues[i] = Integer.MAX_VALUE;
            } else
            if (tmpExtLong < Integer.MIN_VALUE) {
                dioDecision.xValues[i] = Integer.MIN_VALUE;
            } else {
                dioDecision.xValues[i] = tmpExtLong.intValue();
            }

        }

        return dioDecision;
    }


    private NodeVariants getAllVariantNode(int x, int radius) {
        NodeVariants variants = new NodeVariants(radius);

        for (int i = 0; i <= radius * 2; i++) {
            int xI = x - radius + i;
            variants.add(xI);
        }

        return variants;
    }

    @Override
    public DioMediator sum(Mediator<DioDecision> firstM, Mediator<DioDecision> secondM) {
        final DioMediator first = castMediator(firstM);
        final DioMediator second = castMediator(secondM);

        long[] result = Arrays.copyOf(first.getxValues(), size);
        long[] secondValues = second.getxValues();

        for (int i = 0; i < size; i++) {
            result[i] += secondValues[i];
        }

        return new DioMediator(result);
    }

    @Override
    public DioMediator subtract(Mediator<DioDecision> firstM, Mediator<DioDecision> secondM) {
        final DioMediator first = castMediator(firstM);
        final DioMediator second = castMediator(secondM);


        long[] result = Arrays.copyOf(first.getxValues(), size);
        long[] secondValues = second.getxValues();

        for (int i = 0; i < size; i++) {
            result[i] -= secondValues[i];
        }

        return new DioMediator(result);
    }

    @Override
    public DioMediator multiply(Mediator<DioDecision> firstM, float m) {
        final DioMediator first = castMediator(firstM);

        long[] result = Arrays.copyOf(first.getxValues(), size);

        for (int i = 0; i < size; i++) {
            result[i] *= m;
        }

        return new DioMediator(result);
    }

    @Override
    public DioMediator createMediator(DioDecision first) {
        return new DioMediator(first);
    }

    private DioMediator castMediator(Mediator<DioDecision> mediator) {
        if (mediator.getClass() != DioMediator.class) {
            throw new IllegalArgumentException("for " + DiophantineEquation.class + " mediator must be " + DioMediator.class);
        }
        return (DioMediator) mediator;
    }
    @Override
    public DioDecision createZero() {
        return new DioDecision(new int[size]);
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


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("DiophantineEquation {");
        for (int i = 0; i < size; i++) {
            result
                    .append(coefficients[i])
                    .append("*x")
                    .append(i)
                    .append("^")
                    .append(exponent[i]);

            if (i != size - 1) {
                result.append(" + ");
            }
        }

        result
                .append(" - ")
                .append(this.result)
                .append(" -> min")
                .append("}");

        return result.toString();
    }
}
