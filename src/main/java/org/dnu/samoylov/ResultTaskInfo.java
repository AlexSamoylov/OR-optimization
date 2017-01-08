package org.dnu.samoylov;

import org.dnu.samoylov.method.base.statistic.WorkStatistic;
import org.dnu.samoylov.task.base.Decision;

public class ResultTaskInfo {
    private Decision result;
    private String nameOfOptimizationMethod;
    private WorkStatistic workStatistic;

    public ResultTaskInfo(String nameOfOptimizationMethod, Decision result, WorkStatistic workStatistic) {
        this.result = result;
        this.nameOfOptimizationMethod = nameOfOptimizationMethod;
        this.workStatistic = workStatistic;
    }

    public ResultTaskInfo(Decision result, WorkStatistic workStatistic) {
        this.result = result;
        this.workStatistic = workStatistic;
    }

    public Decision getResult() {
        return result;
    }

    public WorkStatistic getWorkStatistic() {
        return workStatistic;
    }

    public String getNameOfOptimizationMethod() {
        return nameOfOptimizationMethod;
    }

    public void setNameOfOptimizationMethod(String nameOfOptimizationMethod) {
        this.nameOfOptimizationMethod = nameOfOptimizationMethod;
    }
}
