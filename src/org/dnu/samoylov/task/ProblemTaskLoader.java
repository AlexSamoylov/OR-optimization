package org.dnu.samoylov.task;

public class ProblemTaskLoader {

    public<T extends ProblemTask> ProblemTask load(String path, Class<T> taskType) {
        if (taskType == DiophantineEquation.class) {
            return new DiophantineEquation();
        }

        throw new IllegalArgumentException("load logic not implements for this problem task");
    }

}
