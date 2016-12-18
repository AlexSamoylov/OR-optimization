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
import org.dnu.samoylov.task.ProblemTaskLoader;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;
import org.dnu.samoylov.task.diophantine.DiophantineEquation;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }



    public static void main(String[] args) {
        final ProblemTask problemTask = getProblemTaskDemo();

        final HillClimbing decisionMethodHill = HillClimbing.newBuilder()
                .setRadiusFoundNeighbor(1)
                .setMaxNumberOfIteration(100_000)
                .build();

        final GeneticAlgorithm decisionMethodGenetic = new GeneticAlgorithm();


        final SimulatedAnnealing decisionSimulatedAnnealing = new SimulatedAnnealing(0, 0.05, 3000, 10, 25 );
        final DecisionMethod decisionMethod = decisionSimulatedAnnealing;

        final ResultTaskInfo taskInfo = decisionMethod.process(problemTask);

        final Decision result = taskInfo.getResult();
        final WorkStatistic statistic = taskInfo.getWorkStatistic();

        final Objective minObjective = problemTask.calculateObjective(result);

        System.out.println("Result: " + minObjective
                + "\n\tfor arguments:" + result
                + "\n\n work statistic:\n" + statistic);
    }

    private static ProblemTask getProblemTaskDemo() {
        final int[] coefficients = new int[] {1,1};
        final int[] exponent = new int[] {1,1};
        final int result = 10;

        return new DiophantineEquation(coefficients, exponent, result);
    }

    private static ProblemTask getProblemTaskByPath() {
        return new ProblemTaskLoader().load("destination", DiophantineEquation.class);
    }
}
