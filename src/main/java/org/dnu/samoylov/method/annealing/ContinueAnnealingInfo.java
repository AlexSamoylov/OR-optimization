package org.dnu.samoylov.method.annealing;

import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.method.base.resume.ContinueWithOneDecisionInfo;
import org.dnu.samoylov.task.base.Decision;

public class ContinueAnnealingInfo extends ContinueWithOneDecisionInfo implements ContinueData {

    public ContinueAnnealingInfo(Decision startDecision) {
        super(startDecision);
    }

}
