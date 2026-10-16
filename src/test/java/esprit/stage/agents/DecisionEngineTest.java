package esprit.stage.agents;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the rule-based decision engine (no JADE runtime needed).
 */
public class DecisionEngineTest {

    // ---- Gas rules: OK <= 100%, WARNING <= 120%, CRITICAL > 120% ----

    @Test
    public void gasWithinLimitIsOk() {
        assertEquals("OK", DecisionEngine.analyzeGas(80000f, 100000f));
        assertEquals("OK", DecisionEngine.analyzeGas(100000f, 100000f));
    }

    @Test
    public void gasSlightlyAboveLimitIsWarning() {
        assertEquals("WARNING", DecisionEngine.analyzeGas(100001f, 100000f));
        assertEquals("WARNING", DecisionEngine.analyzeGas(120000f, 100000f));
    }

    @Test
    public void gasFarAboveLimitIsCritical() {
        assertEquals("CRITICAL", DecisionEngine.analyzeGas(120001f, 100000f));
        assertEquals("CRITICAL", DecisionEngine.analyzeGas(200000f, 100000f));
    }

    // ---- Response time rules: OK <= 100%, WARNING <= 150%, CRITICAL > 150% ----

    @Test
    public void responseTimeWithinLimitIsOk() {
        assertEquals("OK", DecisionEngine.analyzeResponseTime(10f, 30f));
        assertEquals("OK", DecisionEngine.analyzeResponseTime(30f, 30f));
    }

    @Test
    public void responseTimeSlightlyAboveLimitIsWarning() {
        assertEquals("WARNING", DecisionEngine.analyzeResponseTime(31f, 30f));
        assertEquals("WARNING", DecisionEngine.analyzeResponseTime(45f, 30f));
    }

    @Test
    public void responseTimeFarAboveLimitIsCritical() {
        assertEquals("CRITICAL", DecisionEngine.analyzeResponseTime(46f, 30f));
        assertEquals("CRITICAL", DecisionEngine.analyzeResponseTime(100f, 30f));
    }
}
