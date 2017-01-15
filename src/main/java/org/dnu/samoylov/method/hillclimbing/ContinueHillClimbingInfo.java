package org.dnu.samoylov.method.hillclimbing;

import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.method.base.resume.ContinueWithOneDecisionInfo;
import org.dnu.samoylov.task.base.Decision;

public class ContinueHillClimbingInfo extends ContinueWithOneDecisionInfo implements ContinueData {

    public ContinueHillClimbingInfo(Decision startDecision) {
        super(startDecision);
    }

}
