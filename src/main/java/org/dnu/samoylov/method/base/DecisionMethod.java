package org.dnu.samoylov.method.base;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.method.base.statistic.GlobalWorkStatistic;
import org.dnu.samoylov.method.base.statistic.TraceBestStatistic;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

public abstract class DecisionMethod {

    public ResultTaskInfo process(ProblemTask task, ContinueData continueData) {
        final GlobalWorkStatistic workStatistic = new GlobalWorkStatistic();

        workStatistic.startEstimationTime();
        ResultTaskInfo resultTaskInfo = internalProcess(task, continueData);
        workStatistic.stopEstimationTime();

        workStatistic.setSpecificWorkStatistic((TraceBestStatistic) resultTaskInfo.getWorkStatistic());

        ResultTaskInfo resultTaskInfo1 = new ResultTaskInfo(resultTaskInfo.getNameOfOptimizationMethod(), resultTaskInfo.getResult(), workStatistic);
        resultTaskInfo1.setContinueData(resultTaskInfo.getContinueData());
        return resultTaskInfo1;
    }


    public ResultTaskInfo process(ProblemTask task) {
        final GlobalWorkStatistic workStatistic = new GlobalWorkStatistic();

        workStatistic.startEstimationTime();
        ResultTaskInfo resultTaskInfo = internalProcess(task);
        workStatistic.stopEstimationTime();

        workStatistic.setSpecificWorkStatistic((TraceBestStatistic) resultTaskInfo.getWorkStatistic());

        ResultTaskInfo resultTaskInfo1 = new ResultTaskInfo(resultTaskInfo.getNameOfOptimizationMethod(), resultTaskInfo.getResult(), workStatistic);
        resultTaskInfo1.setContinueData(resultTaskInfo.getContinueData());
        return resultTaskInfo1;
    }

    protected abstract <DECISION extends Decision, OBJECTIVE extends Objective>
                        ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task);

    protected abstract <DECISION extends Decision, OBJECTIVE extends Objective>
                        ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task, ContinueData continueData);

}
