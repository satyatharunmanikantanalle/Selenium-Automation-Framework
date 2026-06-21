package com.OrangeHRM.test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.listeners.TestListener;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

@Listeners(TestListener.class)
public class LoginPageTest extends BaseClass {
	private LoginPage loginPage;
	private HomePage homePage;
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
   // ExtentManager.startTest("Valid Login Test"); //This has been implemented in TestListener
	System.out.println("Running testMethod1 on thread: " +Thread.currentThread().getId());
	ExtentManager.logStep("Navigating to Login Page entering username and password");
	loginPage.login(username,password);
	ExtentManager.logStep("Verifying admin tab is visible or not");
	Assert.assertTrue(homePage.isAdminTabVisible(),"admin should be visible");
	ExtentManager.logStep("validation successfull");
	homePage.logout();
	ExtentManager.logStep("Logged out successfully");
	staticWait(2);
	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String username, String password) {
		//ExtentManager.startTest("INValid Login Test"); This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: " +Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginPage.login(username,password);
		String expectedErrorMessage ="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test failed:Invalid error");
		ExtentManager.logStep("validation successfull");
		ExtentManager.logStep("Logged out successfully");
	}
}
