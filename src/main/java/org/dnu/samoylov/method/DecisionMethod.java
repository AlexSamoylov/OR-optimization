package org.dnu.samoylov.method;

import org.dnu.samoylov.Decision;
import org.dnu.samoylov.Objective;
import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.statistic.GlobalWorkStatistic;
import org.dnu.samoylov.task.ProblemTask;

import java.util.logging.Logger;

public abstract class DecisionMethod {

    private static final Logger log = Logger.getLogger(DecisionMethod.class.getName());

    public ResultTaskInfo process(ProblemTask task) {
        final GlobalWorkStatistic workStatistic = new GlobalWorkStatistic();

        workStatistic.startEstimationTime();
        log.info("start internalProcess for" + task);
        ResultTaskInfo resultTaskInfo = internalProcess(task);
        log.info("end internalProcess for" + task);
        workStatistic.stopEstimationTime();

        workStatistic.setSpecificWorkStatistic(resultTaskInfo.getWorkStatistic());

        return new ResultTaskInfo(resultTaskInfo.getResult(), workStatistic);
    }

    protected abstract <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task);

}
