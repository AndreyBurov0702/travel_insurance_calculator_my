package org.javaguru.travel.insurance.rest.v2;

import org.junit.jupiter.api.Test;

class TravelCalculatePremiumControllerTestCase8 extends TravelCalculatePremiumControllerV2TestCase {

    @Test
    public void execute() throws Exception{
        executeAndCompare();
    }

    @Override
    protected String getTestCaseFolderName() {
        return "test_case_8";
    }
}
