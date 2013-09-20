package hudson.plugins.view.dashboard.builds;

import hudson.model.FreeStyleProject;
import hudson.model.Job;
import hudson.model.Run;
import hudson.plugins.view.dashboard.RunLoadCounter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class LatestBuildsTest extends HudsonTestCase{

    @Test
    public void testAvoidEagerLoading() throws Exception {
        final FreeStyleProject p = createFreeStyleProject();
        RunLoadCounter.prepare(p);
        for (int i = 0; i < 5; i++) {
            assertBuildStatusSuccess(p.scheduleBuild2(0));
        }

        int numbuilds = 3;
        final LatestBuilds latest = new LatestBuilds("-", numbuilds) {

            @Override
            protected List<Job> getDashboardJobs() {
                return Collections.singletonList((Job) p);
            }

        };

        RunLoadCounter.assertMaxLoads(p, numbuilds, new Callable<List<Run>>() {

            public List<Run> call() throws Exception {
                return latest.getFinishedBuilds();
            }
        });
    }

}