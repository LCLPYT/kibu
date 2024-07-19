package work.lclpnet.kibu.scheduler.util;

import org.slf4j.Logger;
import work.lclpnet.kibu.scheduler.RootScheduler;
import work.lclpnet.kibu.scheduler.api.Scheduler;

public class ChildScheduler extends Scheduler {

    private final RootScheduler parent;

    public ChildScheduler(RootScheduler parent, Logger logger) {
        super(logger);

        this.parent = parent;
        this.parent.addChild(this);
    }

    public void detach() {
        this.parent.removeChild(this);
    }
}
