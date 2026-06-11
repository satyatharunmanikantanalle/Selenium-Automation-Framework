package com.OrangeHRM.test;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;

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
	loginPage.login("Admin","admin123");
	Assert.assertTrue(homePage.isAdminTabVisible(),"admin should be visible");
	homePage.logout();
	staticWait(2);
	}
	
	@Test
	public void inValidLoginTest() {
		loginPage.login("admin","admin");
		String expectedErrorMessage ="Invalid credentials1";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test failed:Invalid error");
	}
}
