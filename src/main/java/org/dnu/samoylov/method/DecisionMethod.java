package org.dnu.samoylov.method;

import org.dnu.samoylov.Decision;
import org.dnu.samoylov.Objective;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.statistic.GlobalWorkStatistic;
import org.dnu.samoylov.task.ProblemTask;

public abstract class DecisionMethod {

    public ResultTaskInfo process(ProblemTask task) {
        final GlobalWorkStatistic workStatistic = new GlobalWorkStatistic();

        workStatistic.startEstimationTime();
        ResultTaskInfo resultTaskInfo = internalProcess(task);
        workStatistic.stopEstimationTime();

        workStatistic.setSpecificWorkStatistic(resultTaskInfo.getWorkStatistic());

        return new ResultTaskInfo(resultTaskInfo.getResult(), workStatistic);
    }

    protected abstract <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task);

}
