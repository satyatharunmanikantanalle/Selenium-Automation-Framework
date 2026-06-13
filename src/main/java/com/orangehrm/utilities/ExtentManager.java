package com.orangehrm.utilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	    
        private static ExtentReports extent;
        private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
        private static Map<Long,WebDriver> driverMap = new HashMap<>();
        
        //Initialize the Extent Report
        public static ExtentReports getReporter() {
        if(extent == null) {
        	String reportPath =System.getProperty("user.dir")+"/src/test/resources/ExtentReports/ExtentReport.html";
        	ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        	spark.config().setReportName("Automation Test Report");
        	spark.config().setDocumentTitle("OrangeHRM Report");
        	spark.config().setTheme(Theme.DARK);
        	
        	extent = new ExtentReports();
        	//Adding system information
        	extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        	extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        	extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extent;
        }
        //start the test
        public static ExtentTest startTest(String testName) {
        	ExtentTest extentTest = getReporter().createTest(testName);
        }
        //Register webDriver for current Thread
        public static void registerDriver(WebDriver driver) {
        	driverMap.put(Thread.currentThread().getId(), driver);
        }
}
