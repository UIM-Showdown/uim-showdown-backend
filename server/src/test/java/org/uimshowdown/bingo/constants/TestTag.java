package org.uimshowdown.bingo.constants;

/**
 * A collection of constants to be used in conjunction with the `@Tag` annotation used to
 * label tests at the class or method level.
 */
public class TestTag {
    /** 
     * Test the correct inter-operation of multiple subsystems There is whole spectrum there, from testing
     * integration between two classes, to testing integration with the production environment.
     */
    public final static String INTEGRATION_TEST = "IntegrationTest";

    /**
     * A test that was written when a bug was fixed. It ensures that this specific bug will not occur again.
     * The full name is "non-regression test". It can also be a test made prior to changing an application
     * to make sure the application provides the same outcome.
     */
    public final static String REGRESSION_TEST = "RegressionTest";

    /**
     * (AKA sanity check) A simple integration test where we just check that when the system under test is
     * invoked it returns normally and does not blow up.
     */
    public final static String SMOKE_TEST = "SmokeTest";

    /**
     * Specify and test one point of the contract of single method of a class. This should have a very narrow
     * and well defined scope. Complex dependencies and interactions to the outside world are stubbed or mocked
     */
    public final static String UNIT_TEST = "UnitTest";
}
