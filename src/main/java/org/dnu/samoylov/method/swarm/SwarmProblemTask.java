package org.dnu.samoylov.method.swarm;

import org.dnu.samoylov.task.base.Decision;
import org.dnu.samoylov.task.base.Mediator;
import org.dnu.samoylov.task.base.Objective;
import org.dnu.samoylov.task.base.ProblemTask;

public interface SwarmProblemTask<DECISION extends Decision, OBJECTIVE extends Objective> extends ProblemTask<DECISION, OBJECTIVE> {

    Mediator<DECISION> sum(Mediator<DECISION> first, Mediator<DECISION> second);

    Mediator<DECISION> subtract(Mediator<DECISION> first, Mediator<DECISION> second);

    Mediator<DECISION> multiply(Mediator<DECISION> first, float m);

    Mediator<DECISION> createMediator(DECISION first);

    DECISION createZero();

}
