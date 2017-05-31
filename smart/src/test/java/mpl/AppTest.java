package mpl;

//import junit.framework.Test;
import org.junit.Test;
import junit.framework.TestCase;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.runner.RunWith;
import research.mpl.backend.smart.metaheuristics.network.ProblemDataSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;

@RunWith(CdiTestRunner.class)
public class AppTest extends TestCase {

    @Inject
    private EntityManager entityManager;

    @Inject
    private ProblemDataSet dataSet;

    @Test
    public void testApp()
    {
        dataSet.init();
        assertTrue(true);
    }
}
