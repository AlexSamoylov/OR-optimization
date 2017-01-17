package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.annealing.SimulatedAnnealing;
import org.dnu.samoylov.method.base.DecisionMethod;
import org.dnu.samoylov.method.base.statistic.GlobalWorkStatistic;
import org.dnu.samoylov.method.base.statistic.WorkStatistic;
import org.dnu.samoylov.method.genetic.GeneticAlgorithm;
import org.dnu.samoylov.method.hillclimbing.HillClimbing;
import org.dnu.samoylov.method.hillclimbing.HillClimbingBest;
import org.dnu.samoylov.method.swarm.ParticleSwarm;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;
import org.dnu.samoylov.task.diophantine.DiophantineEquation;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    public TextField coefficientInput;
    public TextField exponentInput;
    public TextField resultInput;

    public ComboBox<DiophantineEquation> resultEquation;
    public ChoiceBox<MainConsole.DecisionMethodEnum> choiceMethod;

    public VBox parameterContainer;

    public LineChart<Number, Number> chartEvolutionBest;
    public ListView<String> console;

    public Button runHillClimbingOnResultButton;
    public Button continueMainMethodOnResultButton;
    public ProgressIndicator progressIndicator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultEquation.getItems().addAll(MainConsole.getAllTemplateEquation());
        resultEquation.valueProperty().addListener((observable, oldValue, newValue) -> {
            printEquation(newValue);
        });
        resultEquation.getSelectionModel().select(0);

        choiceMethod.getItems().addAll(MainConsole.DecisionMethodEnum.values());
        choiceMethod.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeMethodField(newValue);
            runHillClimbingOnResultButton.setDisable(true);
            continueMainMethodOnResultButton.setDisable(true);
        });
        choiceMethod.getSelectionModel().select(MainConsole.DecisionMethodEnum.HillClimbingBest);


        chartEvolutionBest.setCreateSymbols(false);
        chartEvolutionBest.setLegendVisible(false);
