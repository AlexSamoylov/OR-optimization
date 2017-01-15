package org.dnu.samoylov.method.base;

import org.dnu.samoylov.ResultTaskInfo;
import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.method.base.resume.ContinueWithOneDecisionInfo;
import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

public abstract class OneDecisionInitializeMethod extends DecisionMethod {

    protected Decision startDecision = null;

    @Override
    protected <DECISION extends Decision, OBJECTIVE extends Objective> ResultTaskInfo internalProcess(ProblemTask<DECISION, OBJECTIVE> task, ContinueData continueData) {
        startDecision = ((ContinueWithOneDecisionInfo) continueData).getStartDecision();

        return internalProcess(task);
    }


    @SuppressWarnings("unchecked")
    protected  <DECISION extends Decision, OBJECTIVE extends Objective> DECISION getStartNode(ProblemTask<DECISION, OBJECTIVE> task) {
        DECISION randomDecision = task.getRandomDecision();
        if (startDecision != null && startDecision.getClass() == randomDecision.getClass()) {
            Decision startDecision1 = startDecision;
            this.startDecision = null;
            return (DECISION) startDecision1;
        } else {
            return randomDecision;
        }
    }
}
