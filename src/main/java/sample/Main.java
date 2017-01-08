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

    public static void main(String[] args) {
        final ProblemTask problemTask = getProblemTaskDemo();

        final DecisionMethod decisionMethod = getDecisionMethod(DecisionMethodEnum.SimulatedAnnealing);

        final ResultTaskInfo taskInfo = decisionMethod.process(problemTask);

        final Decision result = taskInfo.getResult();
        final WorkStatistic statistic = taskInfo.getWorkStatistic();

        final Objective minObjective = problemTask.calculateObjective(result);

        System.out.println("Result: " + minObjective
                + "\n\tfor arguments:" + result
                + "\n\n work statistic:\n" + statistic);
    }

    private static DecisionMethod getDecisionMethod(DecisionMethodEnum cl) {
        DecisionMethod decisionMethod;

        switch (cl) {
            case HillClimbing:
                decisionMethod = HillClimbing.newBuilder()
                        .setRadiusFoundNeighbor(1000)
                        .setMaxNumberOfIteration(100_000)
                        .build();
                break;
            case HillClimbingBest:
                decisionMethod = HillClimbingBest.newBuilder()
                        .setRadiusFoundNeighbor(10000)
                        .setMaxNumberOfIteration(10_000_000)
                        .build();
                break;
            case GeneticAlgorithm:
                decisionMethod = new GeneticAlgorithm();
                break;
            case SimulatedAnnealing:
                decisionMethod = new SimulatedAnnealing(0, 0.10, 10);
                break;
            case ParticleSwarm:
                decisionMethod = new ParticleSwarm();
                break;
            default:
                throw new IllegalArgumentException("not supported DecisionMethodEnum");
        }

        return decisionMethod;
    }

    private static ProblemTask getProblemTaskDemo() {
        final int[] coefficients = new int[]{1, 1};
        final int[] exponent = new int[]{10, 10};
        final int result = 1356217073;

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

    private enum DecisionMethodEnum {
        HillClimbing, HillClimbingBest, GeneticAlgorithm, SimulatedAnnealing, ParticleSwarm
    }
}
