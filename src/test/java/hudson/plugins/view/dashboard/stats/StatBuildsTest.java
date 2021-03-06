package hudson.plugins.view.dashboard.stats;

import hudson.model.BallColor;
import hudson.model.FreeStyleProject;
import hudson.model.TopLevelItem;
import hudson.plugins.view.dashboard.RunLoadCounter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.jvnet.hudson.test.HudsonTestCase;

public class StatBuildsTest extends HudsonTestCase{

    @Test public void avoidEagerLoading() throws Exception {
        final FreeStyleProject p = createFreeStyleProject();
        RunLoadCounter.prepare(p);
        for (int i = 0; i < 15; i++) {
            assertBuildStatusSuccess(p.scheduleBuild2(0));
        }
        final StatBuilds stats = new StatBuilds("-");
        assertEquals(StatBuilds.MAX_BUILDS, RunLoadCounter.assertMaxLoads(p, StatBuilds.MAX_BUILDS + /* AbstractLazyLoadRunMap.headMap actually loads start, alas */1, new Callable<Integer>() {
            public Integer call() throws Exception {
                return stats.getBuildStat(Collections.<TopLevelItem>singletonList(p)).get(BallColor.BLUE);
            }
        }).intValue());
    }

    @Test public void testGettingBuildStatsWithZeroBuild() throws Exception {
        final FreeStyleProject project = createFreeStyleProject();
        RunLoadCounter.prepare(project);
        final StatBuilds stats = new StatBuilds("-");
        final Map<BallColor,Integer> buildStats = stats.getBuildStat(Collections.<TopLevelItem>singletonList(project));
        for (BallColor color : BallColor.values()) {
            assertEquals((Integer)0, buildStats.get(color.noAnime()));
        }
    }

}
