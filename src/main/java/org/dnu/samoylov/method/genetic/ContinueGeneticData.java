package org.dnu.samoylov.method.genetic;

import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.task.base.Decision;

import java.util.ArrayList;

public class ContinueGeneticData<DECISION extends Decision> implements ContinueData {

    private final ArrayList<GeneticAlgorithm.FitnessDecision<DECISION>> population;

    public ContinueGeneticData(ArrayList<GeneticAlgorithm.FitnessDecision<DECISION>> population) {
        this.population = population;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<GeneticAlgorithm.FitnessDecision<DECISION>> getPopulation() {
        return population;
    }
}
