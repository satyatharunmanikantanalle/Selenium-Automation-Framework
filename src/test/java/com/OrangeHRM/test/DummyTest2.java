package com.OrangeHRM.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest2 extends BaseClass {

    @Test
    public void dummyTest() {
    	//ExtentManager.startTest("Dummy Test2");This has been implemented in TestListener
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying title");
        assert title.equals("OrangeHRM")
                : "Test Failed - Title not matched";
        System.out.println("Test Passed - Title is matching");
        ExtentManager.logStep("validation successfull");
    }
}