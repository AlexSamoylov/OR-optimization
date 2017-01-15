package org.dnu.samoylov.method.base.resume;

import org.dnu.samoylov.task.base.Decision;

public class ContinueWithOneDecisionInfo implements ContinueData {

    private final Decision startDecision;

    public ContinueWithOneDecisionInfo(Decision startDecision) {
        this.startDecision = startDecision;
    }

    public Decision getStartDecision() {
        return startDecision;
    }
}
