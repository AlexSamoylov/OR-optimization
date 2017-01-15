package org.dnu.samoylov.method.swarm;

import org.dnu.samoylov.method.base.resume.ContinueData;
import org.dnu.samoylov.task.base.Decision;

public class ContinueSwarmData implements ContinueData {
    private ParticleSwarm.Swarm swarm;

    public ContinueSwarmData(ParticleSwarm.Swarm swarm) {
        this.swarm = swarm;
    }

    public ParticleSwarm.Swarm<Decision> getSwarm() {
        return swarm;
    }
}
