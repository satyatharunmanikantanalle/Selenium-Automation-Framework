package com.orangehrm.listeners;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class TestListener implements ITestListener {
    // Triggered when a test starts satya
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        // Start logging in Extent Reports
        ExtentManager.startTest(testName);
        ExtentManager.logStep("Test Started : " + testName);
        System.out.println("Listener Triggered");
    }

    // Triggered when a test passes satya
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Test Passed Successfully!","Test End : " + testName + " - Test Passed");
    }

    // Triggered when a test fails satya
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMessage = result.getThrowable().getMessage();
        ExtentManager.logStep("Failure Reason : " + failureMessage);
         ExtentManager.logFailure(BaseClass.getDriver(),"Test Failed!","Test End : " + testName + " - Test Failed");
    }
    // Triggered when a test is skipped satya
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test Skipped : " + testName);
    }

    // Triggered before suite execution starts Satya
    @Override
    public void onStart(ITestContext context) {
        // Initialize Extent Report
        ExtentManager.getReporter();
    }

    // Triggered after suite execution completes Satya
    @Override
    public void onFinish(ITestContext context) {
        // Flush Extent Report
        ExtentManager.endTest();
    }
}