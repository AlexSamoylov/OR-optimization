package org.dnu.samoylov;

import org.dnu.samoylov.method.base.statistic.WorkStatistic;
import org.dnu.samoylov.task.base.Decision;

public class ResultTaskInfo {
    private Decision result;
    private WorkStatistic workStatistic;

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
}
