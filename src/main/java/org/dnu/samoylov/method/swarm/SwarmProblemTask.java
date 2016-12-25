package org.dnu.samoylov.method.swarm;

import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

public interface SwarmProblemTask<DECISION extends Decision, OBJECTIVE extends Objective> extends ProblemTask<DECISION, OBJECTIVE> {

    DECISION sum(DECISION first, DECISION second);

    DECISION subtract(DECISION first, DECISION second);

    DECISION multiply(DECISION first, float m);

    DECISION createZero();

}
