package org.dnu.samoylov.task.base;

public interface Mediator<DECISION extends Decision> {
    DECISION extract();
}
