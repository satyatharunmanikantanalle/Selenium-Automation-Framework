package com.OrangeHRM.test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	@Test
	public void verifyValidLoginTest() {
   // ExtentManager.startTest("Valid Login Test"); //This has been implemented in TestListener
	System.out.println("Running testMethod1 on thread: " +Thread.currentThread().getId());
	ExtentManager.logStep("Navigating to Login Page entering username and password");
	loginPage.login("Admin","admin123");
	ExtentManager.logStep("Verifying admin tab is visible or not");
	Assert.assertTrue(homePage.isAdminTabVisible(),"admin should be visible");
	ExtentManager.logStep("validation successfull");
	homePage.logout();
	ExtentManager.logStep("Logged out successfully");
	staticWait(2);
	}
	
	@Test
	public void inValidLoginTest() {
		//ExtentManager.startTest("INValid Login Test"); This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: " +Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.login("admin","admin");
		String expectedErrorMessage ="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test failed:Invalid error");
		ExtentManager.logStep("validation successfull");
		ExtentManager.logStep("Logged out successfully");
	}
}