//        setUpperBoundChart();
    }

    private void setUpperBoundChart() {
        ((NumberAxis)chartEvolutionBest.getYAxis()).setAutoRanging(false);
        ((NumberAxis)chartEvolutionBest.getYAxis()).setUpperBound(10_000);
    }

    private void changeMethodField(MainConsole.DecisionMethodEnum newValue) {
        ObservableList<Node> children = parameterContainer.getChildren();
        children.clear();

        switch (newValue) {
            case HillClimbing:
                addField(children, "epsilon", 1);
                addField(children, "count of iteration", 100_000);
                break;
            case HillClimbingBest:
                addField(children, "epsilon", 1000);
                addField(children, "count of iteration", 1_000_000);
                break;
            case GeneticAlgorithm:
                addField(children, "population size", 100);
                addField(children, "count of iteration", 2_500_000);
                break;
            case SimulatedAnnealing:
                addField(children, "start temperature", 0);
                addField(children, "cooling alpha", 0.10);
                addField(children, "number of rejected", 25);
                break;
            case ParticleSwarm:
                addField(children, "swarm size", 20);
                addField(children, "count of iteration", 100_000);
                break;
        }
    }

    private void addField(ObservableList<Node> children, String name, Object initValue) {
        children.addAll(
                new Label(name + ":"),
                new TextField(initValue.toString())
        );
    }


    private void printEquation(DiophantineEquation selectedItem) {
        coefficientInput.setText(makeString(selectedItem.getCoefficients()));
        exponentInput.setText(makeString(selectedItem.getExponent()));
        resultInput.setText(String.valueOf(selectedItem.getResult()));
    }

    private String makeString(int[] arr) {
        return Arrays.stream(arr)
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public void buildEquation(ActionEvent actionEvent) {
        int[] coefficient = Arrays.stream(coefficientInput.getText().split(",")).mapToInt(Integer::valueOf).toArray();
        int[] exponent = Arrays.stream(exponentInput.getText().split(",")).mapToInt(Integer::valueOf).toArray();
        int result = Integer.valueOf(resultInput.getText());

        DiophantineEquation diophantineEquation = new DiophantineEquation(coefficient, exponent, result);
        resultEquation.getItems().add(diophantineEquation);
        resultEquation.getSelectionModel().select(diophantineEquation);
    }

    private DecisionMethod currentDecisionMethod;
    private ProblemTask currentProblemTask;
    private ResultTaskInfo currentTaskInfo;

    public void runMainMethod(ActionEvent actionEvent) {
        console.getItems().clear();
        chartEvolutionBest.getData().clear();
        progressIndicator.setVisible(true);

        MainConsole.DecisionMethodEnum selectedItem = choiceMethod.getSelectionModel().getSelectedItem();

        Task <Void> t = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                if (currentTaskInfo != null) {
                    currentTaskInfo.setContinueData(null);
                    currentTaskInfo.setWorkStatistic(null);
                    currentTaskInfo = null;
                }

                currentDecisionMethod = buildDecisionMethod(selectedItem);
                currentProblemTask = resultEquation.getSelectionModel().getSelectedItem();

                currentTaskInfo = currentDecisionMethod.process(currentProblemTask);

                printAndShowRes();

                return null;
            }
        };

        new Thread(t).start();
    }

    private void printAndShowRes() {
        Platform.runLater(() -> print(currentProblemTask, currentTaskInfo));


        ArrayList<Object[]> numberNumberSeries = getNumberNumberSeries(currentTaskInfo);

        Platform.runLater(() -> {
            showChart(numberNumberSeries);
            runHillClimbingOnResultButton.setDisable(false);
            continueMainMethodOnResultButton.setDisable(false);

            progressIndicator.setVisible(false);
        });
    }

    private DecisionMethod buildDecisionMethod(MainConsole.DecisionMethodEnum cl) {
        DecisionMethod decisionMethod;

        switch (cl) {
            case HillClimbing:
                decisionMethod = new HillClimbing(
                        Integer.valueOf(getParameter(0)),
                        Integer.valueOf(getParameter(1))
                );
                break;
            case HillClimbingBest:
                decisionMethod = new HillClimbingBest(
                        Integer.valueOf(getParameter(0)),
                        Integer.valueOf(getParameter(1))
                );
                break;
            case GeneticAlgorithm:
                decisionMethod = new GeneticAlgorithm(
                        Integer.valueOf(getParameter(0)),
                        Integer.valueOf(getParameter(1))
                );
                break;
            case SimulatedAnnealing:
                decisionMethod = new SimulatedAnnealing(
                        Integer.valueOf(getParameter(0)),
                        Double.valueOf(getParameter(1)),
                        Integer.valueOf(getParameter(2))
                );
                break;
            case ParticleSwarm:
                decisionMethod = new ParticleSwarm(
                        Integer.valueOf(getParameter(0)),
                        Integer.valueOf(getParameter(1))
                );
                break;
            default:
                throw new IllegalArgumentException("not supported DecisionMethodEnum");
        }

        return decisionMethod;
    }

    private String getParameter(int pos) {
        return ((TextField) parameterContainer.getChildren().get(1 + pos * 2)).getText();
    }

    @SuppressWarnings("unchecked")
    public void runHillClimbingOnResult(ActionEvent actionEvent) {
        final Decision upgradedDecision = new HillClimbingBest(currentTaskInfo.getResult(), 100, 100_000)
                .process(currentProblemTask)
                .getResult();
        final Objective upgradedMinObjective = currentProblemTask.calculateObjective(upgradedDecision);

        printRow("HillClimbingBest upgrade:"
                + "\nresult: " + upgradedMinObjective
                + "\n\tfor arguments:" + upgradedDecision);
    }

    public void continueMainMethodOnResult(ActionEvent actionEvent) {

        progressIndicator.setVisible(true);
        MainConsole.DecisionMethodEnum selectedItem = choiceMethod.getSelectionModel().getSelectedItem();

        DecisionMethod decisionMethod = buildDecisionMethod(selectedItem);

        if (decisionMethod.getClass() == currentDecisionMethod.getClass()) {
            currentDecisionMethod = decisionMethod;
        }



        Task <Void> t = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                currentTaskInfo = currentDecisionMethod.process(currentProblemTask, currentTaskInfo.getContinueData());

                printAndShowRes();
                return null;
            }
        };

        new Thread(t).start();
    }

    private void printRow(String row) {
        console.getItems().add(row);
    }


    @SuppressWarnings("unchecked")
    private void print(ProblemTask problemTask, ResultTaskInfo taskInfo) {
        final Decision result = taskInfo.getResult();
        final WorkStatistic statistic = taskInfo.getWorkStatistic();

        final Objective minObjective = problemTask.calculateObjective(result);

        printRow("method: " + taskInfo.getNameOfOptimizationMethod()
                + "\nresult: " + minObjective
                + "\n\tfor: " + result
                + "\n work statistic:\n" + statistic + "\n");

        int radiusFoundNeighbor = (int) (Math.log(2000) / Math.log( ((DiophantineEquation)currentProblemTask).getCoefficients().length));

        System.out.println("radius = " + radiusFoundNeighbor);

        Decision localOptimum = new HillClimbing(result, radiusFoundNeighbor, 1).process(problemTask).getResult();
        printRow("check is local optimum radius "+radiusFoundNeighbor+": "
                + result.equals(localOptimum)
                + " (local:" + localOptimum + ")");
    }

    private void showChart(ArrayList<Object[]> data) {
        XYChart.Series<Number, Number> numberNumberSeries = chartEvolutionBest.getData().size() != 0 ? chartEvolutionBest.getData().get(0) : new XYChart.Series<>();

        ObservableList<XYChart.Data<Number, Number>> observableList = numberNumberSeries.getData();
        int start = observableList.size()>0? (int) observableList.get(observableList.size() - 1).getXValue() : 0;

        System.out.println("start = [" + start + "]");
        for (int i = 0; i < data.size(); i ++) {
            observableList.add(new XYChart.Data<>(start + (Integer) (data.get(i)[0]), (BigInteger) (data.get(i)[1])));
        }

        if (chartEvolutionBest.getData().size() == 0) {
            chartEvolutionBest.getData().add(numberNumberSeries);
        }
    }


    private ArrayList<Object[]> getNumberNumberSeries(ResultTaskInfo taskInfo) {
        ArrayList<Object[]> res = new ArrayList<>();

        List<Decision> traceBest = ((GlobalWorkStatistic) taskInfo.getWorkStatistic()).getTraceBest();

        int countDot = 1000;
        int step = traceBest.size() / countDot + 1;

        for (int i = 0; i < traceBest.size(); i += step) {
            BigInteger bigInteger = currentProblemTask.calculateFitness(traceBest.get(i));
            res.add(new Object[] { i, bigInteger});
        }
        Decision end = traceBest.get(traceBest.size() - 1);
        BigInteger bigInteger = currentProblemTask.calculateFitness(end);
        res.add(new Object[] { traceBest.size() - 1, bigInteger});
        return res;
    }

    public void clearChart(ActionEvent actionEvent) {
        chartEvolutionBest.getData().clear();
    }
}
