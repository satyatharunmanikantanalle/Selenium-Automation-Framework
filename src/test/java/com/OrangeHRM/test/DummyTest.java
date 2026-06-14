package com.OrangeHRM.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest extends BaseClass {

    @Test
    public void dummyTest() {
    	//ExtentManager.startTest("Dummy Test");This has been implemented in TestListener
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying title");
        assert title.equals("OrangeHRM")
                : "Test Failed - Title not matched";
        System.out.println("Test Passed - Title is matching");
        ExtentManager.logSkip("this case is skipped");
        throw new SkipException("skipping test as part of testing");
    }
}