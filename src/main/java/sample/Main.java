package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dnu.samoylov.Decision;
import org.dnu.samoylov.Objective;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.DecisionMethod;
import org.dnu.samoylov.method.diophantine.DiophantineEquation;
import org.dnu.samoylov.method.hillclimbing.HillClimbing;
import org.dnu.samoylov.statistic.WorkStatistic;
import org.dnu.samoylov.task.ProblemTask;
import org.dnu.samoylov.task.ProblemTaskLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }



    public static void main(String[] args) {
        final ProblemTask problemTask = new ProblemTaskLoader().load("destination", DiophantineEquation.class);

        final DecisionMethod decisionMethod = HillClimbing.newBuilder()
                .setRadiusFoundNeighbor(1)
                .build();

        final ResultTaskInfo taskInfo = decisionMethod.process(problemTask);

        final Decision result = taskInfo.getResult();
        final WorkStatistic statistic = taskInfo.getWorkStatistic();

        final Objective minObjective = problemTask.calculateObjective(result);

        System.out.println("Result: " + minObjective
                + "\n\tfor arguments:" + result
                + "\n\n work statistic:\n" + statistic);
    }
}
