package org.dnu.samoylov.task;

import org.dnu.samoylov.method.diophantine.DiophantineEquation;

public class ProblemTaskLoader {

    public<T extends ProblemTask> ProblemTask load(String path, Class<T> taskType) {
        if (taskType == DiophantineEquation.class) {
            return null;
        }

        throw new IllegalArgumentException("load logic not implements for this problem task");
    }

}
