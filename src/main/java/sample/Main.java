package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.annealing.SimulatedAnnealing;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.method.base.statistic.WorkStatistic;
import org.dnu.samoylov.method.genetic.GeneticAlgorithm;
import org.dnu.samoylov.method.hillclimbing.HillClimbing;
import org.dnu.samoylov.method.hillclimbing.HillClimbingBest;
import org.dnu.samoylov.method.swarm.ParticleSwarm;
import org.dnu.samoylov.task.ProblemTaskLoader;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;
import org.dnu.samoylov.task.diophantine.DiophantineEquation;

public class Main extends Application {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        final ProblemTask problemTask = getCoeffVarTaskDemo();

        final DecisionMethod decisionMethod = getDecisionMethod(DecisionMethodEnum.ParticleSwarm);

        final ResultTaskInfo taskInfo = decisionMethod.process(problemTask);

        print(problemTask, taskInfo);

        ResultTaskInfo taskInfo2 = decisionMethod.process(problemTask, taskInfo.getContinueData());
        System.out.println("\n------------------------\n");
        print(problemTask, taskInfo2);
    }

    private static void print(ProblemTask problemTask, ResultTaskInfo taskInfo) {
        final Decision result = taskInfo.getResult();
        final WorkStatistic statistic = taskInfo.getWorkStatistic();

        final Objective minObjective = problemTask.calculateObjective(result);

        System.out.println("method: " + taskInfo.getNameOfOptimizationMethod()
                + "\nresult: " + minObjective
                + "\n\tfor: " + result
                + "\n work statistic:\n" + statistic + "\n");

        Decision localOptimum = new HillClimbing(result, 5, 1).process(problemTask).getResult();
        System.out.println("check is local optimum radius 5: "
                + result.equals(localOptimum)
                + " (local:" + localOptimum + ")");

        final Decision upgradedDecision = new HillClimbingBest(result, 100, 100_000).process(problemTask).getResult();
        final Objective upgradedMinObjective = problemTask.calculateObjective(upgradedDecision);
        System.out.println("HillClimbingBest upgrade:"
                + "\nresult: " + upgradedMinObjective
                + "\n\tfor arguments:" + upgradedDecision);
    }

    private static DecisionMethod getDecisionMethod(DecisionMethodEnum cl) {
        DecisionMethod decisionMethod;

        switch (cl) {
            case HillClimbing:
                decisionMethod = new HillClimbing(1, 100_000);
                break;
            case HillClimbingBest:
                decisionMethod = new HillClimbingBest(1000, 1_000_000);
                break;
            case GeneticAlgorithm:
                decisionMethod = new GeneticAlgorithm(100, 2_500_000);
                break;
            case SimulatedAnnealing:
                decisionMethod = new SimulatedAnnealing(0, 0.10, 50);
                break;
            case ParticleSwarm:
                decisionMethod = new ParticleSwarm(20, 100_000);
                break;
            default:
                throw new IllegalArgumentException("not supported DecisionMethodEnum");
        }

        return decisionMethod;
    }

    //todo
    private static ProblemTask getMediumCoeffVarTaskDemo() { //3, 40, 6 | 30, 0, -5
        final int[] coefficients = new int[]{2, 6, 1};
        final int[] exponent = new int[]{2, 3, 2};
        final int result = 1825;

        return new DiophantineEquation(coefficients, exponent, result);
    }

    //todo
    private static ProblemTask getCoeffVarTaskDemo() { //5, 15, 25, 35, 4
        final int[] coefficients = new int[]{5, 9, 3, 7, 2};
        final int[] exponent = new int[]{5, 3, 2, 2, 2};
        final int result = 60500;

        return new DiophantineEquation(coefficients, exponent, result);
    }

    private static ProblemTask getManyVarTaskDemo() { //12, 6, 6, 7, 4, 5, 12
        final int[] coefficients = new int[]{1, 1, 1, 1, 1, 1, 1};
        final int[] exponent = new int[]{2, 2, 2, 2, 2, 2, 2};
        final int result = 450;

        return new DiophantineEquation(coefficients, exponent, result);
    }

    private static ProblemTask getPowTenDemo() { //7, 8
        final int[] coefficients = new int[]{1, 1};
        final int[] exponent = new int[]{10, 10};
        final int result = 1356217073;

        return new DiophantineEquation(coefficients, exponent, result);
    }

    private static ProblemTask getEazyTask() { //0, 10
        final int[] coefficients = new int[]{1, 1};
        final int[] exponent = new int[]{1, 1};
        final int result = 10;

        return new DiophantineEquation(coefficients, exponent, result);
    }

    private static ProblemTask getProblemTaskByPath() {
        return new ProblemTaskLoader().load("destination", DiophantineEquation.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public enum DecisionMethodEnum {
        HillClimbing, HillClimbingBest, GeneticAlgorithm, SimulatedAnnealing, ParticleSwarm
    }
}
