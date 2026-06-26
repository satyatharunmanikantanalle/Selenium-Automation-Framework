package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String username, String password) {

        System.out.println("Running verifyValidLoginTest on Thread : "
                + Thread.currentThread().getId());

        ExtentManager.logStep("Logging into OrangeHRM");

        loginPage.login(username, password);

        ExtentManager.logStep("Verifying Admin tab");

        Assert.assertTrue(homePage.isAdminTabVisible(),
                "Admin tab is not visible after successful login.");

        ExtentManager.logStep("Admin tab verified successfully");

        homePage.logout();

        ExtentManager.logStep("Logout successful");
    }

    @Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)
    public void inValidLoginTest(String username, String password) {

        System.out.println("Running inValidLoginTest on Thread : "
                + Thread.currentThread().getId());

        ExtentManager.logStep("Logging in with invalid credentials");

        loginPage.login(username, password);

        String expectedErrorMessage = "Invalid credentials";

        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),
                "Expected error message was not displayed.");

        ExtentManager.logStep("Error message validated successfully");
    }

}