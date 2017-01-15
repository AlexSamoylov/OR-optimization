package org.dnu.samoylov.method.base;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.statistic.GlobalWorkStatistic;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

public abstract class DecisionMethod {

    public ResultTaskInfo process(ProblemTask task) {
        final GlobalWorkStatistic workStatistic = new GlobalWorkStatistic();

        workStatistic.startEstimationTime();
        ResultTaskInfo resultTaskInfo = internalProcess(task);
        workStatistic.stopEstimationTime();

        workStatistic.setSpecificWorkStatistic(resultTaskInfo.getWorkStatistic());

        return new ResultTaskInfo(resultTaskInfo.getNameOfOptimizationMethod(), resultTaskInfo.getResult(), workStatistic);
    }

    protected abstract <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task);

}
